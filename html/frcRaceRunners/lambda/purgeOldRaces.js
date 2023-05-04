const AWS = require('aws-sdk');

// Create the DynamoDB Client with the region you want
const region = 'us-east-1';
const dynamoDbClient = createDynamoDbClient(region);

let racePages = [];
let emailAddr = null;

function doScan( lastKey, resolver ) {
  let scanInput = createScanInput(lastKey);
  console.log( "Scan input: " + JSON.stringify(scanInput) );
  executeScan(dynamoDbClient, scanInput).then((result) => {
    collectItems(result);
    if( typeof result.LastEvaluatedKey !== 'undefined' ) {
      console.log( "Have last evaluated: " + JSON.stringify(result.LastEvaluatedKey) );
      doScan( result.LastEvaluatedKey, resolver );
    } else {
      
      let flatten = [];
      for( var i = 0; i < racePages.length; i++ ) {
        flatten = flatten.concat( racePages[i] );
      }
      
      console.log("Returning " + flatten.length + " races");
      
      resolver({
        statusCode: 200,
        body: JSON.stringify(flatten)
      });
    }
  }).catch((err) => {
    resolver({
      statusCode: 202,
      body: "{ \"error\" : \"" + handleScanError(err) + "\" } "
    });
  });
}

function collectItems( result ) {
  let items = result["Items"];
  console.log('Processing ' + items.length + ' items.');
  let remapped = items.map((obj) => {
    let retVal = {};
    retVal['id'] = obj['nameAndDate']['S'];
    retVal['name'] = obj['name']['S'];
    retVal['date'] = obj['date']['S'];
    retVal['url'] = obj['url'] ? obj['url']['S'] : 'N/A';
    retVal['distance'] = obj['distance']['S'];
    retVal['location'] = obj['location']['S'];
    if( obj['runners'] ) {
      if( emailAddr && obj['runners']['SS'].includes(emailAddr.toLowerCase()) ) {
          retVal['running'] = true;
      }
      
      retVal['runners'] = obj['runners']['SS'].length;
    } else {
      retVal['runners'] = 0;
    }
    
    return retVal;
  });
  racePages.push( remapped );
}

function createDynamoDbClient(regionName) {
  // Set the region
  AWS.config.update({region: regionName});
  // Use the following config instead when using DynamoDB Local
  // AWS.config.update({region: 'localhost', endpoint: 'http://localhost:8000', accessKeyId: 'access_key_id', secretAccessKey: 'secret_access_key'});
  return new AWS.DynamoDB();
}

function createScanInput(todayDate, lastKey) {
  return (typeof lastKey !== 'undefined') ? {
    "TableName": "USRunningRaces",
    "ConsistentRead": false,
    "ExclusiveStartKey" : lastKey
  } : {
    "TableName": "USRunningRaces",
    "ConsistentRead": false
  }


  return {
    "TableName": "USRunningRaces",
    "ConsistentRead": false,
    "FilterExpression": "#1e630 <= :1e630",
    "ExpressionAttributeValues": {
      ":1e630": {
        "N": "20230416"
      }
    },
    "ExpressionAttributeNames": {
      "#1e630": "dateNum"
    }
  }
}

function executeScan(dynamoDbClient, scanInput) {
  // Call DynamoDB's scan API
  return dynamoDbClient.scan(scanInput).promise();
}

// Handles errors during Scan execution. Use recommendations in error messages below to 
// add error handling specific to your application use-case. 
function handleScanError(err) {
  if (!err) {
    console.error('Encountered error object was empty');
    return;
  }
  if (!err.code) {
    console.error(`An exception occurred, investigate and configure retry strategy. Error: ${JSON.stringify(err)}`);
    return;
  }
  // here are no API specific errors to handle for Scan, common DynamoDB API errors are handled below
  handleCommonErrors(err);
}

function handleCommonErrors(err) {
  switch (err.code) {
    case 'InternalServerError':
      console.error(`Internal Server Error, generally safe to retry with exponential back-off. Error: ${err.message}`);
      return;
    case 'ProvisionedThroughputExceededException':
      console.error(`Request rate is too high. If you're using a custom retry strategy make sure to retry with exponential back-off. `
        + `Otherwise consider reducing frequency of requests or increasing provisioned capacity for your table or secondary index. Error: ${err.message}`);
      return;
    case 'ResourceNotFoundException':
      console.error(`One of the tables was not found, verify table exists before retrying. Error: ${err.message}`);
      return;
    case 'ServiceUnavailable':
      console.error(`Had trouble reaching DynamoDB. generally safe to retry with exponential back-off. Error: ${err.message}`);
      return;
    case 'ThrottlingException':
      console.error(`Request denied due to throttling, generally safe to retry with exponential back-off. Error: ${err.message}`);
      return;
    case 'UnrecognizedClientException':
      console.error(`The request signature is incorrect most likely due to an invalid AWS access key ID or secret key, fix before retrying. `
        + `Error: ${err.message}`);
      return;
    case 'ValidationException':
      console.error(`The input fails to satisfy the constraints specified by DynamoDB, `
        + `fix input before retrying. Error: ${err.message}`);
      return;
    case 'RequestLimitExceeded':
      console.error(`Throughput exceeds the current throughput limit for your account, `
        + `increase account level throughput before retrying. Error: ${err.message}`);
      return;
    default:
      console.error(`An exception occurred, investigate and configure retry strategy. Error: ${err.message}`);
      return;
  }
}

exports.handler = async (event) => {
    // TODO implement
    console.log( event );
    let payload = event;
    if( event.body ) {
      payload = JSON.parse(event.body);
    }
    
    emailAddr = payload.email;
    racePages = [];
    
    return new Promise( function(resolve, reject) {
        doScan(null, resolve);
    });
};

