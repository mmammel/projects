
/**
 * Module dependencies.
 */

var express = require('express');
var routes = require('./routes');
var user = require('./routes/user');
var http = require('http');
var path = require('path');
var elastical = require('elastical');

var app = express();

// all environments
app.set('port', process.env.PORT || 3000);
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'jade');
app.enable('trust proxy');
app.use(express.favicon());
app.use(express.logger('dev'));
app.use(express.json());
app.use(express.urlencoded());
app.use(express.methodOverride());
app.use(app.router);
app.use(express.static(path.join(__dirname, 'public')));

// development only
if ('development' == app.get('env')) {
  app.use(express.errorHandler());
}

app.get('/', routes.index);
app.get('/users', user.list);
var foobar = 'Jomama';
var searchClient = new elastical.Client('localhost',{port:"80",basePath:"/elastic",curlDebug:"true"});
app.get('/api/getTest', function( req, res ) {
  console.log( req.query.id );
  var result = {};
  searchClient.get('assessments',req.query.id, function( err, results, res ) {
    result = res;
  });
  res.send(result);
});

app.post('/api/search', function( req, res ) {
  console.log( req.body.query );
  res.send({result:'foobar'});
});

http.createServer(app).listen(app.get('port'), function(){
  console.log('Express server listening on port ' + app.get('port'));
});
