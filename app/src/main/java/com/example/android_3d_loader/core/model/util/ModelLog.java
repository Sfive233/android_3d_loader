package com.example.android_3d_loader.core.model.util;

import android.util.Log;

public class ModelLog {
    private static final String TAG = "ModelLog";
    private static boolean isShow = false;
    public static void d(String msg){
        if (isShow){
            Log.d(TAG, "d: "+msg);
        }
    }
    public static void i(String msg){
        if (isShow){
            Log.i(TAG, "d: "+msg);
        }
    }
}
