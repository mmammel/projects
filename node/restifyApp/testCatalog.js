var ElasticSearchClient = require('elasticsearchclient');
var restify = require('restify');

var esServerConfig = {
  host: 'localhost',
  pathPrefix: '/elastic',
  port: 80,
  secure: false
};

var searchClient = new ElasticSearchClient( esServerConfig );

var server = restify.createServer();
server.use(restify.queryParser());
server.use(restify.bodyParser());

server.listen(3000);

/*
  Here as a reminder to implement error handling...
*/
var getTestProto = function( req, res, next ) {
  searchClient.get( 'assessments','test', req.params.id )
   .on( 'data', function(data) {
    })
   .on( 'error', function(error) {
   })
   .on( 'done', function() {
   })
   .exec();
};

var getTest = function( req, res, next ) {
  searchClient.get( 'assessments','test', req.params.testKey )
  .on( 'data', function(data) {
    res.json(JSON.parse(data));
   }).exec();
};

var getHeaders = function( req, res, next ) {
  searchClient.search( 'assessments','catalogheader', { query: { match_all: {} }, size: 100 } )
  .on( 'data', function(data) {
    res.json(JSON.parse(data));
   }).exec();
};

var getMenu = function( req, res, next ) {
  searchClient.search( 'assessments','catalogheader', { query: { match_all: {} }, size: 100 } )
  .on( 'data', function(data) {
//console.log(data);
    res.json(JSON.parse(data));
   }).exec();
};

var getSubMenu = function(req, res, next ) {
  var headerId = req.params.headerId;
  searchClient.search( 'assessments', headerId, { query: { match_all: {} }, size: 100 } )
  .on( 'data', function(data) {
    res.json(JSON.parse(data));
  }).exec();
};

var doSearch = function(req, res, next ) {
  var queryStr = '*' + req.params.q + '*';
  var offset = req.params.o ? req.params.o : 0;
  var count = req.params.c ? req.params.c : 9999;
  searchClient.search( 'assessments', 'test', { from: offset, size: count, query: { query_string: { query: queryStr } } } )
  .on( 'data', function(data) {
//console.log( data );
    res.json(JSON.parse(data));
  }).exec();
};

var doOrFilter = function( req, res, next ) {
console.log( req );
  var searchJSON = '{ "query" : { "constant_score" : { "filter" : { "or" : ' + buildFilter( req.params.data ) + '} } } }';
  searchClient.search( 'assessments', 'test', JSON.parse( searchJSON ) )
   .on( 'data', function(data) {
      res.json(data);
   }).exec();
};

function buildFilter( selectedOptions ) {
  var retVal = '[ ';
  var currField = '';
  for( var i = 0; i < selectedOptions.length; i++ ) {
    currField = selectedOptions[i].topic;
    for( var j = 0; j < selectedOptions[i].checkedvalues.length; j++ ) {
      if( i > 0 || j > 0 ) retVal += ',';
      retVal = retVal + '{ "term" : { "' + currField + '" : "' + selectedOptions[i].checkedvalues[j].toLowerCase() + '" } }';
    }
  }
  retVal += ' ]';
console.log( retVal );

  return retVal;
}

function esFilterFromArray( fieldname, valArray ) {
  var retVal = '[ ';
  for( var i = 0; i < valArray.length; i++ ) {
    if( i > 0 ) retVal += ',';
    retVal = retVal + '{ "term" : { "' + fieldname + '" : "' + valArray[i] + '" } }';
  } 
  retVal += ' ]';

  return retVal;
}

server.get('/api/menu', getMenu );
server.get('/api/headers', getHeaders );
server.get('/api/subMenu/:headerId', getSubMenu );
server.get('/api/test/:testKey', getTest );
server.get('/api/search', doSearch );
server.post('/api/orfilter', doOrFilter );

