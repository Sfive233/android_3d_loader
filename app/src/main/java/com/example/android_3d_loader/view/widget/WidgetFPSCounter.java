package com.example.android_3d_loader.view.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.android_3d_loader.R;
import com.example.android_3d_loader.controller.GLRequest;
import com.example.android_3d_loader.controller.communicator.GLCommunicator;
import com.example.android_3d_loader.core.dataType.Int;

public class WidgetFPSCounter extends LinearLayout {
    private static final String TAG = "WidgetFPSCounter";
    private LinearLayout sRoot;
    private TextView mFpsNum;
    private GLCommunicator mCommunicator;
    private Int mCurrentFPS;
    private Handler mFPSHandler;

    public WidgetFPSCounter(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        sRoot = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.widget_fps_counter, this);
        mFpsNum = findViewById(R.id.fps_num);
        mFPSHandler = new Handler();
    }

    public void changeVisibility(boolean isChecked){
        sRoot.setVisibility(isChecked? VISIBLE: INVISIBLE);
        if (isChecked){
            beginCount();
        }
    }

    public void beginCount(){
        mFPSHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mCommunicator = GLCommunicator.getInstance();
                if (mCommunicator.getCurrentGLRequest().equals(GLRequest.RUNNING)) {
//                if(mCommunicator.isFree()){
                    if (sRoot.getVisibility() == VISIBLE) {
                        mCurrentFPS = (Int) mCommunicator.getData("CurrentFPS");
                        mFpsNum.setText(mCurrentFPS.getVal() + "");
                        mCurrentFPS.setVal(0);
                        if (!((Activity) getContext()).isFinishing()) {
                            beginCount();
                        }
                    }
                }else {
                    beginCount();
                }
            }
        }, 1000);
    }
}
