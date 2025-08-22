import * as https from 'node:https';
import { S3 } from '@aws-sdk/client-s3';

const s3 = new S3();

/**
 * Pass the data to send as `event.data`, and the request options as
 * `event.options`. For more information see the HTTPS module documentation
 * at https://nodejs.org/api/https.html.
 *
 * Will succeed with the response body.
 */
const getTodaysSolution = (resolver) => {
    const options = {
        hostname: 'www.nytimes.com',
        port: 443,
        path: '/puzzles/spelling-bee',
        method: 'GET'
    };
    
    const req = https.request(options, (res) => {
        let body = '';
        console.log('Status:', res.statusCode);
        console.log('Headers:', JSON.stringify(res.headers));
        res.setEncoding('utf8');
        res.on('data', (chunk) => {
            if( chunk ) {
                body += chunk;
            }
        });
        res.on('end', () => {
            console.log('Successfully processed HTTPS response');
            // If we know it's JSON, parse it
            parseAnswers(body, resolver);
        });
    });
    req.on('error', (err) => {
        console.log('Error! ' + err);
        resolver(err);
    });
    req.end();
};

const parseAnswers = (body, resolver ) => {
  let solutionMatcher = new RegExp('window.gameData.*?({.*?)</script>');
  let m = null;
  if((m = solutionMatcher.exec(body)) != null ) {
      resolver(JSON.stringify(JSON.parse(m[1])));
  } else {
      resolver("No solution found");
  }
};

const processManualInput = async (letters, resolver) => {
    const params = {
        Bucket: 'mjmtools',
        Key: 'scrabble_words.txt'
    };
    
    let result = {};
    let resultArray = [];
    const validRegex = /^[a-z]{7,}$/;
    
    if( validRegex.test(letters) ) {
        let keyLet = letters.slice(-1);
        console.log( "Valid letters: " + letters + " using keyLet: " + keyLet );
        const letterMatcher = new RegExp("^["+letters+"]{4,}$", "g");
        console.log( "Using pattern: " + letterMatcher.toString() );
        try {
            const data = await s3.getObject(params);
            const wordString = await data.Body.transformToString();
            const wordArray = wordString.split("\n");
            wordArray.forEach( (word) => {
               if( letterMatcher.test(word) && word.indexOf(keyLet) > -1 ) {
                   resultArray.push(word);
               } 
            });
            
            resolver( JSON.stringify(resultArray) );
        } catch( err ) {
            resolver("error");
        }
    } else {
        resolver("error");
    }
};

export const handler = (event, context, callback) => {
    console.log("Event: " + JSON.stringify(event) );
    return new Promise( function( resolve, reject ) { 
        let todaysSolution = true;
        let letters = '';
        if( event.queryStringParameters ) {
          if( event.queryStringParameters["s"] ) {
            letters = event.queryStringParameters["s"];
            todaysSolution = false;
          } else if( event.queryStringParameters["t"] ) {
            todaysSolution = true;
          }
        }

        if( todaysSolution ) {
          getTodaysSolution(resolve);
        } else {
          processManualInput(letters, resolve );
        }
    });
};

