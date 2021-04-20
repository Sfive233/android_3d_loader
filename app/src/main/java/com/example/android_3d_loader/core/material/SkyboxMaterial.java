package com.example.android_3d_loader.core.material;

import android.content.Context;

import com.example.android_3d_loader.R;
import com.example.android_3d_loader.core.texture.Texture;
import com.example.android_3d_loader.core.texture.textureCube.TextureCube;

public class SkyboxMaterial extends Material{

    protected TextureCube cubeMap = TextureCube.getNullTextureCube();
    protected float cubeMapLod = 0.0f;

    public SkyboxMaterial(Context context) {
        super(context, R.raw.skybox_vs, R.raw.skybox_fs);
    }

    @Override
    public void init() {

    }

    @Override
    public void update() {
        shader.setCubeMap("CubeMap", cubeMap);
        shader.setFloat("CubeMapLod", cubeMapLod);
    }

    public void setCubeMap(Texture cubeMap) {
        this.cubeMap = (TextureCube) cubeMap;
    }

    public void setCubeMapLod(float cubeMapLod) {
        this.cubeMapLod = cubeMapLod;
    }
}
