// ------------ NodeJS runtime ---------------
// Add aws-sdk in package.json as a dependency
// Example:
// {
//     "dependencies": {
//         "aws-sdk": "^2.0.9",
//     }
// }
// Create your credentials file at ~/.aws/credentials (C:\Users\USER_NAME\.aws\credentials for Windows users)
// Format of the above file should be:
//  [default]
//  aws_access_key_id = YOUR_ACCESS_KEY_ID
//  aws_secret_access_key = YOUR_SECRET_ACCESS_KEY

const AWS = require('aws-sdk');

// Create the DynamoDB Client with the region you want
const region = 'us-east-1';
const dynamoDbClient = createDynamoDbClient(region);

function addRunnerToRace( payload, resolve, reject ) {
  // Call DynamoDB's updateItem API twice, once for the runner, once for the race
  let dynamoPromises = [];

  dynamoPromises.push(executeUpdateItem(dynamoDbClient, createUpdateRaceInput(payload)));
  dynamoPromises.push(executeUpdateItem(dynamoDbClient, createUpdateRunnerInput(payload)));
  
  Promise.allSettled(dynamoPromises).then(() => {
      console.info('UpdateItem API call has been executed.')
      resolve({
        statusCode: 200,
        body: JSON.stringify({ msg : "runner added" })
      });
  }).catch((err) => {
      resolve({
          statusCode: 202,
          body: "{ \"error\" : \"" + handleUpdateItemError(err) + "\" } "
      });
  });   
}

function createDynamoDbClient(regionName) {
  // Set the region
  AWS.config.update({region: regionName});
  // Use the following config instead when using DynamoDB Local
  // AWS.config.update({region: 'localhost', endpoint: 'http://localhost:8000', accessKeyId: 'access_key_id', secretAccessKey: 'secret_access_key'});
  return new AWS.DynamoDB();
}

function createUpdateRunnerInput(payload) {
  return {
    "TableName": "USRunners",
    "Key": {
      "raceRunnerKey": {
        "S": 'runner.'+payload.runnerId
      }
    },
    "UpdateExpression": " ADD #bfb00 :bfb00",
    "ExpressionAttributeValues": {
      ":bfb00": {
        "SS": [
          payload.raceId
        ]
      }
    },
    "ExpressionAttributeNames": {
      "#bfb00": "races"
    }
  }
}

function createUpdateRaceInput(payload) {
  return {
    "TableName": "USRunners",
    "Key": {
      "raceRunnerKey": {
        "S": 'race.'+payload.raceId
      }
    },
    "UpdateExpression": "SET #6a5c0 = :6a5c0 ADD #6a5c1 :6a5c1",
    "ExpressionAttributeValues": {
      ":6a5c0": {
        "N": ''+payload.dateNum
      },
      ":6a5c1": {
        "SS": [
          payload.runnerId
        ]
      }
    },
    "ExpressionAttributeNames": {
      "#6a5c0": "dateNum",
      "#6a5c1": "runners"
    }
  }
}

function executeUpdateItem(dynamoDbClient, updateItemInput) {
  // Call DynamoDB's updateItem API
    return dynamoDbClient.updateItem(updateItemInput).promise();
}

// Handles errors during UpdateItem execution. Use recommendations in error messages below to 
// add error handling specific to your application use-case. 
function handleUpdateItemError(err) {
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

exports.handler = async (event) => {
    // TODO implement
    console.log( event );
    let payload = event;
    if( event.body ) {
      payload = JSON.parse(event.body);
    }
    
    if( payload.raceId && payload.runnerId && payload.dateNum ) {
      return new Promise( function(resolve, reject) {
          addRunnerToRace(payload, resolve, reject );
      });
    } else {
      return new Promise.resolve({
        statusCode: 400,
        body: "{ \"error\" : \"Bad payload\" }"
      });
    }
};


