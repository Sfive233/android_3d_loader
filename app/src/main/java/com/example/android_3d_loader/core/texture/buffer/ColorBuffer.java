package com.example.android_3d_loader.core.texture.buffer;

import com.example.android_3d_loader.core.texture.Texture;
import com.example.android_3d_loader.core.util.TextureHelper;

public class ColorBuffer extends Texture {

    protected int width;
    protected int height;
    protected static ColorBuffer nullColorBuffer;

    public static ColorBuffer getNullColorBuffer(){
        if (nullColorBuffer == null){
            nullColorBuffer = new ColorBuffer(0, 0, nullTex);
        }
        return nullColorBuffer;
    }

    public ColorBuffer(BufferParam bufferParam){
        this.width = this.height = bufferParam.getMapSize();
        this.tex = TextureHelper.createTextureForFrameBuffer(width, height, bufferParam);
    }

    public ColorBuffer(int width, int height, BufferParam bufferParam){
        this.width = width;
        this.height = height;
        this.tex = TextureHelper.createTextureForFrameBuffer(width, height, bufferParam);
    }

    public ColorBuffer(int width, int height, int texId){
        this.width = width;
        this.height = height;
        this.tex = texId;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
