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
