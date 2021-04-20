package com.example.android_3d_loader.core.model.mesh.basicObject;

import com.example.android_3d_loader.core.model.mesh.Mesh;

public class Plane extends Mesh {

    protected static int size = 3;
    protected static float[] vertices = new float[]{
             1.0f * size, 0.0f,  1.0f * size,
             1.0f * size, 0.0f, -1.0f * size,
            -1.0f * size, 0.0f, -1.0f * size,
            -1.0f * size, 0.0f,  1.0f * size,
    };
    protected static float[] uvs = new float[]{
            1.0f * size, 0.0f * size,
            1.0f * size, 1.0f * size,
            0.0f * size, 1.0f * size,
            0.0f * size, 0.0f * size
    };
    protected static float[] normals = new float[]{
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
    };
    protected static int[] indices = new int[]{
            0, 1, 2,
            0, 2, 3
    };

    public Plane() {
        super("Plane", vertices, uvs, normals, indices, TYPE_TRIANGLES);
    }

    public Plane(int size){
        super("Plane", new float[]{
                1.0f * size, 0.0f,  1.0f * size,
                1.0f * size, 0.0f, -1.0f * size,
                -1.0f * size, 0.0f, -1.0f * size,
                -1.0f * size, 0.0f,  1.0f * size,
        }, uvs = new float[]{
                1.0f * 2, 0.0f * 2,
                1.0f * 2, 1.0f * 2,
                0.0f * 2, 1.0f * 2,
                0.0f * 2, 0.0f * 2
        }, normals, indices, TYPE_TRIANGLES);

    }
}
