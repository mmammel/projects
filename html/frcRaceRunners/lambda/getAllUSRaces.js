const https = require('https');
const AWS = require('aws-sdk');

// Create the DynamoDB Client with the region you want
const region = 'us-east-1';
const s3Client = createS3Client(region);

const raceStateData = [
  [],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],
  [],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],
  [],[],[],[],[],[],[],[],[],[],[]
];

function createS3Client(regionName) {
    // Set the region
  AWS.config.update({region: regionName});
  // Use the following config instead when using DynamoDB Local
  // AWS.config.update({region: 'localhost', endpoint: 'http://localhost:8000', accessKeyId: 'access_key_id', secretAccessKey: 'secret_access_key'});
  return new AWS.S3({apiVersion: '2006-03-01'});
}

function doUpload( data, resolve ) {
    let params = {
        Bucket: 'www.mjmtools.com',
        Key: 'frc/js/races.json',
        Body: data
    };
    
    let options = {partSize: 10 * 1024 * 1024, queueSize: 1};
    
    s3Client.upload( params, options, (err, data ) => {
       if( err ) {
           resolve({
               statusCode: 202,
               message: "Error: " + err
           });
       } else {
           resolve({
               statusCode: 200,
               message: "Success!"
           });
       }
    });
}

function makeId( str ) {
  return 'R' + str.replace(/[^-A-Za-z0-9_.]/g, '_');
}

// 16 May 23 -> 20230516
function dateToDateNum( date ) {
  date = date.trim();
  var arr = date.split(" ");
  
  var yearNum = 0;
  var monthNum = 0;
  var dayNum = 0;
  
  if( arr[1] === "Jan" ) { monthNum = 1; }
  else if( arr[1] === "Feb" ) { monthNum = 2; }
  else if( arr[1] === "Mar" ) { monthNum = 3; }
  else if( arr[1] === "Apr" ) { monthNum = 4; }
  else if( arr[1] === "May" ) { monthNum = 5; }
  else if( arr[1] === "Jun" ) { monthNum = 6; }
  else if( arr[1] === "Jul" ) { monthNum = 7; }
  else if( arr[1] === "Aug" ) { monthNum = 8; }
  else if( arr[1] === "Sep" ) { monthNum = 9; }
  else if( arr[1] === "Oct" ) { monthNum = 10; }
  else if( arr[1] === "Nov" ) { monthNum = 11; }
  else if( arr[1] === "Dec" ) { monthNum = 12; }
  
  yearNum = (2000 + parseInt(arr[2])) * 10000;
  monthNum = monthNum * 100;
  dayNum = parseInt(arr[0]);
  
  return yearNum + monthNum + dayNum;
}

function createJSONItem(data) {
  var id = makeId( ""+data[5]+"-"+data[3]);
  
  return {
    id : id,
    name: data[5],
    date: data[3],
    dateNum : dateToDateNum(data[3]),
    location: data[7],
    distance: data[6],
    url: data[4]
  }
}

let raceData = [];
const monthStrs = [ "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" ];

function getStatePages(state) {
  return new Promise( function( resolver, rejector ) {
    getAllStatePages(state, resolver, rejector );
  }).then(() => {
    console.log( "Finished state " + state );
  });
}

function getAllStatePages( state, resolver, rejecter ) {
  new Promise( function( resolve, reject ) {
    var now = new Date();
    var dateStr =  monthStrs[now.getMonth()] + "%2F" + now.getDate() + "%2F" + now.getFullYear();
    getDatePagesForState( state, dateStr, resolve );
  }).then(() => {
    console.log( "Got all state pages for " + state );
    resolver("Done with " + state );
  });
}

