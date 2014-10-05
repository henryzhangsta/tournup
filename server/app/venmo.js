
exports.webhook = function(req, res, next) {
    if (req.query.venmo_challenge) {
        res.send(req.query.venmo_challenge);
        res.end();
        return;
    }
    
    console.log(req.body);
}
