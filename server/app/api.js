var Constants = require('./constants');
var MongoClient = require('mongodb').MongoClient;
var db;
MongoClient.connect(Constants.MONGO_DATABASE_URL, function(err, db) {
    console.assert(err == null);
    console.assert(db != null);
    console.log('Mongo connection open');
    db = db;
});

function raiseInvalidParametersException(res, message) {
    res.status(400);
    res.send(JSON.stringify({
        message: message
    }));
    res.end();
}

exports.tournament = function(req, res, next) {
    switch (req.method) {
        case 'GET':
            if (!req.params.id) {
                raiseInvalidParametersException(res, 'ID field is required.');
            }
            break;
        case 'POST':
            if (req.params.id) {
                raiseInvalidParametersException(res, 'ID field is not allowed.');
            }
            break;
        case 'UPDATE':
            if (!req.params.id) {
                raiseInvalidParametersException(res, 'ID field is required.');
            }
            break;
        case 'DELETE':
            if (!req.params.id) {
                raiseInvalidParametersException(res, 'ID field is required.');
            }
            break;
    }
};

exports.match = function(req, res, next) {
    switch (req.method) {
        case 'GET':
            if (!req.params.id) {
                raiseInvalidParametersException(res, 'ID field is required.');
            }
            break;
        case 'POST':
            if (req.params.id) {
                raiseInvalidParametersException(res, 'ID field is not allowed.');
            }
            break;
        case 'UPDATE':
            if (!req.params.id) {
                raiseInvalidParametersException(res, 'ID field is required.');
            }
            break;
        case 'DELETE':
            if (!req.params.id) {
                raiseInvalidParametersException(res, 'ID field is required.');
            }
            break;
    }
};

exports.user = function(req, res, next) {
    switch (req.method) {
        case 'GET':
            if (!req.params.id) {
                raiseInvalidParametersException(res, 'ID field is required.');
            }
            break;
        case 'POST':
            if (req.params.id) {
                raiseInvalidParametersException(res, 'ID field is not allowed.');
            }
            break;
        case 'UPDATE':
            if (!req.params.id) {
                raiseInvalidParametersException(res, 'ID field is required.');
            }
            break;
        case 'DELETE':
            if (!req.params.id) {
                raiseInvalidParametersException(res, 'ID field is required.');
            }
            break;
    }
};