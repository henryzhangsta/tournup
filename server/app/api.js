var MongoClient = require('mongodb').MongoClient;
MongoClient.connect('mongodb://tournup:tournup@ds048487.mongolab.com:48487/tournup', function(err, db) {
    console.assert(err == null);
    console.assert(db != null);
    console.log('connection open;');
});

exports.tournament = function(req, res, next) {
    
};

exports.match = function(req, res, next) {
    
};

exports.user = function(req, res, next) {
    
};