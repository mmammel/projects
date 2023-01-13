const https = require('https');

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
            resolver({
                statusCode: 200,
                body: JSON.stringify(raceData)
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

exports.handler = async(event) => {
    return new Promise( function(resolve, reject) {
        getPage(callContext, resolve, reject );
    });
};


