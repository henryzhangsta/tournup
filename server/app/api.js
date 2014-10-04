var Parse = require('./db/parse');

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
                        // Generate the bracket and start the tournament.
                    }
                    else if (req.params.property == 'add') {
                        // Add a user to a tournament
                    }
                }
                else
                {
                    try {
                        if (!tournament.format in ['roundrobin', 'single_elim', 'swiss']) {
                            throw 'Invalid format.';
                        }
                        data = req.body;
                        tournament = {};
                        tournament.owner = data.owner;
                        tournament.name = data.name;
                        tournament.format = data.format;

                    }
                    catch (e) {
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
        req.mongo.collection('tournaments').findOne({_id: req.params.id}, function(err, item) {
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
        var query = new Parse.Query.get.(req.params.id, {
            success: function(user) {
                if (user) {
                    act();
                }
                else {
                    raiseInvalidParametersException(res, 'User does not exist.');
                }
            },
            error: function(err) {
                raiseDbError(res, 'Database access error.');
            }
        });
    }
    else {
        act();
    }
};