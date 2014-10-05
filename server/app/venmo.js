var request = require('request');

exports.webhook = function(req, res, next) {
    if (req.query.venmo_challenge) {
        res.status(200);
        res.send(req.query.venmo_challenge);
        res.end();
        return;
    }

    console.log(req.body);
    res.status(200);
    res.end();
}

exports.oauth = function(req, res, next) {
    if (req.query.code) {
        data = {
            code: req.query.code;
            client_id: '2003';
            client_secret: 'SECRET';
        };
        request({
            url: 'https://api.venmo.com/v1/oauth/access_token',
            method: 'POST',
            form: data
        }, function(error, response, body) {
            console.log(body);
        });
    }

    res.end();
}
