package com.example.android_3d_loader.controller.process;

import android.content.Context;

import com.example.android_3d_loader.controller.communicator.GLCommunicator;

public abstract class Process {

    protected Object[] params;
    protected Context context;
    protected OnProcessFinishedListener onProcessFinishedListener;
    protected GLCommunicator glCommunicator;

    public Process(Context context, Object... params) {
        this.context = context;
        this.params = params;
        this.glCommunicator = GLCommunicator.getInstance();
    }

    public Process(Context context, OnProcessFinishedListener listener, Object... params) {
        this.context = context;
        this.onProcessFinishedListener = listener;
        this.params = params;
        this.glCommunicator = GLCommunicator.getInstance();
    }

    protected abstract Object[] onProcess();

    public void doProcess(){
        Object [] objects = onProcess();
        if (onProcessFinishedListener != null){
            onProcessFinishedListener.onFinish(objects);
        }
    }

    public void setOnProcessFinishedListener(OnProcessFinishedListener listener){
        this.onProcessFinishedListener = listener;
    }

    public interface OnProcessFinishedListener{
        void onFinish(Object... objects);
    }

    protected Context getContext(){
        return context;
    }
}
