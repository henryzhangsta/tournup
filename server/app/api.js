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

function raiseCustomError(res, error) {
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

                res.status(200);
                res.send(JSON.stringify(tournament));
                res.end();
                break;
            case 'POST':
                if (req.params.id) {
                    if (!req.params.property) {
                        raiseInvalidParametersException(res, 'Action field is required.');
                    }

                    if (req.params.property == 'start') {
                        tournament.contestants = tournament.contestants.filter(function isConfirmed(contestant){
                            return contestant.status == 'confirmed';
                        });
                        require('./formats/' + tournament.format).start(tournament, req.mongo, function(err, result){
                            if (err) {
                                raiseCustomError(res, err);
                            }
                            else {
                                res.status(200);
                                res.send({result: 'Tournament start successful.'});
                                res.end();
                            }
                        });
                    }
                    else if (req.params.property == 'add') {
                        data = req.body;
                        (new Parse.Query(Parse.User)).get(data.user, {
                            success: function(obj) {
                                if (tournament.contestants.filter(function(item) { return item.id == data.user; }).length > 0) {
                                    raiseInvalidParametersException(res, 'User is already in this tournament.');
                                    return;
                                }
                                
                                user = {
                                    id: data.user,
                                    registration_time: (new Date()).toJSON(),
                                    paid: false,
                                    transaction: null,
                                    status: 'pending',
                                    scoring: {
                                        score: 0,
                                        wins: 0,
                                        draws: 0,
                                        losses: 0
                                    }
                                };
                                if (tournament.paid) {
                                    tournament.contestants.push(user);
                                }
                                else {
                                    user.status = 'confirmed';
                                    tournament.contestants.push(user);
                                }
                                req.mongo.collection('tournaments').save(tournament, function(err, result){
                                    if (err) {
                                        raiseDbError(res, err);
                                    }
                                    else {
                                        res.status(200);
                                        res.send({message: 'Successfully added user to tournament.'});
                                        res.end();
                                    }
                                });
                            },
                            error: function(err) {
                                raiseDbError(res, 'User does not exist.');
                            }
                        });
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
                                tournament.state = 'pending';
                                tournament.contestants = [];

                                req.mongo.collection('tournaments').insert(tournament, function(err, result){
                                    if (err) {
                                        raiseDbError(res, err);
                                    }
                                    else {
                                        res.status(200);
                                        res.send(result[0]._id.toHexString());
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
        if (req.params.id.length < 12) {
            raiseInvalidParametersException(res, 'Invalid ID.');
            return;
        }
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
            case 'PUT':
                if (!req.params.id) {
                    raiseInvalidParametersException(res, 'ID field is required.');
                }
                data = req.body;
                if (!data.winner || !data.result) {
                    raiseInvalidParametersException(res, 'Request is missing one or more required parameters');
                    return;
                }
                if (data.result == 'draw' || match.players.filter(function(item) {return item == winner}).length == 1) {
                    raiseInvalidParametersException(res, 'Invalid result.');
                    return;
                }
                var tournament = req.mongo.collection('tournaments').findOne(match.tournament_id, function(err, item) {
                    if (err) {
                        raiseDbError(res, 'Tournament does not exist.');
                    }
                    else {
                        match.winner = data.winner;
                        match.result = data.result;
                        match.state = 'finished';
                        require('./formats/' + tournament.format).matchEnd(match, req.mongo, function(err, result) {
                            if (err) {
                                raiseCustomError(res, err);
                            }
                            else {
                                res.status(200);
                                res.send({message: 'Result saved.'});
                                res.end();
                            }
                        });
                    }
                });

                break;
            case 'DELETE':
                if (!req.params.id) {
                    raiseInvalidParametersException(res, 'ID field is required.');
                }
                break;
        }
    }

    if (req.params.id) {
        if (req.params.id.length < 12) {
            raiseInvalidParametersException(res, 'Invalid ID.');
            return;
        }
        req.mongo.collection('matches').findOne({_id: ObjectID.createFromHexString(req.params.id)}, function(err, item) {
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

exports.payment = function(req, res, next) {
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
            data = req.body;
            if (!data.payment_id || !data.tournament_id || !data.user_id) {
                raiseInvalidParametersException(res, 'Request is missing one or more required parameters');
                return;
            }
            (new Parse.Query(Parse.User)).get(req.params.id, {
                success: function(obj) {
                    user = obj;
                    req.mongo.collection('tournaments').findOne({_id: ObjectID.createFromHexString(data.tournament_id)}, function(err, item) {
                        if (err == null && item) {
                            tournament = item;
                            req.mongo.collection('transactions').findOne({payment_id: data.payment_id}, function(err, transaction) {
                                if (err) {
                                    raiseDbError(res, err);
                                }
                                else {
                                    if (transaction && transaction.confirmed) {
                                        for (var i in tournament.contestants) {
                                            if (tournament.contestants[i].id == data.user_id) {
                                                tournament.contestants[i].paid = true;
                                                tournament.contestants[i].status = 'confirmed';
                                                tournament.contestants[i].transaction = transaction._id;
                                                
                                                req.mongo.collection('tournaments').save(tournament, function (err, result) {
                                                    if (err) {
                                                        raiseDbError(res, err);
                                                        return;
                                                    }
                                                    res.status(200);
                                                    res.send(res, JSON.stringify({
                                                        transaction_id: transaction._id,
                                                        status: 'confirmed'
                                                    }));
                                                    res.end(); 
                                                });
                                                return;
                                            }
                                        }
                                    }
                                    else if (transaction) {
                                        raiseDbError(res, 'Payment in inconsistent state. Contact support.');
                                    }
                                    else {
                                        req.mongo.collection('transactions').insert({
                                            user_id: data.user_id,
                                            payment_id: data.payment_id,
                                            tournament_id: data.tournament_id,
                                            confirmed: false
                                        }, function(err, result) {
                                            res.status(200);
                                            res.send(data.payment_id);
                                        });
                                    }
                                }
                            });
                        }
                        else if (err) {
                            raiseDbError(res, err);
                        }
                        else {
                            raiseInvalidParametersException(res, 'Tournament does not exist.');
                        }
                    });
                },
                error: function(err) {
                    raiseDbError(res, 'User does not exist.');
                }
            });
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