var http = require('./http');
var ws = require('./ws');
var mongo = require('./db/mongo');

function listen(http_port, ws_port, callback) {
    mongo().then(function(db) {
        http.listen(http_port);
        ws.listen(ws_port);
        callback();
    }, function(err) {
        console.log('Failed to connect to Mongo.');
        console.log(err);
    });
};

exports.listen = listen;
