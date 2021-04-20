package com.example.android_3d_loader.core.material;

import android.content.Context;

import com.example.android_3d_loader.R;
import com.example.android_3d_loader.core.texture.Texture;
import com.example.android_3d_loader.core.texture.textureCube.TextureCube;

public class PBRCubeMapPreFilterMaterial extends Material{

    private TextureCube cubeMap = TextureCube.getNullTextureCube();
    private float roughness;

    public PBRCubeMapPreFilterMaterial(Context context) {
        super(context, R.raw.pbr_ibl_prefiter_vs, R.raw.pbr_ibl_prefilter_fs);
    }

    @Override
    public void update() {
        shader.setCubeMap("CubeMap", cubeMap);
        shader.setFloat("Roughness", roughness);
    }

    public void setCubeMap(Texture cubeMap) {
        this.cubeMap = (TextureCube) cubeMap;
    }

    public void setRoughness(float roughness) {
        this.roughness = roughness;
    }
}
