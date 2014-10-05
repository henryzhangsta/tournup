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
            match.players = [(row1[i] ? row1[i] : row2[i])];
            match.result = 'win';
            match.winner = players[0];
            match.state = 'finished';
        }
        else {
            match.players = [row1[i], row2[i]];
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

exports.matchStart = function(match, db, cb) {

}

exports.matchEnd = function(match, db, cb) {

}

exports.roundStart = function(tournament, db, cb) {
    var matches = generatePairing(tournament.round, tournament);
    tournament.matches.playing = matches;
    async.each(matches, function(item, callback) {
        db.collection('matches').insert(item, function(err, result) {
            if (err) {
                callback(err);
            }
            else {
                if (item.players.length > 1) {
                    async.each(item.players, function(player, callb) {
                        var request = {
                            channels: ['user_' + player.id],
                            data: {
                                match: item._id
                            }
                        };
                        console.log(request);
                        if (player == item.players[0]) {
                            request.data.alert = 'You are playing' + item.players[1].id + ' next!';
                            request.data.opponent = item.players[1].id;
                        }
                        else {
                            request.data.alert = 'You are playing' + item.players[0].id + ' next!';
                            request.data.opponent = item.players[0].id;
                        }
                        Parse.Push.send(request).then(function() {
                            callb();
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
                            callback();
                        }
                    });
                }
                else {
                    callback();
                }
            }
        });
    }, function(err) {
        if (err) {
            cb({
                code: 500,
                message: err
            });
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
                    cb();
                }
            });
        }
    });
}

exports.roundEnd = function(tournament, db, cb) {

}

exports.end = function(tournament, db, cb) {

}

// Tournament result reporting
