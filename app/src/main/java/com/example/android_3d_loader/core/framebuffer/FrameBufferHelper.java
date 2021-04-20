package com.example.android_3d_loader.core.framebuffer;

import android.opengl.GLES20;
import android.opengl.GLES30;


import com.example.android_3d_loader.core.util.BufferHelper;

import java.nio.IntBuffer;

public class FrameBufferHelper {

    /**
     * 创建帧缓冲，默认挂载了深度附件
     * @param width
     * @param height
     * @param emptyColorTexture 用于存储颜色信息的空纹理
     * @return
     */
    public static int createFrameBuffer(int width, int height, int emptyColorTexture){
        int[] fbos = new int[1];
        int[] rbos = new int[1];

        GLES30.glGenFramebuffers(fbos.length, fbos, 0);// 生成帧缓冲
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, fbos[0]);// 绑定

        GLES30.glFramebufferTexture2D(GLES30.GL_FRAMEBUFFER, GLES30.GL_COLOR_ATTACHMENT0,
                GLES30.GL_TEXTURE_2D, emptyColorTexture, 0);// 挂载颜色附件

        GLES30.glGenRenderbuffers(rbos.length, rbos, 0);// 生成渲染缓冲
        GLES30.glBindRenderbuffer(GLES30.GL_RENDERBUFFER, rbos[0]);// 绑定渲染缓冲
        GLES30.glRenderbufferStorage(GLES30.GL_RENDERBUFFER, GLES30.GL_DEPTH_COMPONENT16,
                width, height);// 生成深度附件
        GLES30.glFramebufferRenderbuffer(GLES30.GL_FRAMEBUFFER, GLES30.GL_DEPTH_ATTACHMENT,
                GLES30.GL_RENDERBUFFER, rbos[0]);// 挂载深度附件

        int status = GLES30.glCheckFramebufferStatus(GLES30.GL_FRAMEBUFFER);// 验证帧缓冲完整度
        if (status != GLES30.GL_FRAMEBUFFER_COMPLETE){
            throw new RuntimeException("Framebuffer is not complete: "+Integer.toHexString(status));
        }

        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0);// 结束绑定
        return fbos[0];
    }

    /**
     * 创建只挂载深度附件的帧缓冲
     * @param emptyTexture 用于存储深度信息的空纹理
     * @return
     */
    public static int createDepthMap(int width, int height, int emptyTexture){
        int[] fbos = new int[1];
        GLES30.glGenFramebuffers(fbos.length, fbos, 0);
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, fbos[0]);

        if (false){
            GLES30.glFramebufferTexture2D(GLES30.GL_FRAMEBUFFER, GLES30.GL_DEPTH_ATTACHMENT,
                    GLES30.GL_TEXTURE_2D, emptyTexture, 0);// 挂载深度缓冲

            IntBuffer intBuffer = BufferHelper.createIntBuffer(new int[]{GLES30.GL_NONE});
            GLES30.glDrawBuffers(1, intBuffer);// 告诉OpenGL不使用任何颜色数据
            GLES30.glReadBuffer(GLES30.GL_NONE);
        }else {
            GLES30.glFramebufferTexture2D(GLES30.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
                    GLES30.GL_TEXTURE_2D, emptyTexture, 0);

            int[] rbos = new int[1];
            // 生成并挂载深度附件
            GLES30.glGenRenderbuffers(rbos.length, rbos, 0);// 生成渲染缓冲
            GLES30.glBindRenderbuffer(GLES30.GL_RENDERBUFFER, rbos[0]);// 绑定渲染缓冲
            GLES30.glRenderbufferStorage(GLES30.GL_RENDERBUFFER, GLES30.GL_DEPTH_COMPONENT16,
                    width, height);// 生成深度附件
            GLES30.glFramebufferRenderbuffer(GLES30.GL_FRAMEBUFFER, GLES30.GL_DEPTH_ATTACHMENT,
                    GLES30.GL_RENDERBUFFER, rbos[0]);// 挂载深度附件
        }


        int status = GLES30.glCheckFramebufferStatus(GLES30.GL_FRAMEBUFFER);// 验证帧缓冲完整度
        if (status != GLES30.GL_FRAMEBUFFER_COMPLETE){
            throw new RuntimeException("Framebuffer is not complete: "+Integer.toHexString(status));
        }

        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0);// 结束绑定
        return fbos[0];
    }

    /**
     * 创建挂载多个颜色附件的帧缓冲，默认挂载了深度附件
     * @param width
     * @param height
     * @param emptyColorTextures 用于存储颜色信息的空纹理数组
     * @return
     */
    public static int createFrameBuffer(int width, int height, int[] emptyColorTextures){
        int[] fbos = new int[1];
        int[] rbos = new int[1];

        GLES30.glGenFramebuffers(fbos.length, fbos, 0);// 生成帧缓冲
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, fbos[0]);// 绑定

        // 挂载颜色附件
        for (int i = 0; i < emptyColorTextures.length; i++){
            GLES30.glFramebufferTexture2D(GLES30.GL_FRAMEBUFFER, GLES30.GL_COLOR_ATTACHMENT0+i,
                    GLES30.GL_TEXTURE_2D, emptyColorTextures[i], 0);
        }

        // 生成并挂载深度附件
        GLES30.glGenRenderbuffers(rbos.length, rbos, 0);// 生成渲染缓冲
        GLES30.glBindRenderbuffer(GLES30.GL_RENDERBUFFER, rbos[0]);// 绑定渲染缓冲
        GLES30.glRenderbufferStorage(GLES30.GL_RENDERBUFFER, GLES30.GL_DEPTH_COMPONENT16,
                width, height);// 生成深度附件
        GLES30.glFramebufferRenderbuffer(GLES30.GL_FRAMEBUFFER, GLES30.GL_DEPTH_ATTACHMENT,
                GLES30.GL_RENDERBUFFER, rbos[0]);// 挂载深度附件

        //告诉OpenGL这个帧缓存要用到哪些颜色附件
        int[] colorAttachment = new int[emptyColorTextures.length];
        for (int i = 0; i < emptyColorTextures.length; i++){
            colorAttachment[i] = GLES30.GL_COLOR_ATTACHMENT0+i;
        }
        IntBuffer intBuffer = BufferHelper.createIntBuffer(colorAttachment);
        GLES30.glDrawBuffers(emptyColorTextures.length, intBuffer);

        // 验证帧缓冲完整度
        checkStatus();

        // 结束绑定
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0);
        return fbos[0];
    }

    /**
     * 检验帧缓冲完整性
     */
    private static void checkStatus(){
        int status = GLES30.glCheckFramebufferStatus(GLES30.GL_FRAMEBUFFER);
        if (status != GLES30.GL_FRAMEBUFFER_COMPLETE){
            throw new RuntimeException("Framebuffer is not complete: "+Integer.toHexString(status));
        }
    }
}
