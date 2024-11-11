var exec = require('cordova/exec');

var AndroidWidget = {
    updateWidget: function(text, successCallback, errorCallback) {
        exec(successCallback, errorCallback, "AndroidWidget", "updateWidget", [text]);
    },
    listenForButtonClicks: function(successCallback, errorCallback) {
        exec(successCallback, errorCallback, "AndroidWidget", "listenForButtonClicks", []);
    }
};

module.exports = AndroidWidget;
