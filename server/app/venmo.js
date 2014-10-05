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
            code: req.query.code,
            client_id: '2003',
            client_secret: 'SECRET'
        };
        request({
            url: 'https://api.venmo.com/v1/oauth/access_token',
            method: 'POST',
            form: data
        }, function(error, response, body) {
            body = JSON.parse(body);
            body._id = body.user.id;
            req.mongo.collection('venmo').save(body, function(error, result) {
                if (error) {
                    res.status(500);
                    res.send(JSON.stringify({message: 'Unable to save OAuth token.'}));
                    res.end();
                }
                else {
                    res.status(200);
                    res.send(JSON.stringify({venmo_id: body._id}));
                    res.end();
                }
            });
            console.log(body);
        });
    }

    res.end();
}
