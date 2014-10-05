var http = require('./http');
var ws = require('./ws');
var mongo = require('./db/mongo');

function listen(http_port, ws_port, callback) {
    http.listen(http_port);
    ws.listen(ws_port);
    callback();
};

exports.listen = listen;
