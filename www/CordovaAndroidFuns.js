var exec = require('cordova/exec');

exports.store = function (textString, fileName success, error) {
    exec(success, error, 
        'CordovaAndroidFuns', 
        'store', 
        [textString, fileName]);
};
