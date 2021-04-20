package com.example.android_3d_loader.view.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.android_3d_loader.R;
import com.example.android_3d_loader.view.GLESWindow;
import com.example.android_3d_loader.view.widget.dialog.ProgressDialog;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ConstraintLayout mRoot;
    private GLESWindow mGLESWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");

//        Debug.startMethodTracing("shixintrace");

        // 开启无标题
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        // 全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

//        mMyGLESWindow = findViewById(R.id.my_gles_window);
        mRoot = findViewById(R.id.main_drawer);
        mGLESWindow = new GLESWindow(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRoot.addView(mGLESWindow, 0, new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }
        }, 1000);
        ProgressDialog.getInstance().show(getFragmentManager(), "First Boot");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.d(TAG, "onWindowFocusChanged: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
//        Debug.stopMethodTracing();
        mGLESWindow.handleOnResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mGLESWindow.handleOnResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {

        mGLESWindow.handleOnPause();
        Log.d(TAG, "onPause: ");

        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}