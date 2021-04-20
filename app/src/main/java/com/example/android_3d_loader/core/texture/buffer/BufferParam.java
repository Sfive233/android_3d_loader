package com.example.android_3d_loader.core.texture.buffer;

import android.opengl.GLES30;

public class BufferParam {
    public enum BufferComponent{
        RGB,
        RGBA,
        R,
        RG
    }
    public enum BufferBit{
        BIT_8,
        BIT_16,
        BIT_32
    }
    public enum BufferType{
        FLOAT,
        UNSIGNED_INT,
    }
    public enum TextureWrap{
        CLAMP_TO_EDGE,
        REPEAT,
    }
    public enum TextureFilter{
        LINEAR,
        NEAREST,
        LINEAR_MIP_MAP_LINEAR,
        LINEAR_MIP_MAP_NEAREST,
        NEAREST_MIP_MAP_NEAREST,
        NEAREST_MIP_MAP_LINEAR
    }
    private TextureWrap textureWrap = TextureWrap.CLAMP_TO_EDGE;
    private TextureFilter textureMagFilter = TextureFilter.NEAREST;
    private TextureFilter textureMinFilter = TextureFilter.NEAREST;
    private BufferComponent bufferComponent = BufferComponent.RGBA;
    private BufferBit bufferBit = BufferBit.BIT_8;
    private BufferType bufferType = BufferType.UNSIGNED_INT;
    private boolean isGenMipmap = false;
    private int maxMipmapLevel = 0;
    private int mapSize = 1024;

    public BufferParam(){

    }

    public BufferParam(BufferComponent bufferComponent, BufferBit bufferBit, BufferType bufferType){
        this.bufferComponent = bufferComponent;
        this.bufferBit = bufferBit;
        this.bufferType = bufferType;
    }

    public int getTextureWrap(){
        switch (textureWrap){
            case REPEAT:
                return GLES30.GL_REPEAT;
            case CLAMP_TO_EDGE:
                return GLES30.GL_CLAMP_TO_EDGE;
            default:
                return GLES30.GL_CLAMP_TO_EDGE;
        }
    }

    public int getTextureMagFilter(){
        switch (textureMagFilter){
            case LINEAR:
                return GLES30.GL_LINEAR;
            case NEAREST:
                return GLES30.GL_NEAREST;
            case LINEAR_MIP_MAP_LINEAR:
                return GLES30.GL_LINEAR_MIPMAP_LINEAR;
            case LINEAR_MIP_MAP_NEAREST:
                return GLES30.GL_LINEAR_MIPMAP_NEAREST;
            case NEAREST_MIP_MAP_LINEAR:
                return GLES30.GL_NEAREST_MIPMAP_LINEAR;
            case NEAREST_MIP_MAP_NEAREST:
                return GLES30.GL_NEAREST_MIPMAP_NEAREST;
            default:
                return GLES30.GL_NEAREST;
        }
    }

    public int getTextureMinFilter(){
        switch (textureMinFilter){
            case LINEAR:
                return GLES30.GL_LINEAR;
            case NEAREST:
                return GLES30.GL_NEAREST;
            case LINEAR_MIP_MAP_LINEAR:
                return GLES30.GL_LINEAR_MIPMAP_LINEAR;
            case LINEAR_MIP_MAP_NEAREST:
                return GLES30.GL_LINEAR_MIPMAP_NEAREST;
            case NEAREST_MIP_MAP_LINEAR:
                return GLES30.GL_NEAREST_MIPMAP_LINEAR;
            case NEAREST_MIP_MAP_NEAREST:
                return GLES30.GL_NEAREST_MIPMAP_NEAREST;
            default:
                return GLES30.GL_NEAREST;
        }
    }

