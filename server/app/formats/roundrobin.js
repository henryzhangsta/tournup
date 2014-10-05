var async = require('async');
var Parse = require('../db/parse');

function generatePairing(round, tournament, state) {
    state = typeof state !== 'undefined' ? state : 'playing';
    matches = [];

    var contestants = tournament.contestants.slice();
    var row1 = contestants.slice(0, contestants.length / 2);
    var row2 = contestants.slice(contestants.length / 2);
    
    if (row2.length != row1.length) {
        row2.push(null);
    }

    if (row1.length > 1) {
        for (var i = 1; i < round; ++i) {
            row1.splice(1, 0, row2.splice(0, 1)[0]);
            row2.push(row1.splice(row1.length - 1, 1)[0]);
        }
    }

    for (var i = 0; i < row1.length; i++) {
        match = {
            tournament_id: tournament._id,
            state: state,
            result: null,
            winner: null
        };

        if (!row1[i] || !row2[i]) {
            match.players = [(row1[i] ? row1[i].id : row2[i].id)];
            match.result = 'win';
            match.winner = players[0].id;
            match.state = 'finished';
        }
        else {
            match.players = [row1[i].id, row2[i].id];
        }

        matches.push(match);
    }

    return matches;
}

// Code to start and work on tournament flow.
exports.start = function(tournament, db, cb) {
    if (tournament.contestants.length <= 1) {
        cb({
            code: 400,
            message: 'Cannot start a tournament in this format with less than 2 contestants.'
        });
        return;
    }

    tournament.state = 'started';
    tournament.matches = {};
    tournament.matches.playing = [];
    tournament.matches.finished = [];
    tournament.num_rounds = tournament.contestants.length - (tournament.contestants.length % 2 == 1 ? 0 : 1);
    tournament.round = 1;

    db.collection('tournaments').save(tournament, function(err, reuslt) {
        if (err) {
            cb({
                code: 500,
                message: 'Cannot start this tournament.'
            });
        }
        else {
            exports.roundStart(tournament, db, cb);
        }
    });
}

exports.matchStart = function(tournament, match, db, cb) {

}

exports.matchEnd = function(tournament, match, db, cb) {
    tournament.matches.finished.push(match._id);
    tournament.matches.playing = tournament.matches.playing.filter(function (item) {
        return !item.equals(match._id);
    });

    if (match.result == 'draw') {
        for (var i in tournament.contestants) {
            if (tournament.contestants[i].id == match.players[0] || tournament.contestants[i].id == match.players[1]) {
                tournament.contestants[i].scoring.score += 1;
                tournament.contestants[i].scoring.draws += 1;
            }
        }
    }
    else {
        var loser = match.players.filter(function(item) {return item != match.winner})[0];
        for (var i in tournament.contestants) {
            if (tournament.contestants[i].id == match.winner) {
                tournament.contestants[i].scoring.score += 3;
                tournament.contestants[i].scoring.wins += 1;
            }
            else if (tournament.contestants[i].id == loser) {
                tournament.contestants[i].scoring.losses += 1;
            }
        }
    }

    db.collection('matches').save(match, function(err, result) {
        if(err) {
            cb({
                code: 500,
                message: err
            });
        }
        else {
            if (tournament.matches.playing.length == 0) {
                exports.roundEnd(tournament, db, cb);
            }
            else {
                db.collection('tournaments').save(tournament, function(err, result) {
                    if (err) {
                        cb({
                            code: 500,
                            message: err
                        });
                    }
                    else {
                        cb(null, result);
                    }
                });
            }
        }
    })   
}

exports.roundStart = function(tournament, db, cb) {
    var matches = generatePairing(tournament.round, tournament);
    tournament.matches.playing = matches;
    async.map(matches, function(item, callback) {
        db.collection('matches').insert(item, function(err, result) {
            if (err) {
                callback(err);
            }
            else {
                if (item.players.length > 1) {
                    async.each(item.players, function(player, callb) {
                        var request = {
                            channels: ['user_' + player],
                            data: {
                                match: item._id
                            }
                        };
                        console.log(request);
                        if (player == item.players[0]) {
                            request.data.alert = 'You are playing' + item.players[1] + ' next!';
                            request.data.opponent = item.players[1];
                        }
                        else {
                            request.data.alert = 'You are playing' + item.players[0] + ' next!';
                            request.data.opponent = item.players[0];
                        }
                        Parse.Push.send(request).then(function() {
                            callb(null);
                        }, function (error) {
                            callb(error);
                        });
                    }, function(error) {
                        if (err) {
                            cb({
                                code: 500,
                                message: err
                            });
                        }   
                        else {
                            callback(null, item._id);
                        }
                    });
                }
                else {
                    tournament.matches.finished.push(item._id);
                    callback(null, null);
                }
            }
        });
    }, function(err, results) {
        if (err) {
            cb({
                code: 500,
                message: err
            });
        }
        else {
            tournament.matches.playing = results.filter(function(item) {return item != null;});

            db.collection('tournaments').save(tournament, function(err, result) {
                if (err) {
                    cb({
                        code: 500,
                        message: err
                    });
                }   
                else {
                    cb();
                }
            });
        }
    });
}

exports.roundEnd = function(tournament, db, cb) {
    if (tournament.matches.playing.length > 0) {
        cb({
            code: 500,
            message: 'Round is not over. Cannot end round.'
        })
    }

    if (tournament.round == tournament.num_rounds) {
        exports.end(tournament, db, cb);
    }
    else {
        tournament.round += 1;
        exports.roundStart(tournament, db, cb);
    }
}

exports.end = function(tournament, db, cb) {
    tournament.state = 'ended';
    db.collection('tournaments').save(tournament, function(err, result) {
        if (err) {
            cb({
                code: 500,
                message: err
            });
        }   
        else {
            Parse.Push.send({
                channels: ['tournament_' + tournament._id],
                data: {
                    alert: 'Tournament is complete. Results are ready!',
                    tournament_id: tournament._id
                }
            }).then(function(result) {
                cb(null, result);
            }, function(error) {
                cb({
                    code: 500,
                    message: error
                });
            });
        }
    });
}

// Tournament result reporting
