'use client'

var exec = require('cordova/exec');

var AndroidWidget = {
    updateWidget: function(text, successCallback, errorCallback) {
        exec(successCallback, errorCallback, "AndroidWidget", "updateWidget", [text]);
    }
};

module.exports = AndroidWidget;
