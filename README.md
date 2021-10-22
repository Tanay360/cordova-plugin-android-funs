# cordova-plugin-android-funs
This simple Cordova plugin allows a lot of functionality, namely, storing and reading files from external and internal storage, saving settings, creating toast messages, and getting the sdk version of the android phone.

Writing files to external storage works good for api level 29 and above.


## Examples
1. To write a text file to internal storage:

```javascript

cordova.plugins.CordovaAndroidFuns.store('hello.txt', 'Hello world', 
	function (success){
		// It worked
	},
	
	function (error){
		// Did not work
	}
);

```

2. To read a file from internal storage:

```javascript

cordova.plugins.CordovaAndroidFuns.read('hello.txt', 
	function (response){
		// Success
		console.log(response);
	},
	
	function (error){
		console.log(error);
	}
);

```

3. To delete a file from internal storage:

```javascript

cordova.plugins.CordovaAndroidFuns.deleteFile('hello.txt',
	function(success){
		// Success
	},
	
	function(error){
		// Error
	}
);

```

4. To append text to file:

```javascript

cordova.plugins.CordovaAndroidFuns.appendFile('hello.txt',
	function(success){
		// Success
	},
	
	function(error){
		//Error
	}
);

```

5. To save a string preference:

```javascript

cordova.plugins.CordovaAndroidFuns.storePS('key', 'value', 
	function(success){
		// Success
	},
	
	function(error){
		// Error
	}
);

```

6. To save a boolean preference:

```javascript

cordova.plugins.CordovaAndroidFuns.storePB('key', true, 
	function(success){
		// Success
	},
	
	function(error){
		// Error
	}
);

```

7. To save an int preference:

```javascript

cordova.plugins.CordovaAndroidFuns.storePI('key', 0, 
	function(success){
		// Success
	},
	
	function(error){
		// Error
	}
);

```

7. To save an float preference:

```javascript

cordova.plugins.CordovaAndroidFuns.storePF('key', 0.0, 
	function(success){
		// Success
	},
	
	function(error){
		// Error
	}
);

```

8. To save an long preference:

```javascript

cordova.plugins.CordovaAndroidFuns.storePL('key', 0, 
	function(success){
		// Success
	},
	
	function(error){
		// Error
	}
);

```

9. To get a preference:

```javascript

cordova.plugins.CordovaAndroidFuns.getPref('key', 'type', // type can be int, float, boolean, long, or String
	function(response){
		// Success
		console.log(response);
	},
	
	function(error){
		// Error
		console.log(error);
	}
);

```

10. To show a short toast:

```javascript

cordova.plugins.CordovaAndroidFuns.showShortToast(message);

```

11. To show a long toast:

```javascript

cordova.plugins.CordovaAndroidFuns.showLongToast(message);

```

12. To store an image to external storage:

```javascript

cordova.plugins.CordovaAndroidFuns.storeImage(base64string, 'Pictures', `${fileName}.png`,
	function(success){
		// Success
	},
	
	function(error){
		// Error
	}
);

```

13. To store a document to external storage:

```javascript

cordova.plugins.CordovaAndroidFuns.storeDocument(fileContent, mimeType, 'Download', fileName,
	function(success){
		// Success
	},
	
	function(error){
		// Error
	}
);

```

14. To store an audio to external storage:

```javascript

cordova.plugins.CordovaAndroidFuns.storeAudio(base64string, 'audio/mp3', 'Music', 'hello.mp3',
	function(success){
		alert(success);
	},
	function(error){
		alert(error);
	}
);


```

15. To store an video to external storage:

```javascript

cordova.plugins.CordovaAndroidFuns.storeVideo(base64string, 'video/mp4', 'Movies', 'hello.mp4',
	function(success){
		alert(success);
	},
	function(error){
		alert(error);
	}
);


```

16. Get data from external file:

```javascript

cordova.plugins.CordovaAndroidFuns.getDataFromFile(mimeType,
	function(response){
		console.log(response);
	},
	
	function(error){
		console.log(error);
	}
);

```

17. Get sdk version of android phone:

```javascript

cordova.plugins.CordovaAndroidFuns.getDeviceVersion(
	function(deviceVersion){
		console.log(deviceVersion);
	},
	
	function(error){
		console.log(error);
	}
);

```