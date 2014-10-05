var express = require('express');
var http = express();
var compression = require('compression');
var bodyparser = require('body-parser');

var api = require('./api');
var venmo = require('./venmo');

var API_DIR = '/api';

function registerApiCall(path, callback) {
    http.use(API_DIR + path, callback);
};

http.use(compression());
http.use(function(req, res, next) {
    require('./db/mongo')().then(function(db){
        req.mongo = db;
        next();
    }, function(err) {
        res.status(500);
        res.send(JSON.stringify({message: 'Database connection failure.'}));
        res.end();
    });
});

registerApiCall('', bodyparser.json());
registerApiCall('/tournament/:id?/:property?', api.tournament);
registerApiCall('/match/:id?/:property?', api.match);
registerApiCall('/user/:id?/:property?', api.user);
registerApiCall('/payment/:id?', api.payment);
registerApiCall('/venmo/webhook', venmo.webhook);

http.use(function(req, res, next) {
    res.status(400);
    res.send('API endpoint not found.');
    res.end();
});

module.exports = http;
