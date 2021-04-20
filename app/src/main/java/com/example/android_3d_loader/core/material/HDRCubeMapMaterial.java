package com.example.android_3d_loader.core.material;

import android.content.Context;
import android.opengl.GLES30;

import com.example.android_3d_loader.R;
import com.example.android_3d_loader.core.texture.Texture;
import com.example.android_3d_loader.core.texture.texture2D.Texture2D;

public class HDRCubeMapMaterial extends Material {

    protected Texture2D hdrTexture = Texture2D.getNullTexture2D();

    public HDRCubeMapMaterial(Context context) {
        super(context, R.raw.hdr_cube_map_vs, R.raw.hdr_cube_map_fs);
    }

    @Override
    public void update() {
        GLES30.glActiveTexture(GLES30.GL_TEXTURE1);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, hdrTexture.getTex());
        shader.setInt("HDRMap", 1);
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
    }

    public void setTexture(Texture texture) {
        this.hdrTexture = (Texture2D) texture;
    }
}
