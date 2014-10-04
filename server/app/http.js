var express = require('express');
var http = express();
var compression = require('compression');

var api = require('./api');

var API_DIR = '/api';
function registerApiCall(path, callback) {
    http.use(path, callback);
};

http.use(compression());

module.exports = http;
