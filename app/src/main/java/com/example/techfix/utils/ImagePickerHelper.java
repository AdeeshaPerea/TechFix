package com.example.techfix.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.File;

public class ImagePickerHelper {

    private static final String TAG = "ImagePickerHelper";

    /**
     * Create a temporary file and return FileProvider content URI for camera output
     */
    public static Uri createTempCameraUri(Context context) {
        try {
            File cacheDir = new File(context.getCacheDir(), "camera_photos");
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            File photoFile = File.createTempFile("photo_" + System.currentTimeMillis(), ".jpg", cacheDir);
            String authority = context.getPackageName() + ".fileprovider";
            return FileProvider.getUriForFile(context, authority, photoFile);
        } catch (Exception e) {
            Log.e(TAG, "Error creating camera temp URI: " + e.getMessage());
            return null;
        }
    }
}
