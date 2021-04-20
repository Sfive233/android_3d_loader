package com.example.android_3d_loader.core.model.mesh.basicObject;

import com.example.android_3d_loader.core.model.mesh.Mesh;

public class Cube extends Mesh {

    public static float[] vertices = new float[]{
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

    public static float[] texcoords = new float[]{
            0.0f, 0.0f, // Bottom-left
            1.0f, 1.0f, // top-right
            1.0f, 0.0f, // bottom-right
            1.0f, 1.0f, // top-right
            0.0f, 0.0f, // bottom-left
            0.0f, 1.0f, // top-left
            // Front face
            0.0f, 0.0f, // bottom-left
            1.0f, 0.0f, // bottom-right
            1.0f, 1.0f, // top-right
            1.0f, 1.0f, // top-right
            0.0f, 1.0f, // top-left
            0.0f, 0.0f, // bottom-left
            // Left face
            1.0f, 0.0f, // top-right
            1.0f, 1.0f, // top-left
            0.0f, 1.0f, // bottom-left
            0.0f, 1.0f, // bottom-left
            0.0f, 0.0f, // bottom-right
            1.0f, 0.0f, // top-right
            // Right face
            1.0f, 0.0f, // top-left
            0.0f, 1.0f, // bottom-right
            1.0f, 1.0f, // top-right
            0.0f, 1.0f, // bottom-right
            1.0f, 0.0f, // top-left
            0.0f, 0.0f, // bottom-left
            // Bottom face
            0.0f, 1.0f, // top-right
            1.0f, 1.0f, // top-left
            1.0f, 0.0f, // bottom-left
            1.0f, 0.0f, // bottom-left
            0.0f, 0.0f, // bottom-right
            0.0f, 1.0f, // top-right
            // Top face
            0.0f, 1.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            0.0f, 0.0f,
    };

    public static float[] normals = new float[0];

    public static int[] indices = new int[]{
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25,
            26, 27, 28, 29, 30, 31, 32, 33, 34, 35,
    };

    public Cube() {
        super("Cube", vertices, texcoords, normals, indices, TYPE_TRIANGLES, ArrayDataType.VERTEX);
    }
}
