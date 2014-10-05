var WebSocketServer = require('ws').Server

function send(ws, object) {
    ws.send(JSON.stringify(object));
}

exports.listen = function(port){
    server = new WebSocketServer({port: port});
    server.broadcast = function(data) {
        for (var i in this.clients) {
            this.clients[i].send(data);
        }
    }
    exports.broadcast = server.broadcast;
    // Implement WebSocket Requests.
    server.on('connection', function(ws) {
        ws.on('message', function(message) {
            console.log(this);
            console.log(message);
        });
        send(ws, {
            message: 'Hello!'
        });
    });
};
