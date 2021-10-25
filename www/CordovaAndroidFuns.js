var exec = require('cordova/exec');

exports.store = function (fileName, textString, success, error) {
    exec(success, error, 
        'CordovaAndroidFuns', 
        'store', 
        [fileName, textString]);
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

exports.storeImage = function(baseString, fileDir, fileName, success, error){
	exec(success, error,
		'CordovaAndroidFuns',
		'storeImage',
		[baseString, fileDir, fileName]);
};

exports.storeDocument = function(baseString, mimeType, fileDir, fileName, success, error){
	exec(success, error,
		'CordovaAndroidFuns',
		'storeDocument',
		[baseString, mimeType, fileDir, fileName]);
};

exports.storeAudio = function(baseString, mimeType, fileDir, fileName, success, error){
	exec(success, error,
		'CordovaAndroidFuns',
		'storeAudio',
		[baseString, mimeType, fileDir, fileName]);
};

exports.storeVideo = function(baseString, mimeType, fileDir, fileName, success, error){
	exec(success, error,
		'CordovaAndroidFuns',
		'storeVideo',
		[baseString, mimeType, fileDir, fileName]);
};

exports.getDataFromFile = function(mimeType, success, error){
	exec(success, error,
		'CordovaAndroidFuns',
		'getDataFromFile',
		[mimeType]);
};

exports.getDeviceVersion = function(success, error){
	exec(success, error,
		'CordovaAndroidFuns',
		'getDeviceVersion',
		[]);
};


exports.createImageDialog = function(filePath, alertText, success, error) {
    exec(success, error,
        'CordovaAndroidFuns',
        'createImageDialog',
        [filePath, alertText]);  
};

exports.simpleGet = function(url, success, error){
	exec(success, error,
		'CordovaAndroidFuns',
		'simpleGet',
		[url]);
};

exports.simpleJsonPost = function(url, jsonString, success, error){
	exec(success, error,
		'CordovaAndroidFuns',
		'simpleJsonPost',
		[url, jsonString]);
};