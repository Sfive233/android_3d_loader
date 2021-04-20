package com.example.android_3d_loader.controller.renderPass;

import android.content.Context;
import android.opengl.GLES30;

import com.example.android_3d_loader.controller.communicator.GLCommunicator;
import com.example.android_3d_loader.core.dataType.Matrix4;
import com.example.android_3d_loader.core.framebuffer.FrameBuffer;

public abstract class BaseRenderPass implements RenderPass{
    protected static int SRC_WIDTH;
    protected static int SRC_HEIGHT;
    protected Context context;
    protected Matrix4 mModelMatrix = new Matrix4();
    protected static RenderPass emptyRenderPass;

    public static RenderPass getEmptyRenderPass(){
        if (emptyRenderPass == null){
            emptyRenderPass = new RenderPass() {
                @Override
                public void renderInit() {

                }

                @Override
                public void renderReact() {

                }

                @Override
                public Object[] renderUpdate(Object... inputs) {
                    return new Object[0];
                }

                @Override
                public void setContext(Context context) {

                }
            };
        }
        return emptyRenderPass;
    }

    public BaseRenderPass(){
        mModelMatrix.setIdentity();
    }

    public BaseRenderPass(Context context) {
        this.context = context;
        mModelMatrix.setIdentity();
    }

    public static void setSrcWidth(int srcWidth) {
        SRC_WIDTH = srcWidth;
    }

    public static void setSrcHeight(int srcHeight) {
        SRC_HEIGHT = srcHeight;
    }

    protected void clearColor(){
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);
    }
    protected void setClearColor(float r, float g, float b){
        GLES30.glClearColor(r, g, b, 1.0f);
    }
    protected void setClearColorToDefault(){
        GLES30.glClearColor(.0f, .0f, .0f, 1.0f);
    }
    protected void openViewport(){
        GLES30.glViewport(0, 0, SRC_WIDTH, SRC_HEIGHT);
    }
    protected void openViewport(int width, int height){
        GLES30.glViewport(0, 0, width, height);
    }
    protected void openViewport(FrameBuffer frameBuffer){
        GLES30.glViewport(0, 0, frameBuffer.getWidth(), frameBuffer.getHeight());
    }
    protected void clearColorAndDepth(){
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);
    }
    protected void enableDepthTest(){
        GLES30.glEnable(GLES30.GL_DEPTH_TEST);
    }
    protected void disableDepthTest(){
        GLES30.glDisable(GLES30.GL_DEPTH_TEST);
    }
    protected void depthLE(){
        GLES30.glDepthFunc(GLES30.GL_LEQUAL);
    }
    protected void depthDefault(){
        GLES30.glDepthFunc(GLES30.GL_LESS);
    }
    protected void enableCullFace(){
        GLES30.glEnable(GLES30.GL_CULL_FACE);
    }
    protected void disableCullFace(){
        GLES30.glDisable(GLES30.GL_CULL_FACE);
    }
    protected void cullFront(){
        GLES30.glCullFace(GLES30.GL_FRONT);
    }
    protected void cullBack(){
        GLES30.glCullFace(GLES30.GL_BACK);
    }
    protected Context getContext(){
        return context;
    }
    public void setContext(Context context){
        this.context = context;
    }
}
