const https = require('https');
const AWS = require('aws-sdk');

// Create the DynamoDB Client with the region you want
const region = 'us-east-1';
const dynamoDbClient = createDynamoDbClient(region);

function createDynamoDbClient(regionName) {
  // Set the region
  AWS.config.update({region: regionName});
  // Use the following config instead when using DynamoDB Local
  // AWS.config.update({region: 'localhost', endpoint: 'http://localhost:8000', accessKeyId: 'access_key_id', secretAccessKey: 'secret_access_key'});
  return new AWS.DynamoDB();
}

// data: [ "loclong", "pageIdx", "globalIdx", "16 May 23", "url", "name", "distance", "location" ]
function createPutItemInput(data) {
  return {
    "TableName": "USRunningRaces",
    "Item": {
      "nameAndDate": {
        "S": ""+data[5]+"-"+data[3]
      },
      "name": {
        "S": data[5]
      },
      "date": {
        "S": data[3]
      },
      "location": {
        "S": data[7]
      },
      "distance": {
        "S": data[6]
      },
      "url": {
          "S": data[4]
      }
    },
    "ConditionExpression": "NOT (attribute_exists(#3ca70))",
    "ExpressionAttributeNames": {
      "#3ca70": "name"
    }
  }
}

function executePutItem(dynamoDbClient, putItemInput) {
  // Call DynamoDB's putItem API
  try {
    return dynamoDbClient.putItem(putItemInput).promise();
    // Handle putItemOutput
  } catch (err) {
    handlePutItemError(err);
    return new Promise((resolve,reject) => {
       resolve("skipped"); 
    });
  }
}

const raceData = [];

const callContext = {
    page: 1,
    lastId: 'init',
    data: null
};

function getPage( context, resolver, rejecter ) {
    let retVal = true;
    let pageData = null;
    let id = '-1';
    if( context.data ) {
        // we've received data for a page, extract the last ID from the payload
        pageData = parsePage(context.data);
        id = pageData[ pageData.length - 1 ][2];
        if( id !== context.lastId ) {
            context.lastId = id;
            for( let i = 0; i < pageData.length; i++ ) {
                raceData.push( pageData[i] );
            }
        } else {
            console.log( "Retrieved " + raceData.length + " races!");
            console.log( "Loading races into dynamoDB...")
            
            let putPromises = [];
            let tempRace = [];
            let tempPutObject = {};
            for( var i = 0; i < raceData.length; i++ ) {
                tempRace = raceData[i];
                tempPutObject = createPutItemInput(tempRace);
                 // Call DynamoDB's putItem API
                putPromises.push(executePutItem(dynamoDbClient, tempPutObject).then(() => {
                    console.info('PutItem API call has been executed.')
                  }
                ).catch((err) => {
                    handlePutItemError(err);
                }));
            }
            
            Promise.allSettled(putPromises).then(() => {
                resolver({
                    statusCode: 200,
                    body: JSON.stringify(raceData)
                });
            });
        }
    }
    
    if( retVal ) {
        // get another page
        context.data = '';
        https.get( 'https://race-find.com/us/races?query=&state=&date=&type=&race-page='+context.page+'&per-page=15', (res) => {
             res.on('data', (d) => {
                 context.data += d.toString();
             });
             res.on('end', () => {
                context.page++;
                getPage(context,resolver,rejecter);
             });
        }).on('error', (e) => {
            rejecter(e);
        });
    }
}

function loadRaceDataIntoDynamo(resolver) {
    
}

function parsePage( data ) {
    let retVal = [];
    let rowMatcher = new RegExp('<tr data-geo="(.*?)".*?data-key="(.*?)"><td>(.*?)</td><td></td><td>(.*?)</td><td><a.*?href="(.*?)".*target="_blank">(.*?)</a></td><td>(.*?)</td><td>(.*?)</td></tr>', 'g');
    let m = null;
    while( (m = rowMatcher.exec(data) ) != null ) {
        m.shift();
        retVal.push(m);
    }
    
    return retVal;
}


// Handles errors during PutItem execution. Use recommendations in error messages below to 
// add error handling specific to your application use-case. 
function handlePutItemError(err) {
  if (!err) {
    console.error('Encountered error object was empty');
    return;
  }
  if (!err.code) {
    console.error(`An exception occurred, investigate and configure retry strategy. Error: ${JSON.stringify(err)}`);
    return;
  }
  switch (err.code) {
    case 'ConditionalCheckFailedException':
      console.error(`Condition check specified in the operation failed, review and update the condition check before retrying. Error: ${err.message}`);
      return;
    case 'TransactionConflictException':
      console.error(`Operation was rejected because there is an ongoing transaction for the item, generally safe to retry ' +
       'with exponential back-off. Error: ${err.message}`);
       return;
    case 'ItemCollectionSizeLimitExceededException':
      console.error(`An item collection is too large, you're using Local Secondary Index and exceeded size limit of` + 
        `items per partition key. Consider using Global Secondary Index instead. Error: ${err.message}`);
      return;
    default:
      break;
    // Common DynamoDB API errors are handled below
  }
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

exports.handler = async(event) => {
    return new Promise( function(resolve, reject) {
        getPage(callContext, resolve, reject );
    });
};

