package com.example.android_3d_loader.core.cubeMap;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLES30;


import com.example.android_3d_loader.core.util.BufferHelper;
import com.example.android_3d_loader.core.util.TextureHelper;

import java.nio.FloatBuffer;
import java.util.List;

import static com.example.android_3d_loader.core.model.Constants.BYTES_PER_FLOAT;

public class CubeMap {
    private static final String TAG = "CubeMap";
    private static final float[] vertices = new float[]{
            -1.0f,  1.0f, -1.0f,
            -1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f,  1.0f, -1.0f,
            -1.0f,  1.0f, -1.0f,

            -1.0f, -1.0f,  1.0f,
            -1.0f, -1.0f, -1.0f,
            -1.0f,  1.0f, -1.0f,
            -1.0f,  1.0f, -1.0f,
            -1.0f,  1.0f,  1.0f,
            -1.0f, -1.0f,  1.0f,

            1.0f, -1.0f, -1.0f,
            1.0f, -1.0f,  1.0f,
            1.0f,  1.0f,  1.0f,
            1.0f,  1.0f,  1.0f,
            1.0f,  1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,

            -1.0f, -1.0f,  1.0f,
            -1.0f,  1.0f,  1.0f,
            1.0f,  1.0f,  1.0f,
            1.0f,  1.0f,  1.0f,
            1.0f, -1.0f,  1.0f,
            -1.0f, -1.0f,  1.0f,

            -1.0f,  1.0f, -1.0f,
            1.0f,  1.0f, -1.0f,
            1.0f,  1.0f,  1.0f,
            1.0f,  1.0f,  1.0f,
            -1.0f,  1.0f,  1.0f,
            -1.0f,  1.0f, -1.0f,

            -1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f,  1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f,  1.0f,
            1.0f, -1.0f,  1.0f
    };
    private int[] VAO = new int[1];
    private int[] VBO = new int[1];
    private int tex;

    public CubeMap(List<Bitmap> bitmaps){
        tex = TextureHelper.loadCubeMapByBitmapList(bitmaps);
        setUp();
    }

    public CubeMap(int textureId){
        tex = textureId;
        setUp();
    }

    public void draw(){
        GLES30.glBindVertexArray(VAO[0]);
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_CUBE_MAP, tex);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0,
                vertices.length / 3);
        GLES30.glBindVertexArray(0);
    }

    private void setUp(){
        GLES30.glGenVertexArrays(VAO.length, VAO, 0);
        GLES30.glGenBuffers(VBO.length, VBO, 0);
        FloatBuffer vertexBuffers = BufferHelper.createFloatBuffer(vertices);

        GLES30.glBindVertexArray(VAO[0]);
        GLES30.glBindBuffer(GLES20.GL_ARRAY_BUFFER, VBO[0]);
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER,vertexBuffers.capacity() * BYTES_PER_FLOAT,
                vertexBuffers, GLES30.GL_STATIC_DRAW);
        GLES30.glVertexAttribPointer(0, 3,
                GLES30.GL_FLOAT,false, 3 * BYTES_PER_FLOAT,0);
        GLES30.glEnableVertexAttribArray(0);
        GLES30.glBindVertexArray(0);
    }

    public int getTexture() {
        return tex;
    }
}
