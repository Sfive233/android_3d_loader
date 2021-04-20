package com.example.android_3d_loader.core.cubeMap;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.Matrix;

import com.example.android_3d_loader.core.EngineConstants;
import com.example.android_3d_loader.core.dataType.Matrix4;
import com.example.android_3d_loader.core.model.mesh.basicObject.Cube;
import com.example.android_3d_loader.core.texture.buffer.BufferParam;
import com.example.android_3d_loader.core.material.PBRCubeMapPreFilterMaterial;
import com.example.android_3d_loader.core.texture.textureCube.TextureCube;

import java.io.File;
import java.io.FileNotFoundException;

public class CubeMapPreRenderer extends Cube{

    protected Matrix4[] sixSideViewMatrix = new Matrix4[6];
    protected Matrix4 cubeMapProjectionMatrix = new Matrix4();
    protected TextureCube environmentMap;
    protected int mapSize = 1024;
    protected int internalFormat = GLES30.GL_RGB16F;
    protected int format = GLES30.GL_RGB;
    protected int type = GLES30.GL_FLOAT;
    protected int minFilter = GLES30.GL_LINEAR;
    protected int magFilter = GLES30.GL_LINEAR;
    protected boolean isGenMipmap = false;
    protected int maxMipLevels = 0;
    protected int frameBuffer;
    protected int renderBuffer;

    protected BufferParam bufferParam;

    protected Context context;
    protected String path;

    public CubeMapPreRenderer(Context context, String path) {
        this.context = context;
        this.path = path;
        if (path.startsWith("/")){
            File file = new File(path);
            if (!file.exists()){
                new FileNotFoundException("Assets \""+path+"\" not found, swapping to default assets.").printStackTrace();
                this.path = EngineConstants.DEFAULT_SKYBOX_PATH;
            }
        }
    }

    public CubeMapPreRenderer(Context context){
        this.context = context;
    }

    private void initMatrix(){
        float[] viewCoords = new float[]{
                1.0f,  0.0f,  0.0f, 0.0f, -1.0f,  0.0f,// back
                -1.0f,  0.0f,  0.0f, 0.0f, -1.0f,  0.0f,// front
                0.0f,  1.0f,  0.0f, 0.0f, 0.0f,  1.0f,// top
                0.0f,  -1.0f,  0.0f, 0.0f, 0.0f,  -1.0f,// down
                0.0f,  0.0f,  1.0f, 0.0f, -1.0f,  0.0f,// left
                0.0f,  0.0f,  -1.0f, 0.0f, -1.0f,  0.0f,// right
        };
        int j = 0;
        for (int i = 0; i < viewCoords.length; i+=6){
            sixSideViewMatrix[j] = new Matrix4();
            sixSideViewMatrix[j].setIdentity();
            Matrix.setLookAtM(sixSideViewMatrix[j].getVal(), 0,
                    0.0f, 0.0f, 0.0f,
                    viewCoords[i], viewCoords[i+1], viewCoords[i+2],
                    viewCoords[i+3], viewCoords[i+4],  viewCoords[i+5]);
            j++;
        }
        cubeMapProjectionMatrix.setIdentity();
        Matrix.perspectiveM(cubeMapProjectionMatrix.getVal(), 0, 90.0f, 1, 0.1f, 10.0f);
    }

    public void setBufferParam(BufferParam bufferParam){
        this.bufferParam = bufferParam;
        this.minFilter = bufferParam.getTextureMinFilter();
        this.magFilter = bufferParam.getTextureMagFilter();
        this.isGenMipmap = bufferParam.getIsGenMipmap();
        this.mapSize = bufferParam.getMapSize();
        this.internalFormat = bufferParam.getInternalFormat();
        this.format = bufferParam.getFormat();
        this.type = bufferParam.getType();
    }

