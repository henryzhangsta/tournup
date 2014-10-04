var async = require('async');

exports.start = function(tournament, db) {
    tournament.matches = {};
    tournament.matches.waiting = [];
    tournament.matches.playing = [];
    tournament.matches.finished = [];
    
    for (var i = 0; i < tournament.contestants.length; ++i) {
        for (var j = i + 1; j < tournament.contestants.length; ++j) {
            matches.push({
                tournament_id: tournament._id,
                players: [tournament.contestants[i]._id, tournament.contestants[j]._id],
                state: 'pending',
                result: null,
                winner: null
            });
        }
    }


    contestants = {};
    for (var i = 0; i < tournament.contestants.length; ++i) {
        contestants[tournament.contestants[i]._id] = true;
    }

    for (var i = 0; i < matches.length; ++i) {
        if (contestants[matches[i].players[0]] && contestants[matches[i].players[1]]) {
            matches[i].state = 'playing';
            delete contestants[matches[i].players[0]];
            delete contestants[matches[i].players[1]];
        }
        if (contestants.length == 0) {
            break;
        }
    }

    for (var i = 0; i < contestants.length; ++i) {
        tournament.contestants
    }
}

exports.matchStart = function(match, db) {

}

exports.matchEnd = function(match, db) {

}

exports.roundStart = function(tournament, db) {

}

exports.roundEnd = function(tournament, db) {

}

exports.end = function(tournament, db) {

}
