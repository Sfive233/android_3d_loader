package com.example.android_3d_loader.core.material.toneMapping;

import android.content.Context;

import com.example.android_3d_loader.R;

public class CEToneMappingMaterial extends ToneMappingMaterial {

    public CEToneMappingMaterial(Context context) {
        super(context, R.raw.ce_tone_mapping_vs, R.raw.ce_tone_mapping_fs);// todo ce该reinhard 直接叫xxxMaterial
    }
    @Override
    public void update() {
        shader.setTexture("SourceImage", sourceImage);
        shader.setFloat("Exposure", exposure);
    }
}