    private void initEnvironmentMap(){
        int[] envMap = new int[1];
        GLES30.glGenTextures(envMap.length, envMap, 0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_CUBE_MAP, envMap[0]);
        for (int i = 0; i < 6; i++){
            GLES30.glTexImage2D(GLES30.GL_TEXTURE_CUBE_MAP_POSITIVE_X+i, 0, internalFormat, mapSize, mapSize, 0,
                    format, type, null);
        }
        this.environmentMap = new TextureCube("EnvMap", this.path, envMap[0]);

        GLES30.glTexParameteri(GLES30.GL_TEXTURE_CUBE_MAP, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_CUBE_MAP, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_CUBE_MAP, GLES30.GL_TEXTURE_WRAP_R, GLES30.GL_CLAMP_TO_EDGE);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_CUBE_MAP, GLES30.GL_TEXTURE_MIN_FILTER, minFilter);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_CUBE_MAP, GLES30.GL_TEXTURE_MAG_FILTER, magFilter);
        if (isGenMipmap){
            GLES30.glGenerateMipmap(GLES30.GL_TEXTURE_CUBE_MAP);
        }

        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);
    }

    private void initFrameBuffer(){
        int[] fbo = new int[1];
        int[] rbo = new int[1];
        GLES30.glGenFramebuffers(fbo.length, fbo, 0);
        GLES30.glGenRenderbuffers(rbo.length, rbo, 0);

        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, fbo[0]);
        GLES30.glBindRenderbuffer(GLES30.GL_RENDERBUFFER, rbo[0]);
        GLES30.glRenderbufferStorage(GLES30.GL_RENDERBUFFER, GLES30.GL_DEPTH_COMPONENT24,
                mapSize, mapSize);
        GLES30.glFramebufferRenderbuffer(GLES30.GL_FRAMEBUFFER, GLES30.GL_DEPTH_ATTACHMENT,
                GLES30.GL_RENDERBUFFER, rbo[0]);
        this.frameBuffer = fbo[0];
        this.renderBuffer = rbo[0];
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0);
    }

    private void convertHDRIToEnvironmentMap(){
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, frameBuffer);
        if(isGenMipmap && maxMipLevels > 1){
            for (int level = 0; level < maxMipLevels; level++){
                int mipSize = (int) (mapSize * Math.pow(0.5f, level));// 每缩放一级，分辨率缩小一半

                GLES30.glBindRenderbuffer(GLES30.GL_RENDERBUFFER, renderBuffer);
                GLES30.glRenderbufferStorage(GLES30.GL_RENDERBUFFER, GLES30.GL_DEPTH_COMPONENT24,
                        mipSize, mipSize);// 每个缩放等级使用不同的分辨率

                GLES30.glViewport(0, 0, mipSize, mipSize);
                GLES30.glEnable(GLES30.GL_DEPTH_TEST);

                if (currentMaterial instanceof PBRCubeMapPreFilterMaterial){
                    PBRCubeMapPreFilterMaterial pbrCubeMapPreFilterMaterial = (PBRCubeMapPreFilterMaterial) currentMaterial;
                    pbrCubeMapPreFilterMaterial.setRoughness((float)level / (float)(maxMipLevels - 1));
                    setCurrentMaterial(pbrCubeMapPreFilterMaterial);
                }

                for (int i = 0; i < 6; i++){
                    GLES30.glFramebufferTexture2D(GLES30.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
                            GLES30.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, environmentMap.getTex(), level);
                    GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);
                    this.draw(null, sixSideViewMatrix[i], cubeMapProjectionMatrix, null);
                }
            }
        }else {
            GLES30.glViewport(0, 0, mapSize, mapSize);
            GLES30.glEnable(GLES30.GL_DEPTH_TEST);
            for (int i = 0; i < 6; i++){
                GLES30.glFramebufferTexture2D(GLES30.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
                        GLES30.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, environmentMap.getTex(), 0);
                GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);
                this.draw(null, sixSideViewMatrix[i], cubeMapProjectionMatrix, null);
            }
        }
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0);
    }

    public TextureCube outPutMap() {

        initMatrix();
        initEnvironmentMap();
        initFrameBuffer();
        convertHDRIToEnvironmentMap();

        return environmentMap;
    }

    public void setMaxMipLevels(int maxMipLevels) {
        this.maxMipLevels = maxMipLevels;
    }
}
