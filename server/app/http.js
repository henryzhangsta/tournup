var express = require('express');
var http = express();
var compression = require('compression');

var api = require('./api');

var API_DIR = '/api';

function registerApiCall(path, callback) {
    http.use(API_DIR + path, callback);
};

http.use(compression());

registerApiCall('/tournament/:id?/:property?', api.tournament);
registerApiCall('/match/:id?/:property?', api.match);
registerApiCall('/user/:id?/:property?', api.user);

http.use(function(req, res, next) {
    res.status(400);
    res.send('API endpoint not found.');
    res.end();
});

module.exports = http;
