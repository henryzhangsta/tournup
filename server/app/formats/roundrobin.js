
exports.start = function(tournament, db) {
    matches = [];
    for (var i = 0; i < tournament.contestants.length; ++i) {
        for (var j = i + 1; j < tournament.contestants.length; ++j) {
            matches.push({
                players: [tournament.contestants[i].id, tournament.contestants[j].id],
                state: 'pending',
                result: null,
                winner: null
            });
        }
    }

    contestants = {};
    for (var i = 0; i < tournament.contestants.length; ++i) {
        contestants[tournament.contestants[i].id] = true;
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

exports.matchEnd = function(match, db) {

}