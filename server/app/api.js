var Parse = require('./db/parse');
var ObjectID = require('mongodb').ObjectID;

function raiseInvalidParametersException(res, message) {
    res.status(400);
    res.send(JSON.stringify({
        message: message
    }));
    res.end();
}

function raiseDbError(res, message) {
    res.status(500);
    res.send(JSON.stringify({
        message: message
    }));
    res.end();
}

function raiseCustomError(error) {
    res.status(error.code);
    res.send(JSON.stringify({
        message: error.message
    }));
    res.end();
}

exports.tournament = function(req, res, next) {
    var tournament;
    function act() {
        switch (req.method) {
            case 'GET':
                if (!req.params.id) {
                    raiseInvalidParametersException(res, 'ID field is required.');
                }
                break;
            case 'POST':
                if (req.params.id) {
                    if (!req.params.property) {
                        raiseInvalidParametersException(res, 'Action field is required.');
                    }

                    if (req.params.property == 'start') {
                        require('./formats/' + tournament.format).start(tournament, req.mongo, function(err, result){
                            if (err) {
                                raiseCustomError(err);
                            }
                            else {
                                console.log(result);
                                res.status(200);
                                res.send({result: 'Tournament start successful.'}});
                                res.end();
                            }
                        });
                    }
                    else if (req.params.property == 'add') {
                        // Add a user to a tournament
                    }
                }
                else
                {
                    try {
                        data = req.body;
                        if (!data.format in ['roundrobin', 'single_elim', 'swiss']) {
                            throw 'Invalid format.';
                        }

                        (new Parse.Query(Parse.User)).get(data.owner, {
                            success: function(obj) {
                                tournament = {};
                                tournament.owner = data.owner;
                                tournament.name = data.name;
                                tournament.format = data.format;
                                tournament.time = data.time;
                                tournament.location = data.location;
                                tournament.paid = data.paid;
                                tournament.max_contestants = data.max_contestants;
                                tournament.entry_cost = data.entry_cost;
                                tournament.contestants = [];

                                req.mongo.collection('tournaments').insert(tournament, function(err, result){
                                    if (err) {
                                        raiseDbError(res, err);
                                    }
                                    else {
                                        res.status(200);
                                        res.send(result[0]._id);
                                        res.end();
                                    }
                                });
                            },
                            error: function(err) {
                                raiseDbError(res, 'Owner does not exist.');
                            }
                        });
                    }
                    catch (e) {
                        console.log(e);
                        raiseInvalidParametersException(res, e);
                    }
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

    if (req.params.id) {
        req.mongo.collection('tournaments').findOne({_id: ObjectID.createFromHexString(req.params.id)}, function(err, item) {
            if (err == null && item) {
                tournament = item;
                act();
            }
            else if (err) {
                raiseDbError(res, err);
            }
            else {
                raiseInvalidParametersException(res, 'Tournament does not exist.');
            }
        });
    }
    else {
        act();
    }
};

exports.match = function(req, res, next) {
    var match;
    function act() {
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
    }

    if (req.params.id) {
        req.mongo.collection('matches').findOne({_id: req.params.id}, function(err, item) {
            if (err == null && item) {
                match = item;
                act();
            }
            else if (err) {
                raiseDbError(res, err);
            }
            else {
                raiseInvalidParametersException(res, 'Match does not exist.');
            }
        });
    }
    else {
        act();
    }
};

exports.user = function(req, res, next) {
    var user;
    function act() {
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
    }
    
    if (req.params.id) {
        (new Parse.Query(Parse.User)).get(req.params.id, {
            success: function(obj) {
                user = obj;
                act();
            },
            error: function(err) {
                raiseDbError(res, 'User does not exist.');
            }
        });
    }
    else {
        act();
    }
};