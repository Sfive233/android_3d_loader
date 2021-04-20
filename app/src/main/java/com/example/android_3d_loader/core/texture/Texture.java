package com.example.android_3d_loader.core.texture;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES30;
import android.util.Log;

import com.example.android_3d_loader.core.exception.AssetsNotFoundException;
import com.example.android_3d_loader.core.exception.TextureNotFoundException;
import com.example.android_3d_loader.core.util.TextureHelper;

import java.util.List;

public class Texture{
    public static final String TAG = "Texture";
    public static final int WARP_CLAMP_TO_EDGE = GLES30.GL_CLAMP_TO_EDGE;
    public static final int WARP_REPEAT = GLES30.GL_REPEAT;
    public static final int FILTER_NEAREST = GLES30.GL_NEAREST;
    public static final int FILTER_LINEAR = GLES30.GL_LINEAR;

    protected int tex;
    protected static int nullTex = -1;

    public Texture(){
    }

    public Texture(List<Bitmap> bitmapList){
        this.tex = TextureHelper.loadCubeMapByBitmapList(bitmapList);
    }

    public Texture(int texId){
        this.tex = texId;
    }

    public Texture(Context context, String path){
        load(context, path);

//        if (tex == 1){
//            Log.d(TAG, "Texture: Texture not found! using default null texture.");//todo 改写为抛出异常
//            this.name = Texture.getNullTexture().name;
//            this.path = Texture.getNullTexture().path;
//            this.tex = Texture.getNullTexture().getTex();
//        }
    }

    public Texture(Context context, String path, int warp, int filter){
        String name = path.substring(path.lastIndexOf("/")+1);
        String format = name.toLowerCase().substring(name.lastIndexOf("."));
        if (format.equals(".tga")){
            tex = TextureHelper.createTGATexture(context, path);
        }else {
            tex = TextureHelper.createTexture(context, path, warp, filter);
        }
    }

    protected void load(Context context, String path){
        boolean isFromAssets = true;
        if (path.startsWith("/")){
            isFromAssets = false;
        }

        String format = path.toLowerCase().substring(path.lastIndexOf("."));
        switch (format){
            case ".tga":
                if (isFromAssets){
                    tex = TextureHelper.createTGATexture(context, path);
                }else {
                    throw new RuntimeException("not support");
                }
                break;
            case ".jpg":
            case ".png":
                if (isFromAssets){
                    tex = TextureHelper.createTexture(context, path, WARP_REPEAT, FILTER_LINEAR);
                }else {
                    tex = TextureHelper.createTextureFromExternal(context, path, WARP_REPEAT, FILTER_LINEAR);
                }
                break;
            case ".hdr":
                tex = TextureHelper.createHDRImage(context, path);
                break;
            default:
                throw new RuntimeException("format: "+format+" not support!");
        }
    }

    public static void createNullTexture(Context context){
        if (nullTex == -1){
            nullTex = TextureHelper.createTexture(context, "default/null.png", WARP_REPEAT, FILTER_NEAREST);
        }
    }

    public int getTex(){
        return tex;
    }

    public void setTex(int tex) {
        this.tex = tex;
    }
}
