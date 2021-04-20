package com.example.android_3d_loader.core.material;

import android.content.Context;

import com.example.android_3d_loader.R;

public class PBRPreBRDFMaterial extends Material{

    public PBRPreBRDFMaterial(Context context) {
        super(context, R.raw.pbr_ibl_brdf_vs, R.raw.pbr_ibl_brdf_fs);
    }

    @Override
    public void update() {

    }
}
