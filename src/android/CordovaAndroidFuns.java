package com.hiddendev;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class CordovaAndroidFuns extends CordovaPlugin {

    private static final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final int WRITE_CODE = 0;
    private static final String TAG = "CordovaAndroidFuns";
    private final int SELECT_PHOTO_CODE = 1;

    @SuppressWarnings("IfCanBeSwitch")
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("store")) {
            this.store(args.getString(1), args.getString(0), callbackContext);
        } else if (action.equals("read")) {
            this.read(args.getString(0), callbackContext);
        } else if (action.equals("delete")) {
            this.deleteFile(args.getString(0), callbackContext);
        } else if (action.equals("append")) {
            this.appendText(args.getString(0), args.getString(1), callbackContext);
        } else if (action.equals("storePS")) {
            this.storePreference(args.getString(0), args.getString(1), callbackContext);
        } else if (action.equals("storePB")) {
            this.storePreference(args.getString(0), args.getBoolean(1), callbackContext);
        } else if (action.equals("storePI")) {
            this.storePreference(args.getString(0), args.getInt(1), callbackContext);
        } else if (action.equals("storePF")) {
            this.storePreference(args.getString(0), (float) args.getDouble(1), callbackContext);
        } else if (action.equals("storePL")) {
            this.storePreference(args.getString(0), args.getLong(1), callbackContext);
        } else if (action.equals("getPref")) {
            this.getPreference(args.getString(0), args.getString(1), callbackContext);
        } else if (action.equals("showShortToast")) {
            this.showShortToast(args.getString(0), callbackContext);
        } else if (action.equals("showLongToast")) {
            this.showLongToast(args.getString(0), callbackContext);
        } else if (action.equals("storeImage")) {
            this.storeImage(args.getString(0), args.getString(1), args.getString(2), callbackContext);
        } else if (action.equals("storeDocument")) {
            this.storeDocument(args.getString(0), args.getString(1), args.getString(2), args.getString(3), callbackContext);
        } else if (action.equals("storeAudio")) {
            this.storeAudio(args.getString(0), args.getString(1), args.getString(2), args.getString(3), callbackContext);
        } else if (action.equals("storeVideo")) {
            this.storeVideo(args.getString(0), args.getString(1), args.getString(2), args.getString(3), callbackContext);
        } else if (action.equals("getDataFromFile")) {
            this.getDataFromFile(args.getString(0), callbackContext);
        } else if (action.equals("getDeviceVersion")) {
            this.getDeviceVersion(callbackContext);
        } else if (action.equals("createImageDialog")) {
            this.showDialogImage(args.getString(0), args.getString(1), callbackContext);
        } else if (action.equals("simpleGet")) {
            this.makeSimpleNetworkGetRequest(args.getString(0), callbackContext);
        } else if (action.equals("simpleJsonPost")) {
            this.makeSimpleNetworkPostRequest(args.getString(0), args.getString(0), callbackContext);
        } else {
            return false;
        }
        return true;
    }

    private void makeSimpleNetworkPostRequest(String stringUrl, String body, CallbackContext callbackContext) {
        cordova.getThreadPool().execute(() -> {
            try {
                URL url = new URL(stringUrl);
                URLConnection con = url.openConnection();
                HttpURLConnection http = (HttpURLConnection) con;
                http.setRequestMethod("POST");
                http.setDoOutput(true);
                byte[] out = body.getBytes(StandardCharsets.UTF_8);
                int length = out.length;

                http.setFixedLengthStreamingMode(length);
                http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                http.connect();
                try (OutputStream os = http.getOutputStream()) {
                    os.write(out);
                }
                StringBuilder result = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(http.getInputStream()))) {
                    for (String line; (line = reader.readLine()) != null; ) {
                        result.append(line);
                    }
                }
                callbackContext.success(result.toString());

            } catch (Exception e) {
                e.printStackTrace();
                callbackContext.error(e.getMessage());
            }
        });

    }

    private void makeSimpleNetworkGetRequest(String stringUrl, CallbackContext callbackContext) {

        cordova.getThreadPool().execute(() -> {
            try {

                StringBuilder result = new StringBuilder();
                URL url = new URL(stringUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()))) {
                    for (String line; (line = reader.readLine()) != null; ) {
                        result.append(line);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callbackContext.error(e.getMessage());
                    return;
                }
                callbackContext.success(result.toString());
            } catch (Exception e) {
                e.printStackTrace();
                callbackContext.error(e.getMessage());
            }
        });
    }

    private void showDialogImage(String filePath, String alertText, CallbackContext callbackContext) {
        final CordovaInterface _cordova = cordova;

        cordova.getThreadPool().execute(new Runnable() {
            final CordovaInterface cordova = _cordova;

            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                try {
                    AssetManager am = cordova.getContext().getAssets();
                    InputStream image = am.open("www/" + filePath);
                    Bitmap bitmap = BitmapFactory.decodeStream(image);
                    cordova.getActivity().runOnUiThread(() -> {
                        LinearLayout ll = new LinearLayout(cordova.getContext());
                        ll.setOrientation(LinearLayout.VERTICAL);
                        final float scale = cordova.getContext().getResources().getDisplayMetrics().density;
                        int padding = (int) (16 * scale + 0.5f);
                        ll.setPadding(padding, padding, padding, padding);
                        int dps = 200;
                        int pixels = (int) (dps * scale + 0.5f);
                        ImageView imageView = new ImageView(cordova.getContext());
                        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(pixels, pixels);
                        imageParams.gravity = Gravity.CENTER_HORIZONTAL;
                        imageParams.weight = 1.0f;
                        imageView.setLayoutParams(imageParams);
                        imageView.setImageBitmap(bitmap);
                        ll.addView(imageView);
                        TextView textView1 = new TextView(cordova.getContext());
                        textView1.setText(alertText);
                        textView1.setTextColor(Color.WHITE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            textView1.setTextAppearance(android.R.style.TextAppearance_Large);
                        } else {
                            textView1.setTextSize(20);
                        }
                        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        int margin = (int) (16 * scale + 0.5f);
                        textParams.setMargins(margin, margin, margin, 0);
                        textView1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        textView1.setLayoutParams(textParams);
                        ll.addView(textView1);
                        Button okBtn = new Button(cordova.getContext());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            okBtn.setTextAppearance(android.R.style.TextAppearance_Large);
                        } else {
                            okBtn.setTextSize(20);
                        }
                        okBtn.setText("OK");
                        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        buttonParams.gravity = Gravity.END;
                        buttonParams.weight = 1.0f;
                        okBtn.setLayoutParams(buttonParams);
                        ll.addView(okBtn);
                        AlertDialog.Builder builder = new AlertDialog.Builder(cordova.getContext());
                        builder.setView(ll);
                        AlertDialog dialog = builder.create();
                        okBtn.setOnClickListener(v1 -> dialog.cancel());
                        dialog.show();
                        callbackContext.success();
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    callbackContext.error(e.getMessage());
                }
            }
        });
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
                    if (e.getMessage() == null) {
                        callbackContext.error("Some error occurred");
                    } else {
                        callbackContext.error(e.getMessage());
                    }
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

    private void storePreference(final String key, final String value, CallbackContext callbackContext) {
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

    private void storePreference(final String key, final boolean value, CallbackContext callbackContext) {
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

    private void storePreference(final String key, final int value, CallbackContext callbackContext) {
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

    private void storePreference(final String key, final float value, CallbackContext callbackContext) {
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

    private void storePreference(final String key, final long value, CallbackContext callbackContext) {
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
                    } else if ("boolean".equalsIgnoreCase(type)) {
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
                    cordova.getActivity().runOnUiThread(() -> Toast.makeText(cordova.getContext(), message, Toast.LENGTH_SHORT).show());
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
                    cordova.getActivity().runOnUiThread(() -> Toast.makeText(cordova.getContext(), message, Toast.LENGTH_LONG).show());
                    callbackContext.success();
                } catch (Exception e) {
                    e.printStackTrace();
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }


    private void storeAudio(String bytes, String mimeType, String fileDir, String fileName, CallbackContext callbackContext) {
        final CordovaInterface _cordova = cordova;

        cordova.getThreadPool().execute(new Runnable() {
            final CordovaInterface cordova = _cordova;

            @Override
            public void run() {
                try {
                    byte[] byteArray = Base64.decode(bytes, Base64.DEFAULT);
                    Context context = this.cordova.getActivity();
                    ContentResolver contentResolver = context.getContentResolver();
                    if (Build.VERSION.SDK_INT >= 29) {
                        final ContentValues contentValues = new ContentValues();
                        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);
                        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, fileDir);
                        contentValues.put(MediaStore.MediaColumns.IS_PENDING, 1);

                        Uri contentUri = contentResolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, contentValues);

                        OutputStream out = contentResolver.openOutputStream(contentUri);
                        out.write(byteArray);
                        out.flush();
                        out.close();

                        contentValues.clear();
                        contentValues.put(MediaStore.Audio.Media.IS_PENDING, 0);
                        contentResolver.update(contentUri, contentValues, null, null);
                        callbackContext.success(contentUri.getPath());
                    } else {
                        if (cordova.hasPermission(WRITE_EXTERNAL_STORAGE)) {
                            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
                            File dir = new File(path + "/" + fileDir);
                            dir.mkdirs();
                            File file = new File(dir, fileName);
                            FileOutputStream out = new FileOutputStream(file);
                            out.write(byteArray);
                            out.flush();
                            out.close();
                            Uri contentUri = Uri.fromFile(file);
                            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, contentUri));
                            callbackContext.success(file.getAbsolutePath());
                        } else {
                            getWritePermission(WRITE_CODE);
                        }

                    }
                } catch (RuntimeException | IOException e) {
                    e.printStackTrace();
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    private void storeVideo(String bytes, String mimeType, String fileDir, String fileName, CallbackContext callbackContext) {
        final CordovaInterface _cordova = cordova;

        cordova.getThreadPool().execute(new Runnable() {
            final CordovaInterface cordova = _cordova;

            @Override
            public void run() {
                try {
                    byte[] byteArray = Base64.decode(bytes, Base64.DEFAULT);
                    Context context = this.cordova.getActivity();
                    ContentResolver contentResolver = context.getContentResolver();
                    if (Build.VERSION.SDK_INT >= 29) {
                        final ContentValues contentValues = new ContentValues();
                        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);
                        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, fileDir);
                        contentValues.put(MediaStore.MediaColumns.IS_PENDING, 1);

                        Uri content = contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues);

                        OutputStream out = contentResolver.openOutputStream(content);
                        out.write(byteArray);
                        out.flush();
                        out.close();

                        contentValues.clear();
                        contentValues.put(MediaStore.Video.Media.IS_PENDING, 0);
                        contentResolver.update(content, contentValues, null, null);
                        callbackContext.success(content.getPath());
                    } else {
                        if (cordova.hasPermission(WRITE_EXTERNAL_STORAGE)) {
                            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
                            File dir = new File(path + "/" + fileDir);
                            dir.mkdirs();
                            File file = new File(dir, fileName);
                            FileOutputStream out = new FileOutputStream(file);
                            out.write(byteArray);
                            out.flush();
                            out.close();
                            Uri contentUri = Uri.fromFile(file);
                            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, contentUri));
                            callbackContext.success(file.getAbsolutePath());
                        } else {
                            getWritePermission(WRITE_CODE);
                        }

                    }
                } catch (RuntimeException | IOException e) {
                    e.printStackTrace();
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    private void storeImage(String bytes, String fileDir, String fileName, CallbackContext callbackContext) {
        final CordovaInterface _cordova = cordova;

        cordova.getThreadPool().execute(new Runnable() {
            final CordovaInterface cordova = _cordova;

            @Override
            public void run() {
                try {
                    byte[] byteArray = Base64.decode(bytes, Base64.DEFAULT);
                    System.out.println("the bytearray is: " + Arrays.toString(byteArray));
                    Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                    Context context = this.cordova.getActivity();
                    ContentResolver contentResolver = context.getContentResolver();
                    if (Build.VERSION.SDK_INT >= 29) {
                        final ContentValues contentValues = new ContentValues();
                        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
                        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, fileDir);
                        contentValues.put(MediaStore.MediaColumns.IS_PENDING, 1);

                        Uri imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

                        OutputStream out = contentResolver.openOutputStream(imageUri);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

                        contentValues.clear();
                        contentValues.put(MediaStore.Images.Media.IS_PENDING, 0);
                        contentResolver.update(imageUri, contentValues, null, null);
                        callbackContext.success(imageUri.getPath());
                    } else {
                        if (cordova.hasPermission(WRITE_EXTERNAL_STORAGE)) {
                            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                            File dir = new File(path + "/" + fileDir);
                            dir.mkdirs();
                            File file = new File(dir, fileName);
                            FileOutputStream out = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                            Uri contentUri = Uri.fromFile(file);
                            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, contentUri));
                            callbackContext.success(file.getAbsolutePath());
                        } else {
                            getWritePermission(WRITE_CODE);
                        }
                    }
                } catch (RuntimeException | IOException e) {
                    e.printStackTrace();
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }


    private void storeDocument(String text, String mimeType, String fileDir, String fileName, CallbackContext callbackContext) {
        final CordovaInterface _cordova = cordova;

        cordova.getThreadPool().execute(new Runnable() {
            final CordovaInterface cordova = _cordova;

            @Override
            public void run() {
                try {
                    Context context = this.cordova.getActivity();
                    ContentResolver contentResolver = context.getContentResolver();
                    if (Build.VERSION.SDK_INT >= 29) {
                        final ContentValues contentValues = new ContentValues();
                        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);
                        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, fileDir);
                        contentValues.put(MediaStore.MediaColumns.IS_PENDING, 1);

                        Uri content = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues);

                        OutputStream out = contentResolver.openOutputStream(content);
                        out.write(text.getBytes(StandardCharsets.UTF_8));
                        out.flush();
                        out.close();

                        contentValues.clear();
                        contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0);
                        contentResolver.update(content, contentValues, null, null);
                        callbackContext.success(content.getPath());
                    } else {
                        if (cordova.hasPermission(WRITE_EXTERNAL_STORAGE)) {
                            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                            File dir = new File(path + "/" + fileDir);
                            dir.mkdirs();
                            File file = new File(dir, fileName);
                            FileOutputStream out = new FileOutputStream(file);
                            out.write(text.getBytes(StandardCharsets.UTF_8));
                            Uri contentUri = Uri.fromFile(file);
                            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, contentUri));
                            callbackContext.success(file.getAbsolutePath());
                        } else {
                            getWritePermission(WRITE_CODE);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    private void getDataFromFile(String mimeType, CallbackContext callbackContext) {
        final CordovaInterface _cordova = cordova;

        cordova.getThreadPool().execute(new Runnable() {
            final CordovaInterface cordova = _cordova;

            @Override
            public void run() {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType(mimeType);
                fileReadContext = callbackContext;
                cordova.startActivityForResult(CordovaAndroidFuns.this, intent, SELECT_PHOTO_CODE);
            }
        });

    }

    private void getDeviceVersion(CallbackContext callbackContext) {

        cordova.getThreadPool().execute(() -> {
            try {
                callbackContext.success(Build.VERSION.SDK_INT);
            } catch (Exception e) {
                e.printStackTrace();
                callbackContext.error(e.getMessage());
            }
        });

    }

    private CallbackContext fileReadContext = null;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == SELECT_PHOTO_CODE) {
            if (fileReadContext == null) {
                Log.e(TAG, "Some error occurred!");
                return;
            }
            if (resultCode == Activity.RESULT_OK) {
                Uri data = intent.getData();
                if (data != null) {
                    try {
                        InputStream is = cordova.getActivity().getContentResolver().openInputStream(data);
                        StringBuilder sb = new StringBuilder();
                        Scanner sc = new Scanner(is);
                        while (sc.hasNextLine()) {
                            sb.append(sc.nextLine()).append("\n");
                        }
                        fileReadContext.success(sb.toString().trim());
                    } catch (Exception e) {
                        e.printStackTrace();
                        fileReadContext.error(e.getMessage());
                    }
                } else {
                    fileReadContext.error("Data received is null!");
                }
            } else {
                fileReadContext.error("Result is " + resultCode);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, intent);
        }
    }

    @SuppressWarnings("SameParameterValue")
    private void getWritePermission(int requestCode) {
        cordova.requestPermission(this, requestCode, WRITE_EXTERNAL_STORAGE);
    }

    public void onRequestPermissionResult(int requestCode, String[] permissions,
                                          int[] grantResults) {
        for (int r : grantResults) {
            if (r == PackageManager.PERMISSION_DENIED) {
                Log.e(TAG, "onRequestPermissionResult: Permission(s) denied");
                Toast.makeText(cordova.getContext(), "Permission denied! Grant permission and try again!", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (requestCode == WRITE_CODE) {
            Toast.makeText(cordova.getContext(), "Storage permission granted!", Toast.LENGTH_SHORT).show();
        }
    }

    private static final String PREFS_KEY = "CordovaAndroidFunsKey";
}