function getDatePagesForState( state, date, resolver ) {
  new Promise( function( resolve, reject) {
    let page = 1;
    getNextPageForState( page, state, date, resolve );
  }).then((result) => {
    console.log( "Retrieved " + raceStateData[state - 1].length + " races!");

    let tempRace = [];
    //let tempPutObject = {};
    for( var i = 0; i < raceStateData[state - 1].length; i++ ) {
      tempRace = raceStateData[state - 1][i];
      tempRace[5] = tempRace[5].replace(/\&#039;/g,"'");
      tempRace[5] = tempRace[5].replace(/\&amp;/g,'&');
      tempRace[5] = tempRace[5].replace(/\&quot;/g,'"');
      tempRace[5] = tempRace[5].replace(/\&lt;/g,'<');
      tempRace[5] = tempRace[5].replace(/\&gt;/g,'>');
    }
    
    //Promise.allSettled(putPromises).then(() => {
    if( result === "done" ) {
      // we are totally done.
      // resolver("done");
      // Upload the data
      // doUpload( JSON.stringify(raceData), resolver );
      resolver("done");
    } else {
      // get the last date
      var lastDate = dateToDateFrom( raceStateData[state - 1][ raceStateData[state - 1].length - 1 ][3] );
      // empty the raceData
      //raceData = [];
      getDatePagesForState( state, lastDate, resolver );
    }
    //}); 
  });
}

function getNextPageForState( page, state, date, resolver ) {
  let data = '';
  let myResolver = resolver;
  console.log("Requesting: " + 'https://race-find.com/us/races?query=&state='+state+'&date_from='+date+'&type=&race-page='+page+'&per-page=15' );
  https.get( 'https://race-find.com/us/races?query=&state='+state+'&date_from='+date+'&type=&race-page='+page+'&per-page=15', (res) => {
     res.on('data', (d) => {
         data += d.toString();  
     });
     res.on('end', () => {
        let pageData = parsePage(data);
        for( var i = 0; i < pageData.length; i++ ) {
          raceStateData[state - 1].push(pageData[i]);
        }
        
        if( data.indexOf("last disabled") >= 0 ) {
          // last page, but it's page 34, so press on.
          if( page < 34 ) {
            // we're done.
            myResolver("done");              
          } else {
            myResolver("notDone");
          }
        } else if( data.indexOf("No results found.") >= 0 || data.indexOf('<ul class="pagination">') < 0 ) {
          // only one page, or no results
          myResolver("done");
        } else {
          getNextPageForState(++page, state, date, myResolver);
        }
     });
  }).on('error', (e) => {
    console.log("Error on page" + context.page );
    myResolver("Error");
  });
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

function dateToDateFrom( date ) {
  // "03 Feb 23" -> "02%2F03%2F2023"
  date = date.trim();
  var arr = date.split(" ");
  var month = "01";
  if( arr[1] === "Jan" ) { month = "01"; }
  else if( arr[1] === "Feb" ) { month = "02"; }
  else if( arr[1] === "Mar" ) { month = "03"; }
  else if( arr[1] === "Apr" ) { month = "04"; }
  else if( arr[1] === "May" ) { month = "05"; }
  else if( arr[1] === "Jun" ) { month = "06"; }
  else if( arr[1] === "Jul" ) { month = "07"; }
  else if( arr[1] === "Aug" ) { month = "08"; }
  else if( arr[1] === "Sep" ) { month = "09"; }
  else if( arr[1] === "Oct" ) { month = "10"; }
  else if( arr[1] === "Nov" ) { month = "11"; }
  else if( arr[1] === "Dec" ) { month = "12"; }
  
  return month + "%2F" + arr[0] + "%2F20" + arr[2];
}

exports.handler = async(event) => {
    return new Promise( function(resolve, reject) {
        //getPage(callContext, resolve, reject );
        //getAllPages( resolve, reject );
        let statePromises = [];
        for( var i = 1; i <= 51; i++ ) {
          statePromises.push( getStatePages(i) );
        }
        
        Promise.allSettled(statePromises).then(() => {
          
          let allData = [];
          let tempRace = null;
          for( var i = 0; i < 51; i++ ) {
            for( var j = 0; j < raceStateData[i].length; j++ ) {
              tempRace = raceStateData[i][j];
              allData.push( createJSONItem(tempRace) );
            }
          }
          
          doUpload( JSON.stringify( allData ), resolve );
        });
    });
};
