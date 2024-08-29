import { DynamoDB } from '@aws-sdk/client-dynamodb';
import { DynamoDBDocument } from '@aws-sdk/lib-dynamodb';

const dynamoDbClient = DynamoDBDocument.from(new DynamoDB());

let polyhedrons = [];

export const handler = async (event) => {
  // TODO implement
  console.log("Event: " + JSON.stringify(event) );
  let results;
  let body = { status: 'success' };
  let statusCode = '200';
  const headers = {
    'Content-Type': 'application/json',
  };
  
  let method = event.requestContext.http.method;
    
  try {
    if( method == 'GET' ) { 
      if( event[ "queryStringParameters" ] && event[ "queryStringParameters" ]["name"] ) {
        await doQuery(event[ "queryStringParameters" ]["name"]);
      } else {
        await doScan(null);
      }
      results = polyhedrons;
    } else if( method == "POST" && event.body) {
      // validate, clean
      let item = JSON.parse(event.body);
      item = processItem(item);
      if( item["ERROR"] == "true" ) {
        statusCode = '400';
        results = "Bad request";
        body.status = "failure";
      } else {
        await doPut(item);
        results = "Shape Added";
      }
    } else {
      statusCode = '400';
      results = "Unknown Method";
      body.status = 'failure';
    }

  } catch( err ) {
    statusCode = '202';
    results = err.message;
    body.status = 'failure';
  } finally {
    //body.results = JSON.stringify(results);
    body.results = results;
  }
  
  return {
    statusCode,
    body,
    headers
  };
};

/* For Put */
function processItem( item ) {
  let retVal = {};
  let now = new Date();
  if( item["name"] && item["vertices"] ) {
    retVal["name"] = item["name"];
    retVal["vertices"] = item["vertices"];
    retVal["description"] = item["description"] ? item["description"] : "N/A";
    retVal["created_date"] = now.toISOString();
  } else {
    retVal["ERROR"] = "true";
  }
  
  return retVal;
}

async function doPut( item ) {
  let putInput = createPutInput(item);
  console.log( "Put input: " + JSON.stringify( putInput ) );
  await dynamoDbClient.put(putInput);
}

function createPutInput( item ) {
  return {
    "TableName" : "polyhedron",
    "Item" : item
  };
}

/* For Query */

async function doQuery( name ) {
  let queryInput = createQueryInput(name);
  console.log( "Query input: " + JSON.stringify(queryInput) );
  const result = await dynamoDbClient.query(queryInput);
  polyhedrons = polyhedrons.concat( result["Items"] );
}

function createQueryInput(name) {
  return {
    "TableName": "polyhedron",
    "ScanIndexForward": true,
    "ConsistentRead": false,
    "KeyConditionExpression": "#6ae90 = :6ae90",
    "ExpressionAttributeValues": {
      ":6ae90": name
    },
    "ExpressionAttributeNames": {
      "#6ae90": "name"
    }
  };
}

/* For Scan */
async function doScan( lastKey ) {
  let scanInput = createScanInput(lastKey);
  console.log( "Scan input: " + JSON.stringify(scanInput) );
  const result = await dynamoDbClient.scan(scanInput);
  polyhedrons = polyhedrons.concat( result["Items"] );
  
  if( result.LastEvaluatedKey != null ) {
    console.log( "Have last evaluated: " + JSON.stringify(result.LastEvaluatedKey) );
    doScan( result.LastEvaluatedKey );
  }
}

function createScanInput(lastKey) {
  return (lastKey != null) ? {
     "TableName" : "polyhedron",
     "ConsistentRead" : false,
     "ExclusiveStartKey" : lastKey
  } : {
    "TableName" : "polyhedron",
    "ConsistentRead" : false    
  };
}



