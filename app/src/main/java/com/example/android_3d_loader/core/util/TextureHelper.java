package com.example.android_3d_loader.core.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLUtils;
import android.util.Log;

import com.example.android_3d_loader.core.exception.AssetsNotFoundException;
import com.example.android_3d_loader.core.texture.buffer.BufferParam;
import com.example.android_3d_loader.core.third.JavaHDR.HDREncoder;
import com.example.android_3d_loader.core.third.JavaHDR.HDRImage;
import com.example.android_3d_loader.core.third.tgareader.TGAReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_LINEAR_MIPMAP_LINEAR;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_CUBE_MAP;
import static android.opengl.GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_X;
import static android.opengl.GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y;
import static android.opengl.GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z;
import static android.opengl.GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_X;
import static android.opengl.GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Y;
import static android.opengl.GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Z;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.GL_UNPACK_ALIGNMENT;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glDeleteTextures;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glGenerateMipmap;
import static android.opengl.GLES20.glTexParameteri;
import static android.opengl.GLUtils.texImage2D;

public class TextureHelper {
    public static final String TAG = "TextureHelper";

    /**
     * 创建纹理对象
     * @param content Android上下文
     * @param resourceId 纹理资源Id
     * @return 纹理对象指针
     */
    public static int loadTexture(Context content, int resourceId){

        /**
         * 创建纹理对象
         */
        final int[] textureObjectIds = new int[1];
        glGenTextures(1, textureObjectIds, 0);

        if(textureObjectIds[0] == 0){
            if(LoggerConfig.ON){
                Log.w(TAG, "无法创建新的纹理对象");
            }

            return 0;
        }

        /**
         * 加载位图数据
         */
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;// 要图像的原始数据，不要缩放版本的数据
        final Bitmap bitmap = BitmapFactory.decodeResource(content.getResources(), resourceId, options);

        if(bitmap == null){
            if(LoggerConfig.ON){
                Log.w(TAG, "资源" + resourceId + "无法解码");
            }

            glDeleteTextures(1, textureObjectIds, 0);
            return 0;
        }

        /**
         * 2D纹理与textureObjectIds[0]做绑定
         */
        glBindTexture(GL_TEXTURE_2D, textureObjectIds[0]);

        /**
         * 设置纹理过滤，纹理缩小时使用三线性过滤，纹理放大时使用双线性过滤
         */
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        /**
         * 读取位图数据，并复制到纹理对象textureObjectIds[0]
         */
        texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);

        /**
         * 立即释放位图数据
         */
        bitmap.recycle();

        /**
         * 生成MIP贴图
         */
        glGenerateMipmap(GL_TEXTURE_2D);

        /**
         * 解除2D纹理对textureObjectIds[0]的绑定，以免意外改变textureObjectIds[0]
         */
        glBindTexture(GL_TEXTURE_2D, 0);

