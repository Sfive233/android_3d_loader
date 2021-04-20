package com.example.android_3d_loader.core.texture.buffer;

import com.example.android_3d_loader.core.texture.Texture;
import com.example.android_3d_loader.core.util.TextureHelper;

public class DepthBuffer extends Texture {
    protected int width;
    protected int height;
    protected static DepthBuffer nullDepthBuffer;

    public static DepthBuffer getNullDepthBuffer(){
        if (nullDepthBuffer == null){
            nullDepthBuffer = new DepthBuffer(0, nullTex);
        }
        return nullDepthBuffer;
    }

    public DepthBuffer(int size){
        this.width = size;
        this.height = size;
        tex = TextureHelper.createTextureForDepthMap(width, height);
    }

    private DepthBuffer(int size, int texId){
        this.width = size;
        this.height = size;
        tex = texId;
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
