package com.example.android_3d_loader.core.framebuffer;

import android.opengl.GLES30;

import com.example.android_3d_loader.core.texture.Texture;
import com.example.android_3d_loader.core.texture.buffer.ColorBuffer;
import com.example.android_3d_loader.core.texture.buffer.DepthBuffer;

public class FrameBuffer {
    private int width;
    private int height;
    private int fbo;

    public FrameBuffer(int width, int height, Texture emptyColorTexture){
        fbo = FrameBufferHelper.createFrameBuffer(width, height, emptyColorTexture.getTex());
        this.width = width;
        this.height = height;
    }

    public FrameBuffer(ColorBuffer emptyColorBuffer){
        fbo = FrameBufferHelper.createFrameBuffer(emptyColorBuffer.getWidth(),
                emptyColorBuffer.getHeight(), emptyColorBuffer.getTex());
        this.width = emptyColorBuffer.getWidth();
        this.height = emptyColorBuffer.getHeight();
    }

    public FrameBuffer(DepthBuffer emptyDepthBuffer){
        fbo = FrameBufferHelper.createDepthMap(emptyDepthBuffer.getWidth(), emptyDepthBuffer.getHeight(), emptyDepthBuffer.getTex());
        this.width = emptyDepthBuffer.getWidth();
        this.height = emptyDepthBuffer.getHeight();
    }

    public FrameBuffer(ColorBuffer... colorBuffers){
        int[] list = new int[colorBuffers.length];
        for (int i = 0; i < colorBuffers.length; i++){
            list[i] = colorBuffers[i].getTex();
        }
        this.width = colorBuffers[0].getWidth();
        this.height = colorBuffers[0].getHeight();
        fbo = FrameBufferHelper.createFrameBuffer(width, height, list);
    }

    public FrameBuffer(int width, int height, ColorBuffer... colorBuffers){
        int[] list = new int[colorBuffers.length];
        for (int i = 0; i < colorBuffers.length; i++){
            list[i] = colorBuffers[i].getTex();
        }
        this.width = width;
        this.height = height;
        fbo = FrameBufferHelper.createFrameBuffer(width, height, list);
    }

    public void enable(){
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, fbo);
    }

    public void disable(){
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0);
    }

    public int getFbo() {
        return fbo;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
