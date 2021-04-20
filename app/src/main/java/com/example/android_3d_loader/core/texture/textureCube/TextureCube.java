package com.example.android_3d_loader.core.texture.textureCube;

import com.example.android_3d_loader.core.texture.Texture;
import com.google.gson.annotations.Expose;

public class TextureCube extends Texture {
    @Expose
    protected String name;
    @Expose
    protected String path;
    protected static TextureCube nullTextureCube;

    public TextureCube(String name, String path, int texId) {
        this.name = name;
        this.path = path;
        this.tex = texId;
    }

    public static TextureCube getNullTextureCube(){
        if (nullTextureCube == null){
            nullTextureCube = new TextureCube("null", "null", nullTex);
        }
        return nullTextureCube;
    }
}