    public int getInternalFormat(){
        switch (bufferComponent){
            case R:
                switch (bufferBit){
                    case BIT_8:
                        switch (bufferType){
                            case FLOAT:
                                return GLES30.GL_R8;
                            case UNSIGNED_INT:
                                return GLES30.GL_R8UI;
                        }
                    case BIT_16:
                        switch (bufferType){
                            case FLOAT:
                                return GLES30.GL_R16F;
                            case UNSIGNED_INT:
                                return GLES30.GL_R16UI;
                        }
                    case BIT_32:
                        switch (bufferType){
                            case FLOAT:
                                return GLES30.GL_R32F;
                            case UNSIGNED_INT:
                                return GLES30.GL_R32UI;
                        }
                }
            case RG:
                switch (bufferBit){
                    case BIT_16:
                        switch (bufferType){
                            case FLOAT:
                                return GLES30.GL_RG16F;
                        }
                }
            case RGB:
                switch (bufferBit){
                    case BIT_8:
                        switch (bufferType){
                            case FLOAT:
                                return GLES30.GL_RGB8;
                            case UNSIGNED_INT:
                                return GLES30.GL_RGB;
                        }
                    case BIT_16:
                        switch (bufferType){
                            case FLOAT:
                                return GLES30.GL_RGB16F;
                            case UNSIGNED_INT:
                                return GLES30.GL_RGB16UI;
                        }
                    case BIT_32:
                        switch (bufferType){
                            case FLOAT:
                                return GLES30.GL_RGB32F;
                            case UNSIGNED_INT:
                                return GLES30.GL_RGB32UI;
                        }
                }
            case RGBA:
                switch (bufferBit){
                    case BIT_8:
                        switch (bufferType){
//                            case FLOAT:
//                                return GLES30.GL_RGBA8;
//                            case UNSIGNED_INT:
//                                return GLES30.GL_RGBA8;
                            default:
                                return GLES30.GL_RGBA;
                        }
                    case BIT_16:
                        switch (bufferType){
                            case FLOAT:
                                return GLES30.GL_RGBA16F;
                            case UNSIGNED_INT:
                                return GLES30.GL_RGBA16UI;
                        }
                    case BIT_32:
                        switch (bufferType){
                            case FLOAT:
                                return GLES30.GL_RGBA32F;
                            case UNSIGNED_INT:
                                return GLES30.GL_RGBA32UI;
                        }
                }
            default:
                return GLES30.GL_RGBA8;
        }
    }

    public int getFormat(){
        switch (bufferComponent){
            case R:
                return GLES30.GL_RED;
            case RG:
                return GLES30.GL_RG;
            case RGB:
                return GLES30.GL_RGB;
            case RGBA:
                return GLES30.GL_RGBA;
            default:
                return GLES30.GL_RGBA;
        }
    }

    public int getType(){
        switch (bufferType){
            case FLOAT:
                return GLES30.GL_FLOAT;
            case UNSIGNED_INT:
                return GLES30.GL_UNSIGNED_INT;
            default:
                return GLES30.GL_UNSIGNED_INT;
        }
    }

    public boolean getIsGenMipmap(){
        return isGenMipmap;
    }

    public int getMaxMipmapLevel() {
        return maxMipmapLevel;
    }

    public void setMaxMipmapLevel(int maxMipmapLevel) {
        this.maxMipmapLevel = maxMipmapLevel;
    }

    public void setTextureWrap(TextureWrap textureWrap) {
        this.textureWrap = textureWrap;
    }

    public void setTextureMagFilter(TextureFilter textureMagFilter) {
        this.textureMagFilter = textureMagFilter;
    }

    public void setTextureMinFilter(TextureFilter textureMinFilter) {
        this.textureMinFilter = textureMinFilter;
    }

    public void setBufferComponent(BufferComponent bufferComponent) {
        this.bufferComponent = bufferComponent;
    }

    public void setBufferBit(BufferBit bufferBit) {
        this.bufferBit = bufferBit;
    }

    public void setBufferType(BufferType bufferType) {
        this.bufferType = bufferType;
    }

    public void setGenMipmap(boolean genMipmap) {
        isGenMipmap = genMipmap;
    }

    public int getMapSize() {
        return mapSize;
    }

    public void setMapSize(int mapSize) {
        this.mapSize = mapSize;
    }
}
