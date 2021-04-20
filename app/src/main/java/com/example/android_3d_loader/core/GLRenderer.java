package com.example.android_3d_loader.core;

import android.content.Context;
import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.os.Debug;
import android.util.Log;
import android.view.WindowManager;

import com.example.android_3d_loader.controller.GLRequest;
import com.example.android_3d_loader.controller.communicator.GLCommunicator;
import com.example.android_3d_loader.controller.renderPass.RenderPass;
import com.example.android_3d_loader.controller.requestSet.RequestCollector;
import com.example.android_3d_loader.core.dataType.Int;
import com.example.android_3d_loader.controller.renderPass.BaseRenderPass;
import com.example.android_3d_loader.core.texture.Texture;
import com.example.android_3d_loader.view.widget.dialog.ProgressDialog;

import java.util.HashMap;
import java.util.Map;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLRenderer implements GLSurfaceView.Renderer {
    private static final String TAG = "Renderer";
    private final Context context;
    private static int SRC_WIDTH;
    private static int SRC_HEIGHT;
    private Int currentFPS = new Int(0);
    private GLCommunicator glCommunicator = GLCommunicator.getInstance();

    public GLRenderer(Context context) {
        this.context = context;

        // get device's resolution
        Point point = new Point();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getSize(point);
        SRC_WIDTH = point.x;
        SRC_HEIGHT = point.y;

        glCommunicator.setGLSurfaceViewWidth(point.x);
        glCommunicator.setGLSurfaceViewHeight(point.y);
        BaseRenderPass.setSrcWidth(SRC_WIDTH);
        BaseRenderPass.setSrcHeight(SRC_HEIGHT);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.d(TAG, "onSurfaceCreated: ");

        glCommunicator.collectPlatformInfo();
        Texture.createNullTexture(context);

        init();
    }

    private HashMap<GLRequest, RenderPass[]> requestHashMap;
    private RenderPass[] bootPasses;
    private void init(){

        RequestCollector requestCollector = glCommunicator.getRequestCollector();
        requestHashMap = requestCollector.getRequestSetHashMap();
        bootPasses = requestHashMap.get(GLRequest.BOOT);
        for (RenderPass renderPass: bootPasses){
            renderPass.setContext(getContext());
            renderPass.renderInit();
        }
    }

    public static boolean isReLoad = true;
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        isReLoad = true;
    }

    private void reload(){
        if (isReLoad) {
            isAllowToDraw = false;
            RenderPass[] renderPasses = requestHashMap.get(glCommunicator.getCurrentGLRequest());
            for (RenderPass renderPass: renderPasses){
                renderPass.renderReact();
            }
            glCommunicator.setCurrentGLStatus(GLRequest.RUNNING);// glStatus该glRequest

            currentFPS = new Int(0);
            glCommunicator.putData("CurrentFPS", currentFPS);

            ProgressDialog.getInstance().dismiss();
            isReLoad = false;

            isAllowToDraw = true;
        }
    }

    protected boolean isAllowToDraw = false;
    @Override
    public void onDrawFrame(GL10 gl) {

        reload();

        if (isAllowToDraw){
            countFPS();

            Object[] passOutput = null;
            for (RenderPass renderPass: bootPasses){
                passOutput = renderPass.renderUpdate(passOutput);
            }
        }

    }

    private void countFPS(){
        currentFPS.setVal(currentFPS.getVal() + 1);
    }
    private Context getContext(){
        return context;
    }
}
