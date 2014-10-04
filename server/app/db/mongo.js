var Promise = require('promise');
var Constants = require('../constants');
var MongoClient = require('mongodb').MongoClient;
var db;

function dbConnect(resolve, reject) {
    if (!db) {
        MongoClient.connect(Constants.MONGO_DATABASE_URL, function(err, mongo) {
            if (!err && mongo) {
                db = mongo;
                resolve(db);
            }
            else {
                reject(err);
            }
        });
    }
    else {
        console.log('use cached');
        resolve(db);
    }
}

module.exports = function() {
    return new Promise(dbConnect);
}
