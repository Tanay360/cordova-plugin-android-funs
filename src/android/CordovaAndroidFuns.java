package com.hiddendev;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;

public class CordovaAndroidFuns extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("store")) {
            this.store(args.getString(1), args.getString(0), callbackContext);
            return true;
        } else if (action.equals("read")) {
            this.read(args.getString(0), callbackContext);
            return true;
        } else if (action.equals("delete")) {
            this.deleteFile(args.getString(0), callbackContext);
            return true;
        } else if (action.equals("append")) {
            this.appendText(args.getString(0), args.getString(1), callbackContext);
            return true;
        } else if (action.equals("storePS")) {
            this.storePreference(args.getString(0), args.getString(1), callbackContext);
            return true;
        } else if (action.equals("storePB")) {
            this.storePreference(args.getString(0), args.getBoolean(1), callbackContext);
            return true;
        } else if (action.equals("storePI")) {
            this.storePreference(args.getString(0), args.getInt(1), callbackContext);
            return true;
        } else if (action.equals("storePF")) {
            this.storePreference(args.getString(0), (float) args.getDouble(1), callbackContext);
            return true;
        } else if (action.equals("storePL")) {
            this.storePreference(args.getString(0), args.getLong(1), callbackContext);
            return true;
        } else if (action.equals("getPref")) {
            this.getPreference(args.getString(0), args.getString(1), callbackContext);
            return true;
        } else if (action.equals("showShortToast")) {
            this.showShortToast(args.getString(0), callbackContext);
            return true;
        } else if (action.equals("showLongToast")) {
            this.showLongToast(args.getString(0), callbackContext);
            return true;
        }
        return false;
    }

    private void store(String textString, String fileName, CallbackContext callbackContext) {
        final CordovaInterface _cordova = cordova;

        cordova.getThreadPool().execute(new Runnable() {
            final CordovaInterface cordova = _cordova;

            @Override
            public void run() {
                try {
                    File dir = new File(cordova.getContext().getFilesDir(), "mydir");
                    if (!dir.exists()) {
                        dir.mkdir();
                    }

                    File gpxfile = new File(dir, fileName);
                    FileWriter writer = new FileWriter(gpxfile);
                    writer.write("");
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

    private void read(String fileName, CallbackContext callbackContext) {
        final CordovaInterface _cordova = cordova;

        cordova.getThreadPool().execute(new Runnable() {
            final CordovaInterface cordova = _cordova;

            @Override
            public void run() {
                try {
                    File dir = new File(cordova.getContext().getFilesDir(), "mydir");
                    if (!dir.exists()) {
                        dir.mkdir();
                    }

                    File gpxfile = new File(dir, fileName);

                    if (!gpxfile.exists()) {
                        callbackContext.error("File does not exist");
                        return;
                    }
                    StringBuilder sb = new StringBuilder();
                    Scanner sc = new Scanner(gpxfile);
                    while (sc.hasNextLine()) {
                        sb.append(sc.nextLine());
                        sb.append("\n");
                    }
                    callbackContext.success(sb.toString().trim());
                } catch (Exception e) {
                    e.printStackTrace();
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    private void deleteFile(String fileName, CallbackContext callbackContext) {
        final CordovaInterface _cordova = cordova;

        cordova.getThreadPool().execute(new Runnable() {
            final CordovaInterface cordova = _cordova;

            @Override
            public void run() {
                try {
                    File dir = new File(cordova.getContext().getFilesDir(), "mydir");
                    if (!dir.exists()) {
                        dir.mkdir();
                    }

                    File gpxfile = new File(dir, fileName);

                    if (!gpxfile.exists()) {
                        callbackContext.error("File does not exist");
                        return;
                    }
                    if (gpxfile.delete()) {
                        callbackContext.success();
                    } else {
                        callbackContext.error("File not deleted!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    private void appendText(String fileName, String textString, CallbackContext callbackContext) {
        final CordovaInterface _cordova = cordova;

        cordova.getThreadPool().execute(new Runnable() {
            final CordovaInterface cordova = _cordova;

            @Override
            public void run() {
                try {
                    File dir = new File(cordova.getContext().getFilesDir(), "mydir");
                    if (!dir.exists()) {
                        dir.mkdir();
                    }

                    File gpxfile = new File(dir, fileName);

                    if (!gpxfile.exists()) {
                        callbackContext.error("File does not exist");
                        return;
                    }
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

    private void storePreference(final String key, final String value, CallbackContext callbackContext){
        final CordovaInterface _cordova = cordova;

        cordova.getThreadPool().execute(new Runnable() {
            final CordovaInterface cordova = _cordova;

            @Override
            public void run() {
                try {
                    SharedPreferences sharedPreferences = cordova.getContext().getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(key, value);
                    editor.apply();
                    callbackContext.success();
                } catch (Exception e) {
                    e.printStackTrace();
                    callbackContext.error(e.getMessage());
                }

            }

        });
    }

    private void storePreference(final String key, final boolean value, CallbackContext callbackContext){
        final CordovaInterface _cordova = cordova;

        cordova.getThreadPool().execute(new Runnable() {
            final CordovaInterface cordova = _cordova;

            @Override
            public void run() {
                try {
                    SharedPreferences sharedPreferences = cordova.getContext().getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(key, value);
                    editor.apply();
                    callbackContext.success();
                } catch (Exception e) {
                    e.printStackTrace();
                    callbackContext.error(e.getMessage());
                }

            }

        });
    }

    private void storePreference(final String key, final int value, CallbackContext callbackContext){
        final CordovaInterface _cordova = cordova;

        cordova.getThreadPool().execute(new Runnable() {
            final CordovaInterface cordova = _cordova;

            @Override
            public void run() {
                try {
                    SharedPreferences sharedPreferences = cordova.getContext().getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(key, value);
                    editor.apply();
                    callbackContext.success();
                } catch (Exception e) {
                    e.printStackTrace();
                    callbackContext.error(e.getMessage());
                }

            }

        });
    }

    private void storePreference(final String key, final float value, CallbackContext callbackContext){
        final CordovaInterface _cordova = cordova;

        cordova.getThreadPool().execute(new Runnable() {
            final CordovaInterface cordova = _cordova;

            @Override
            public void run() {
                try {
                    SharedPreferences sharedPreferences = cordova.getContext().getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putFloat(key, value);
                    editor.apply();
                    callbackContext.success();
                } catch (Exception e) {
                    e.printStackTrace();
                    callbackContext.error(e.getMessage());
                }

            }

        });
    }

    private void storePreference(final String key, final long value, CallbackContext callbackContext){
        final CordovaInterface _cordova = cordova;

        cordova.getThreadPool().execute(new Runnable() {
            final CordovaInterface cordova = _cordova;

            @Override
            public void run() {
                try {
                    SharedPreferences sharedPreferences = cordova.getContext().getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putLong(key, value);
                    editor.apply();
                    callbackContext.success();
                } catch (Exception e) {
                    e.printStackTrace();
                    callbackContext.error(e.getMessage());
                }

            }

        });
    }

    private void getPreference(String key, String type, CallbackContext callbackContext) {
        final CordovaInterface _cordova = cordova;

        cordova.getThreadPool().execute(new Runnable() {
            final CordovaInterface cordova = _cordova;

            @Override
            public void run() {
                try {
                    SharedPreferences sharedPreferences = cordova.getContext().getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
                    if ("int".equalsIgnoreCase(type)) {
                        callbackContext.success(Integer.toString(sharedPreferences.getInt(key, -1)));
                    } else if ("long".equalsIgnoreCase(type)) {
                        callbackContext.success(Long.toString(sharedPreferences.getLong(key, -1)));
                    } else if ("float".equalsIgnoreCase(type)) {
                        callbackContext.success(Float.toString(sharedPreferences.getFloat(key, -1f)));
                    } else if ("string".equalsIgnoreCase(type)) {
                        callbackContext.success(sharedPreferences.getString(key, "not found"));
                    } else if ("boolean".equalsIgnoreCase(type)){
                        callbackContext.success(Boolean.toString(sharedPreferences.getBoolean(key, false)));
                    } else {
                        callbackContext.error(type + " type does not exists");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callbackContext.error(e.getMessage());
                }

            }

        });
    }

    private void showShortToast(String message, CallbackContext callbackContext) {
        final CordovaInterface _cordova = cordova;

        cordova.getThreadPool().execute(new Runnable() {
            final CordovaInterface cordova = _cordova;

            @Override
            public void run() {
                try {
                    cordova.getActivity().runOnUiThread(() -> {
                        Toast.makeText(cordova.getContext(), message, Toast.LENGTH_SHORT).show();
                    });
                    callbackContext.success();
                } catch (Exception e) {
                    e.printStackTrace();
                    callbackContext.error(e.getMessage());
                }

            }

        });
    }

    private void showLongToast(String message, CallbackContext callbackContext) {
        final CordovaInterface _cordova = cordova;

        cordova.getThreadPool().execute(new Runnable() {
            final CordovaInterface cordova = _cordova;

            @Override
            public void run() {
                try {
                    cordova.getActivity().runOnUiThread(() -> {
                        Toast.makeText(cordova.getContext(), message, Toast.LENGTH_LONG).show();
                    });
                    callbackContext.success();
                } catch (Exception e) {
                    e.printStackTrace();
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    private static final String PREFS_KEY = "CordovaAndroidFunsKey";
}
