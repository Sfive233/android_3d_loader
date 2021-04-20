package com.example.android_3d_loader.controller.communicator;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.example.android_3d_loader.controller.GLRequest;
import com.example.android_3d_loader.controller.communicator.Util.FileUtil;
import com.example.android_3d_loader.controller.requestSet.RequestCollector;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import java.util.HashMap;
import java.util.Map;

public class GLCommunicator {
    private static final String TAG = "GLCommunicator";
    private static GLCommunicator instance;

    private String mRenderHardwareName;
    private String mRenderHardwareVendor;
    private String mOpenglVersion;
    private String mGLSLVersion;
    private int mGLSurfaceViewWidth;
    private int mGLSurfaceViewHeight;

    private final RequestCollector requestCollector = new RequestCollector();
    public RequestCollector getRequestCollector() {
        return requestCollector;
    }

    @Expose
    private final HashMap<String, Object> mDataSets = new HashMap<>();

    @Expose
    private HashMap<String, String> mClazzSets = new HashMap<>();

    private HashMap<String, Object> mTemps = new HashMap<>();
    public void addTemp(String key, Object val){
        mTemps.put(key, val);
    }
    public Object getTemp(String key){
        return mTemps.get(key);
    }


    private final Gson mGson;

    private boolean mIsSaveExist = false;

    private GLRequest mCurrentGLRequest = GLRequest.BOOT;

    private GLSurfaceView mGLSurfaceView;

    public static GLCommunicator getInstance(){
        if (instance == null){
            instance = new GLCommunicator();
        }
        return instance;
    }

    private GLCommunicator(){
        mGson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
    }

    public void collectPlatformInfo(){
        mRenderHardwareName = GLES30.glGetString(GLES30.GL_RENDERER);
        mRenderHardwareVendor = GLES30.glGetString(GLES30.GL_VENDOR);
        mOpenglVersion = GLES30.glGetString(GLES30.GL_VERSION);
        mGLSLVersion = GLES30.glGetString(GLES30.GL_SHADING_LANGUAGE_VERSION);
    }

    public void save(Context context){

        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        Gson gson = builder.create();

        String finalStr = gson.toJson(this);
        FileUtil.saveFile(context, "defaultScene.json", finalStr);
        mIsSaveExist = true;

        Log.d(TAG, "save: Save to \"defaultScene.json\"");
    }

    public void loadSavedData(Context context){
        String savedDataStr = FileUtil.readFile(context, "defaultScene.json");
        if (savedDataStr != null){
            GLCommunicator mTempGLCommunicator = mGson.fromJson(savedDataStr, GLCommunicator.class);
            HashMap<String, Object> savedObjectHashMap = mTempGLCommunicator.getDataSets();
            for (Map.Entry<String, Object> savedObjectEntry : savedObjectHashMap.entrySet()) {
                String clazzPath = mTempGLCommunicator.getClazzSets().get(savedObjectEntry.getKey());
                Object currentSavedObject = savedObjectEntry.getValue();
                Class clazz = null;
                try {
                    clazz = Class.forName(clazzPath);
                    String json = mGson.toJson(currentSavedObject);
                    Object pausedObject = mGson.fromJson(json, clazz);
                    mDataSets.put(savedObjectEntry.getKey(), pausedObject);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            mClazzSets = mTempGLCommunicator.getClazzSets();
            mCurrentGLRequest = mTempGLCommunicator.getCurrentGLRequest();
            mIsSaveExist = true;
        }else {
            mIsSaveExist = false;
        }
    }

    private HashMap<String, Object> getDataSets(){
        return mDataSets;
    }

    public void putData(String key, Object data){
        mDataSets.put(key, data);
        mClazzSets.put(key, data.getClass().getName());
    }

    public Object getData(String key){
        Object result = mDataSets.get(key);
        if (result == null){
            new IllegalArgumentException("Key \"" + key + "\" is not exists").printStackTrace();
        }
        return result;
    }

    public void removeData(String key){
        mDataSets.remove(key);
        mClazzSets.remove(key);
    }

    public void clearAll(Context context){
        mDataSets.clear();
        mClazzSets.clear();
        save(context);
    }

    public int getGLSurfaceViewWidth() {
        return mGLSurfaceViewWidth;
    }

    public void setGLSurfaceViewWidth(int gLSurfaceViewWidth) {
        mGLSurfaceViewWidth = gLSurfaceViewWidth;
    }

    public int getGLSurfaceViewHeight() {
        return mGLSurfaceViewHeight;
    }

    public void setGLSurfaceViewHeight(int gLSurfaceViewHeight) {
        mGLSurfaceViewHeight = gLSurfaceViewHeight;
    }

    public String getRenderHardwareName() {
        return mRenderHardwareName;
    }

    public String getRenderHardwareVendor() {
        return mRenderHardwareVendor;
    }

    public String getOpenglVersion() {
        return mOpenglVersion;
    }

    public String getGLSLVersion() {
        return mGLSLVersion;
    }

    public HashMap<String, String> getClazzSets() {
        return mClazzSets;
    }

    public boolean isSaveExist() {
        return mIsSaveExist;
    }

    public GLRequest getCurrentGLRequest() {
        return mCurrentGLRequest;
    }

    public void setCurrentGLStatus(GLRequest mCurrentGLRequest) {
        this.mCurrentGLRequest = mCurrentGLRequest;
    }

    public GLSurfaceView getGLSurfaceView() {
        return mGLSurfaceView;
    }

    public void setGLSurfaceView(GLSurfaceView mGLSurfaceView) {
        this.mGLSurfaceView = mGLSurfaceView;
    }
}
