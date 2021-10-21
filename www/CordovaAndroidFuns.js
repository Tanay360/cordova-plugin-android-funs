var exec = require('cordova/exec');

exports.store = function (fileName, textString, success, error) {
    exec(success, error, 
        'CordovaAndroidFuns', 
        'store', 
        [textString, fileName]);
};

exports.read = function(fileName, success, error) {
	exec(success, error,
		'CordovaAndroidFuns',
		'read',
		[fileName]);
};

exports.deleteFile = function(fileName, success, error){
	exec(success, error, 
		'CordovaAndroidFuns',
		'delete'
		[fileName]);
};
		
exports.appendFile = function(fileName, textString, success, error){
	exec(success, error,
		'CordovaAndroidFuns',
		'append',
		[fileName, textString]);
};
		
exports.storePS = function(key, value, success, error){
	exec(success, error,
		'CordovaAndroidFuns',
		'storePS',
		[key, value]);
};
		
exports.storePB = function(key, value, success, error){
	exec(success, error,
		'CordovaAndroidFuns',
		'storePB',
		[key, value]);
};

exports.storePI = function(key, value, success, error){
	exec(success, error,
		'CordovaAndroidFuns',
		'storePI',
		[key, value]);
};
		
exports.storePF = function(key, value, success, error){
	exec(success, error,
		'CordovaAndroidFuns',
		'storePF',
		[key, value]);
};
		
exports.storePL = function(key, value, success, error){
	exec(success, error,
		'CordovaAndroidFuns',
		'storePL',
		[key, value]);
};
		
exports.getPref = function(key, type, success, error){
	exec(success, error,
		'CordovaAndroidFuns',
		'getPref',
		[key, type]);
};
		
exports.showShortToast = function(message, success, error){
	exec(success, error,
		'CordovaAndroidFuns',
		'showShortToast',
		[message]);
};
		
exports.showLongToast = function(message, success, error){
	exec(success, error,
		'CordovaAndroidFuns',
		'showLongToast',
		[message]);
};