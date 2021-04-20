package com.example.android_3d_loader.core.material.toneMapping;

import android.content.Context;

import com.example.android_3d_loader.core.dataType.Float;
import com.example.android_3d_loader.core.texture.Texture;
import com.example.android_3d_loader.core.texture.buffer.ColorBuffer;
import com.example.android_3d_loader.core.material.Material;
import com.google.gson.annotations.Expose;

public abstract class ToneMappingMaterial extends Material {

    protected ColorBuffer sourceImage = ColorBuffer.getNullColorBuffer();
    @Expose
    protected Float exposure = new Float(1.0f);

    public ToneMappingMaterial(Context context, int vertexRes, int fragmentRes) {
        super(context, vertexRes, fragmentRes);
    }

    public void setSourceImage(Texture texture){
        sourceImage = (ColorBuffer) texture;
    }

    public Float getExposure() {
        return exposure;
    }

    public void setExposure(float exposure) {
        this.exposure.setVal(exposure);
    }
}