        return textureObjectIds[0];
    }

    public static int loadTexture(Context content, int resourceId, int textureUnit){
        final int[] textureObjectIds = new int[1];
        GLES30.glGenTextures(1, textureObjectIds, 0);

        if(textureObjectIds[0] == 0){
            if(LoggerConfig.ON){
                Log.w(TAG, "无法创建新的纹理对象");
            }

            return 0;
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        final Bitmap bitmap = BitmapFactory.decodeResource(content.getResources(), resourceId, options);

        if(bitmap == null){
            if(LoggerConfig.ON){
                Log.w(TAG, "资源" + resourceId + "无法解码");
            }

            glDeleteTextures(1, textureObjectIds, 0);
            return 0;
        }

        GLES30.glBindTexture(GL_TEXTURE_2D, textureObjectIds[0]);
        GLES30.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        GLES30.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        GLUtils.texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
        GLES30.glGenerateMipmap(GL_TEXTURE_2D);
        GLES30.glBindTexture(GL_TEXTURE_2D, 0);
        return textureObjectIds[0];
    }

    /**
     * 创建立方体贴图对象
     * @param context Android上下文
     * @param cubeResource 纹理资源Id数组，顺序为镜头的左、右、下、上、前、后
     * @return 纹理对象指针
     */
    public static int loadCubeMap(Context context, int[] cubeResource){
        // 创建纹理对象
        final int[] textureObjectIds = new int[1];
        GLES30.glGenTextures(1, textureObjectIds, 0);
        if(textureObjectIds[0] == 0){
            if(LoggerConfig.ON){
                Log.w(TAG, "无法创建新的纹理对象");
            }
            return 0;
        }

        // 加载位图数据
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        final Bitmap[] cubeBitmaps = new Bitmap[6];
        for(int i = 0; i < 6; i++){
            cubeBitmaps[i] = BitmapFactory.decodeResource(context.getResources(), cubeResource[i], options);
            if(cubeBitmaps[i] == null){
                if(LoggerConfig.ON){
                    Log.w(TAG, "资源" + cubeResource[i] + "无法解码");
                }
                GLES30.glDeleteTextures(1, textureObjectIds, 0);
                return 0;
            }
        }

        // 纹理与textureObjectIds[0]做绑定
        GLES30.glBindTexture(GL_TEXTURE_CUBE_MAP, textureObjectIds[0]);

        // 纹理过滤，因为天空盒在视点内不会缩小，所以不用MIP贴图，只用常规的双线性过滤
        GLES30.glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        GLES30.glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        // 分别读取各个天空盒的贴图到textureObjectIds[0]
        int index = 0;
        texImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, cubeBitmaps[index++], 0);
        texImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0, cubeBitmaps[index++], 0);
        texImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, cubeBitmaps[index++], 0);
        texImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0, cubeBitmaps[index++], 0);
        texImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0, cubeBitmaps[index++], 0);
        texImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, cubeBitmaps[index++], 0);

        // 解除绑定
        glBindTexture(GL_TEXTURE_CUBE_MAP, 0);

        // 释放位图数据
        for(Bitmap bitmap: cubeBitmaps){
            bitmap.recycle();
        }

        return textureObjectIds[0];
    }

    /**
     * 使用位图数组创建立方体贴图对象
     * @param bitmapList 位图数组
     * @return 纹理对象指针
     */
    public static int loadCubeMapByBitmapList(List<Bitmap> bitmapList){
        // 创建纹理对象
        final int[] textureObjectIds = new int[1];
        GLES30.glGenTextures(1, textureObjectIds, 0);
        if(textureObjectIds[0] == 0){
            if(LoggerConfig.ON){
                Log.w(TAG, "无法创建新的纹理对象");
            }
            return 0;
        }

        // 纹理与textureObjectIds[0]做绑定
        GLES30.glBindTexture(GL_TEXTURE_CUBE_MAP, textureObjectIds[0]);

        // 纹理过滤，因为天空盒在视点内不会缩小，所以不用MIP贴图，只用常规的双线性过滤
        GLES30.glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        GLES30.glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        GLES30.glTexParameteri(GL_TEXTURE_CUBE_MAP, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE);
        GLES30.glTexParameteri(GL_TEXTURE_CUBE_MAP, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE);
        GLES30.glTexParameteri(GL_TEXTURE_CUBE_MAP, GLES30.GL_TEXTURE_WRAP_R, GLES30.GL_CLAMP_TO_EDGE);

        // 分别读取各个天空盒的贴图到textureObjectIds[0]
        int index = 0;
        texImage2D(GLES30.GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0, bitmapList.get(index++), 0);
        texImage2D(GLES30.GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, bitmapList.get(index++), 0);
        texImage2D(GLES30.GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0, bitmapList.get(index++), 0);
        texImage2D(GLES30.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, bitmapList.get(index++), 0);
        texImage2D(GLES30.GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0, bitmapList.get(index++), 0);
        texImage2D(GLES30.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, bitmapList.get(index), 0);

        // 解除绑定
        GLES30.glBindTexture(GL_TEXTURE_CUBE_MAP, 0);

        // 释放位图数据
        for(Bitmap bitmap: bitmapList){
            bitmap.recycle();
        }

        return textureObjectIds[0];
    }

    /**
     * 切割天空盒原图
     * @param context Android上下文
     * @param resourceId 天空盒原图资源Id
     * @return 天空盒位图数组
     */
    public static List<Bitmap> cubeMapSlice(Context context, int resourceId){
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inPremultiplied = false;
        final Bitmap sourceBitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);
        if(sourceBitmap == null){
            if(LoggerConfig.ON){
                Log.w(TAG, "资源" + resourceId + "无法解码");
            }
            return null;
        }

        List<Bitmap> cubeMapList = new ArrayList<>();
        int pieceWidth = sourceBitmap.getWidth() / 4;
        int pieceHeight = sourceBitmap.getHeight() / 3;
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 3; j++){
                int pieceX = i * pieceWidth;
                int pieceY = j * pieceHeight;
                cubeMapList.add(Bitmap.createBitmap(sourceBitmap, pieceX, pieceY, pieceWidth, pieceHeight));
            }
        }

        sourceBitmap.recycle();

        List<Bitmap> newCubeMapList = new ArrayList<>();
        newCubeMapList.add(cubeMapList.get(1));
        newCubeMapList.add(cubeMapList.get(7));
        newCubeMapList.add(cubeMapList.get(5));
        newCubeMapList.add(cubeMapList.get(3));
        newCubeMapList.add(cubeMapList.get(4));
        newCubeMapList.add(cubeMapList.get(10));

        return newCubeMapList;
    }

    public static List<Bitmap> cubeMapSlice(Context context, String path) throws AssetsNotFoundException{
        Bitmap sourceBitmap = createBitmap(context, path);
        if(sourceBitmap == null){
            throw new RuntimeException("资源" + path + "无法解码");
        }else {
            List<Bitmap> cubeMapList = new ArrayList<>();
            int pieceWidth = sourceBitmap.getWidth() / 4;
            int pieceHeight = sourceBitmap.getHeight() / 3;
            for(int i = 0; i < 4; i++){
                for(int j = 0; j < 3; j++){
                    int pieceX = i * pieceWidth;
                    int pieceY = j * pieceHeight;
                    cubeMapList.add(Bitmap.createBitmap(sourceBitmap, pieceX, pieceY, pieceWidth, pieceHeight));
                }
            }

            sourceBitmap.recycle();

            List<Bitmap> newCubeMapList = new ArrayList<>();
            newCubeMapList.add(cubeMapList.get(1));
            newCubeMapList.add(cubeMapList.get(7));
            newCubeMapList.add(cubeMapList.get(5));
            newCubeMapList.add(cubeMapList.get(3));
            newCubeMapList.add(cubeMapList.get(4));
            newCubeMapList.add(cubeMapList.get(10));

            return newCubeMapList;
        }
    }

    public static int loadTexture(Bitmap bitmap, int warping, int filter){
        if (null == bitmap){
            return 1;
        }
        int[] TEXs = new int[]{-1};
        GLES30.glGenTextures(TEXs.length, TEXs, 0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, TEXs[0]);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, warping);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, warping);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, filter);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, filter);
        GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bitmap, 0);
