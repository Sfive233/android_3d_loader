package com.example.android_3d_loader.core.material;

import android.content.Context;

import com.example.android_3d_loader.R;
import com.example.android_3d_loader.core.dataType.Float;
import com.example.android_3d_loader.core.texture.Texture;
import com.example.android_3d_loader.core.texture.textureCube.TextureCube;

public class PBRCubeMapConvolutionMaterial extends Material{

    protected TextureCube cubeMap = TextureCube.getNullTextureCube();
    protected Float sampleDelta = new Float(0.1f);

    public PBRCubeMapConvolutionMaterial(Context context) {
        super(context, R.raw.pbr_ibl_convolution_vs, R.raw.pbr_ibl_convolution_fs);
    }

    @Override
    public void update() {
        shader.setCubeMap("CubeMap", cubeMap);
        shader.setFloat("SampleDelta", sampleDelta);
    }

    public void setCubeMap(Texture cubeMap) {
        this.cubeMap = (TextureCube) cubeMap;
    }

    public Texture getCubeMap() {
        return cubeMap;
    }

    public Float getSampleDelta() {
        return sampleDelta;
    }

    public void setSampleDelta(float sampleDelta) {
        this.sampleDelta.setVal(sampleDelta);
    }
}
