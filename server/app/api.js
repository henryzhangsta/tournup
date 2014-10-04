function raiseInvalidParametersException(res, message) {
    res.status(400);
    res.send(JSON.stringify({
        message: message
    }));
    res.end();
}

function raiseMongoDbError(res, message) {
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
                }
                else
                {
                    // Create a new tournament.
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
                raiseMongoDbError(res, err);
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