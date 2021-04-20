package com.example.android_3d_loader.core.texture.buffer;

import android.opengl.GLES30;

import com.example.android_3d_loader.core.model.mesh.basicObject.ScreenQuad;
import com.example.android_3d_loader.core.framebuffer.FrameBuffer;

public class BufferPreRenderer extends ScreenQuad {

    private ColorBuffer colorBuffer;
    private BufferParam bufferParam;
    private FrameBuffer frameBuffer;

    public BufferPreRenderer(){

    }

    public ColorBuffer outputMap(){

        initFrameBuffer();
        render();

        return colorBuffer;
    }

    private void initFrameBuffer(){
        colorBuffer = new ColorBuffer(bufferParam);
        frameBuffer = new FrameBuffer(colorBuffer);
    }

    private void render(){
        frameBuffer.enable();
        GLES30.glViewport(0, 0, bufferParam.getMapSize(), bufferParam.getMapSize());
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);

        draw();

        frameBuffer.disable();
    }

    public void setBufferParam(BufferParam bufferParam) {
        this.bufferParam = bufferParam;
    }
}
