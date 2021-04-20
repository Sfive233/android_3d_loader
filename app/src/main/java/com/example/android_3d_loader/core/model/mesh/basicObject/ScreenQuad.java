package com.example.android_3d_loader.core.model.mesh.basicObject;

import com.example.android_3d_loader.core.model.mesh.Mesh;

public class ScreenQuad extends Mesh {

    private static float[] vertices = new float[]{
            1.0f, 1.0f, 0.0f,   // 1
            -1.0f, 1.0f, 0.0f,  // 2
            -1.0f, -1.0f, 0.0f, // 3
            1.0f, -1.0f, 0.0f,  // 4
    };
    private static int[] indices = new int[]{
            0, 1, 2,
            0, 2, 3
    };

    public ScreenQuad() {
        super("ScreenQuad", vertices, null, null, indices, TYPE_TRIANGLES, ArrayDataType.VERTEX);
    }
}
