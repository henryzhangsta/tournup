var async = require('async');

exports.start = function(tournament, db, cb) {
    if tournament.contestants.length <= 1 {
        cb({
            code: 400,
            message: 'Cannot start a tournament in this format with less than 2 contestants.'
        });
    }

    tournament.matches = {};
    tournament.matches.waiting = [];
    tournament.matches.playing = [];
    tournament.matches.finished = [];
    tournament.pairing_table = [tournament.contestants.length][tournament.contestants.length];
    matches = [];
    for (var i = 0; i < tournament.contestants.length; ++i) {
        for (var j = 0; j < tournament.contestants.length; ++j) {
            matches.push({
                _tmp: [i, j],
                tournament_id: tournament._id,
                players: [tournament.contestants[i]._id, tournament.contestants[j]._id],
                state: 'pending',
                result: null,
                winner: null
            });
        }
    }

    function insertMatch(item, callback) {
        _tmp = item._tmp;
        delete item._tmp
        db.collection('matches').insert(item, function(err, item) {
            if (err) {
                callback(err, null);
            }
            else {
                item = item[0];
                item._tmp = _tmp;
                callback(null, item);
            }
        });
    }
    async.map(matches, insertMatch, function(err, results){
        if (err) {
            console.log(err);
        }
        else {
            for (var i = 0; i < results.length; ++i) {
                item = results[i];
                tournament.pairing_table[item._tmp[0]][item._tmp[1]] = item._id;
                tournament.matches.waiting.push(item._id);
            }
            tournament.num_rounds = tournament.contestants.length - (tournament.contestants.length % 2 == 1 ? 0 : 1);
            tournament.round = 1;

            db.collection('tournaments').save(tournament, cb);
        }
    });
}

exports.matchStart = function(match, db, cb) {

}

exports.matchEnd = function(match, db, cb) {

}

exports.roundStart = function(tournament, db, cb) {

}

exports.roundEnd = function(tournament, db, cb) {

}

exports.end = function(tournament, db, cb) {

}
