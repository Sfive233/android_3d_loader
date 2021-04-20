package com.example.android_3d_loader.core.model.mesh.basicObject;

import com.example.android_3d_loader.core.model.mesh.Mesh;

public class AxisIndicator extends Mesh {

    private static float axisArrowSize = 0.1f;

    private static float[] vertices = new float[]{
            // center
             0.0f,  0.0f,  0.0f,

            // axis X
             1.0f,  axisArrowSize,  0.0f,
             1.0f,  0.0f, -axisArrowSize,
             1.0f, -axisArrowSize,  0.0f,
             1.0f,  0.0f,  axisArrowSize,

            // axis Y
            axisArrowSize,  1.0f,  0.0f,
             0.0f,  1.0f,  axisArrowSize,
            -axisArrowSize,  1.0f,  0.0f,
             0.0f,  1.0f, -axisArrowSize,

            // axis Z
            axisArrowSize,  0.0f,  1.0f,
             0.0f, -axisArrowSize,  1.0f,
            -axisArrowSize,  0.0f,  1.0f,
             0.0f,  axisArrowSize,  1.0f,

    };

    private static int[] indices = new int[]{
            // axis X (1 ~ 4)
            0, 1, 2,
            0, 2, 3,
            0, 3, 4,
            0, 4, 1,
            1, 2, 3,
            1, 3, 4,

            // axis Y (5 ~ 8)
            0, 5, 6,
            0, 6, 7,
            0, 7, 8,
            0, 8, 5,
            5, 6, 7,
            5, 7, 8,

            // axis Z (9 ~ 12)
            0, 9, 10,
            0, 10, 11,
            0, 11, 12,
            0, 12, 9,
            9, 10, 11,
            9, 11, 12,
    };

    public AxisIndicator(){
        super("Axis", vertices, null, null, indices, TYPE_TRIANGLES, ArrayDataType.VERTEX);
    }
}
