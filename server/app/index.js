var fs = require('fs');
var https = require('https');
var http = require('./http');
var ws = require('./ws');
var mongo = require('./db/mongo');

var options = {
    key: fs.readFileSync('certs/server.key'),
    cert: fs.readFileSync('certs/server.crt'),
    requestCert: false,
    rejectUnauthorized: false
};

function listen(http_port, ws_port, callback) {
    http.listen(http_port);
    var server = https.createServer(options, http).listen(3002);
    ws.listen(ws_port);
    callback();
};

exports.listen = listen;
