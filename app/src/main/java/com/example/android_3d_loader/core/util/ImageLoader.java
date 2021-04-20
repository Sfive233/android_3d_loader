package com.example.android_3d_loader.core.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.android_3d_loader.core.exception.AssetsNotFoundException;
import com.example.android_3d_loader.core.exception.TextureNotFoundException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageLoader {

    public static final String TAG = "ImageLoader";

    public static Bitmap getBitmap(Context content, int imageResourceId){
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;// 要图像的原始数据，不要缩放版本的数据
        final Bitmap bitmap = BitmapFactory.decodeResource(content.getResources(), imageResourceId, options);

        if(bitmap == null){
            if(LoggerConfig.ON){
                Log.e(TAG, "资源" + imageResourceId + "无法解码");
            }
            return null;
        }

        return bitmap;
    }

    public static Bitmap getBitmap(Context context, String path) throws IOException{
        if (path.startsWith("/")){
            return BitmapFactory.decodeFile(path);
        }else {
            return getBitmapWithoutPreMultiplied(context, path);
        }
    }

    public static Bitmap getBitmapWithoutPreMultiplied(Context context, String path) throws IOException{
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inPremultiplied = false;
        return getBitmapInAssetsWithOptions(context, path, options);
    }

    public static Bitmap getBitmapInAssetsWithOptions(Context context, String path, BitmapFactory.Options options) throws IOException {
        return BitmapFactory.decodeStream(context.getAssets().open(path), null, options);
    }

    public static byte[] getBytesFromBitmap(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Bitmap.CompressFormat compressFormat = bitmap.hasAlpha()? Bitmap.CompressFormat.PNG: Bitmap.CompressFormat.JPEG;
        bitmap.compress(compressFormat, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
