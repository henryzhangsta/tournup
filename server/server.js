var cluster = require('cluster');
var app = require('./app');
var count = require('os').cpus().length;

if(!process.env.NO_CLUSTER && cluster.isMaster) {
    for (var i = 0; i < count; i++) {
        cluster.fork();
    }
    cluster.on('exit', function() {
        console.log('Worker died. Spawning a new process...');
        cluster.fork();
    });
}
else {
    var http_port = process.env.PORT || 3000;
    var ws_port = process.env.WSPORT || 3001;
    app.listen(http_port, ws_port, function() {
        console.log('Worker started, listening on HTTP:' + http_port + ' and WebSocket:' + ws_port);
    });
}