//        GLES30.glGenerateMipmap(GLES30.GL_TEXTURE_2D);
        bitmap.recycle();
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);
        return TEXs[0];
    }

    /**
     * 创建空纹理
     * @param width 宽
     * @param height 高
     * @param warping 包裹
     * @param isHDRI 是否属于.hdri格式
     * @return 纹理对象ID
     */
    public static int loadTexture(int width, int height, int warping, boolean isHDRI, boolean isLinear){
        int[] TEXs = new int[1];
        GLES30.glGenTextures(TEXs.length, TEXs, 0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, TEXs[0]);

        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, warping);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, warping);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, isLinear? GLES30.GL_LINEAR: GLES30.GL_NEAREST);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, isLinear? GLES30.GL_LINEAR: GLES30.GL_NEAREST);
        if (isHDRI){
            GLES30.glTexImage2D(GLES30.GL_TEXTURE_2D, 0, GLES30.GL_RGB16F, width, height, 0,
                    GLES30.GL_RGB, GLES30.GL_FLOAT, null);
        }else {
            GLES30.glTexImage2D(GLES30.GL_TEXTURE_2D, 0, GLES30.GL_RGBA, width, height, 0,
                    GLES30.GL_RGBA, GLES30.GL_UNSIGNED_BYTE, null);
        }

