package com.example.android_3d_loader.core.material;

import android.content.Context;

import com.example.android_3d_loader.R;
import com.example.android_3d_loader.core.texture.Texture;
import com.example.android_3d_loader.core.texture.buffer.ColorBuffer;

public class BloomMaterial extends Material {

    private static String TAG = "BloomMaterial";
    private ColorBuffer bloomMap = ColorBuffer.getNullColorBuffer();
    private ColorBuffer sourceMap = ColorBuffer.getNullColorBuffer();

    private boolean isHorizontal = true;
    private boolean isComplete = false;

    public BloomMaterial(Context context) {
        super(context, R.raw.bloom_vs, R.raw.bloom_fs);
    }

    @Override
    public void update() {
        shader.setTexture("BloomMap", bloomMap, 1);
        shader.setTexture("SourceMap", sourceMap, 2);
        shader.setBool("IsHorizontal", isHorizontal);
        shader.setBool("IsComplete", isComplete);
        isComplete = false;
    }

    public void setHorizontal(boolean horizontal) {
        isHorizontal = horizontal;
    }

    public void setBloomMap(Texture bloomMap) {
        this.bloomMap = (ColorBuffer) bloomMap;
    }

    public void setSourceMap(Texture sourceMap) {
        this.sourceMap = (ColorBuffer) sourceMap;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }
}
