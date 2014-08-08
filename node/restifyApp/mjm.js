var restify = require('restify');

var server = restify.createServer();
server.use(restify.queryParser());
server.use(restify.bodyParser());

server.listen(3000);

server.get('/api/mjm', function( req, res, next ) {
  res.json({"foo":"bar"});
});

