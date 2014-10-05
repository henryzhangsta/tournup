var async = require('async');
var Parse = require('../db/parse');

exports.sendResults = function(tournament, callback) {
    Parse.Push.send({
        channels: ['tournament_' + tournament._id, 'host_' + tournament._id],
        data: {
            alert: 'Tournament is complete. Results are ready!',
            tournament_id: tournament._id
        }
    }).then(function(result) {
        callback(null, result);
    }, function(error) {
        callback({
            code: 500,
            message: error
        });
    });
};

exports.sendMatchNotification = function(match, callback) {
    async.each(match.players, function(player, callb) {
        if (player == match.players[0]) {
            opponent = match.players[1];
        }
        else {
            opponent = match.players[0];
        }

        (new Parse.Query(Parse.User)).get(opponent, {
            success: function(obj) {
                var request = {
                    channels: ['user_' + player],
                    data: {
                        match: match._id,
                        alert: 'You are playing ' + obj.attributes.name + ' next!',
                        opponent: opponent
                    }
                };

                console.log(request);

                Parse.Push.send(request).then(function() {
                    callb(null);
                }, function (error) {
                    callb(error);
                });
            },
            error: function(err) {
                callb(err);
            }
        });
    }, function(error) {
        if (error) {
            cb({
                code: 500,
                message: error
            });
        }
        else {
            callback(null, match._id);
        }
    });
};