//        GLES30.glGenerateMipmap(GLES30.GL_TEXTURE_2D);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);
        return TEXs[0];
    }

    public static int createTexture(Context context, String path, int warping, int filter){
        return loadTexture(createBitmap(context, path), warping, filter);
    }

    public static int createTextureFromExternal(Context context, String filePath, int warping, int filter){
        return loadTexture(BitmapFactory.decodeFile(filePath), warping, filter);
    }

    public static int createTextureForFrameBuffer(int width, int height, int warping, boolean isHDR, boolean isLinear){
        return loadTexture(width, height, warping, isHDR, isLinear);
    }

    public static int createTextureForFrameBuffer(int width, int height, BufferParam bufferParam){
        int[] TEXs = new int[1];
        GLES30.glGenTextures(TEXs.length, TEXs, 0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, TEXs[0]);

        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, bufferParam.getTextureWrap());
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, bufferParam.getTextureWrap());
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, bufferParam.getTextureMinFilter());
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, bufferParam.getTextureMagFilter());
        GLES30.glTexImage2D(GLES30.GL_TEXTURE_2D, 0, bufferParam.getInternalFormat(), width, height, 0,
                bufferParam.getFormat(), bufferParam.getType(), null);
        if (bufferParam.getIsGenMipmap()) {
            GLES30.glGenerateMipmap(GLES30.GL_TEXTURE_2D);
        }
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);
        return TEXs[0];
    }

    public static int createCustomDataBuffer(int size, BufferParam bufferParam, FloatBuffer data){
        int[] TEXs = new int[1];
        GLES30.glGenTextures(TEXs.length, TEXs, 0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, TEXs[0]);

        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, bufferParam.getTextureWrap());
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, bufferParam.getTextureWrap());
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, bufferParam.getTextureMinFilter());
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, bufferParam.getTextureMagFilter());
        GLES30.glTexImage2D(GLES30.GL_TEXTURE_2D, 0, bufferParam.getInternalFormat(), size, size, 0,
                bufferParam.getFormat(), bufferParam.getType(), data);

        if (bufferParam.getIsGenMipmap()){
            GLES30.glGenerateMipmap(GLES30.GL_TEXTURE_2D);
        }
        return TEXs[0];
    }

    public static int createTextureForDepthMap(int width, int height){
        int[] TEXs = new int[1];
        GLES30.glGenTextures(TEXs.length, TEXs, 0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, TEXs[0]);

        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE);// 防止超出DepthMap外的片段出现阴影
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_NEAREST);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_NEAREST);
        GLES30.glTexImage2D(GL_TEXTURE_2D, 0, GLES30.GL_R16F, width, height, 0,
                GLES30.GL_RED, GLES30.GL_FLOAT, null);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);
        return TEXs[0];
    }

    public static Bitmap createBitmap(Context context, String path){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inPremultiplied = false;
        Bitmap bitmap = null;
        try {
            bitmap = ImageLoader.getBitmapInAssetsWithOptions(context, path, options);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static int createHDRImage(Context context, String path){
        try {
//            InputStream inputStream = context.getAssets().open(path);

            InputStream inputStream;
            if (path.startsWith("/")) {
                File file = new File(path);
                inputStream = new FileInputStream(file);
            }else {
                inputStream = context.getAssets().open(path);
            }

            HDRImage hdrImage = HDREncoder.readHDR(inputStream, true);
            float[] internalData = hdrImage.getInternalData();
            int width = hdrImage.getWidth();
            int height = hdrImage.getHeight();

            int[] hdrTexture = new int[1];
            GLES30.glGenTextures(hdrTexture.length, hdrTexture, 0);
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, hdrTexture[0]);
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE);
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE);
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR);// GL_LINEAR线性插值，生成的图片是自带模糊的
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);
            GLES30.glTexImage2D(GLES30.GL_TEXTURE_2D, 0, GLES30.GL_RGB32F, width, height, 0,// todo 一些HDRI需要更高位的float存储，不然例如太阳光经过HDR后变蓝色?
                    GLES30.GL_RGB, GLES30.GL_FLOAT, BufferHelper.createFloatBuffer(internalData));
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);

            return hdrTexture[0];
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int createTGATexture(Context context, String path){
        int texture = 0;
        try {
            InputStream inputStream = context.getAssets().open(path);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();

            int[] pixels = TGAReader.read(buffer, TGAReader.ABGR);
            int width = TGAReader.getWidth(buffer);
            int height = TGAReader.getHeight(buffer);

            int[] textures = new int[1];
            GLES30.glGenTextures(1, textures, 0);

            GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textures[0]);
            GLES30.glPixelStorei(GL_UNPACK_ALIGNMENT, 4);

            IntBuffer texBuffer = IntBuffer.wrap(pixels);
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE);
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE);
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR);
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);
            GLES30.glTexImage2D(GL_TEXTURE_2D, 0, GLES30.GL_RGBA, width, height, 0,
                    GLES30.GL_RGBA, GLES30.GL_UNSIGNED_BYTE, texBuffer);

            texture = textures[0];

        } catch (IOException e) {
            e.printStackTrace();
        }
        return texture;
    }
}
