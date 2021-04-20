package com.example.android_3d_loader.core.material.toneMapping;

import android.content.Context;

import com.example.android_3d_loader.R;

public class ACESToneMappingMaterial extends ToneMappingMaterial {

    public ACESToneMappingMaterial(Context context) {
        super(context, R.raw.aces_tone_mapping_vs, R.raw.aces_tone_mapping_fs);
    }

    @Override
    public void update() {
        shader.setTexture("SourceImage", sourceImage);
        shader.setFloat("Exposure", exposure);
    }
}
