package com.example.android_3d_loader.core.texture.texture2D;

import android.content.Context;

import com.example.android_3d_loader.core.exception.TextureNotFoundException;
import com.example.android_3d_loader.core.texture.Texture;
import com.example.android_3d_loader.core.util.TextureHelper;
import com.google.gson.annotations.Expose;

import java.util.HashMap;

public class Texture2D extends Texture{
    private static final String TAG = "Texture2D";
    @Expose
    protected String name;
    @Expose
    protected String path;
    protected boolean isChanged = true;
    protected static Texture2D nullTexture2D;

    public Texture2D(){
        this.name = "default";
        this.path = "default/null.png";
        this.tex = nullTex;
    }

    public Texture2D(Context context, String path){
        super(context, path);
        this.path = path;
        this.name = path.substring(path.lastIndexOf("/")+1, path.lastIndexOf("."));
    }

    public void reload(Context context){
        this.tex = TextureHelper.createTextureFromExternal(context, path, WARP_REPEAT, FILTER_LINEAR);
    }

    public static Texture2D getNullTexture2D(){
        if (nullTexture2D == null){
            nullTexture2D = new Texture2D();
        }
        return nullTexture2D;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.isChanged = !this.path.equals(path);
        this.path = path;
    }

    public boolean isChanged() {
        return isChanged;
    }

    public void setChanged(boolean changed) {
        isChanged = changed;
    }
}
