var Constants = require('../constants');
var Parse = require('parse').Parse;
Parse.initialize(Constants.PARSE_APP_ID, Constants.PARSE_JS_KEY);

module.exports = Parse;
