var http = require('./http')
var ws = require('./ws')

function listen(http_port, ws_port, callback) {
    http.listen(http_port, null);
    ws.listen(ws_port);
    callback();
};

exports.listen = listen;
