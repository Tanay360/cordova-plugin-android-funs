package com.heartade;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 * This class echoes a string called from JavaScript.
 */
public class CordovaAndroidFuns extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("store")) {
            //String message = args.getString(0);
            this.store(args.getString(0), args.getString(1), callbackContext);
            return true;
        }
        return false;
    }

    private void store(String textString, String fileName, CallbackContext callbackContext) {
        // fileDir ex. /Pictures
        // fileName ex. capture.jpg
        final CordovaInterface _cordova = cordova;

        cordova.getThreadPool().execute(new Runnable() {
            CordovaInterface cordova = _cordova;
            @Override
            public void run() {
                try {
                    File dir = new File(cordova.getContext().getFilesDir(), "mydir");
					if(!dir.exists()){
						dir.mkdir();
					}

					File gpxfile = new File(dir, fileName);
					FileWriter writer = new FileWriter(gpxfile);
					writer.append(textString);
					writer.flush();
					writer.close();
                    callbackContext.success();
                } catch (Exception e) {
                    e.printStackTrace();
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }
}
