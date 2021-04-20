package com.example.android_3d_loader.core.util;

public class MatrixHelper {

    /**
     * 透视矩阵
     * @param m
     * @param yFovInDegrees
     * @param aspect
     * @param n
     * @param f
     */
    public static void perspectiveM(float[] m, float yFovInDegrees, float aspect, float n, float f){
        /**
         * 将fov从角度值转为弧度值
         */
        final float angleInRadians = (float) (yFovInDegrees * Math.PI / 180.0);

        /**
         * fov转焦距
         */
        final float a = (float) (1.0 / Math.tan(angleInRadians / 2.0));

        /**
         * 输出矩阵
         */
        int i = 0;
        m[i++] = a / aspect;
        m[i++] = 0f;
        m[i++] = 0f;
        m[i++] = 0f;

        m[i++] = 0f;
        m[i++] = a;
        m[i++] = 0f;
        m[i++] = 0f;

        m[i++] = 0f;
        m[i++] = 0f;
        m[i++] = -((f + n) / (f - n));
        m[i++] = -1f;

        m[i++] = 0f;
        m[i++] = 0f;
        m[i++] = -((2f * f * n) / (f - n));
        m[i++] = 0f;
    }

    public static float[] mat4TakeMat3(float[] src){
        src[3] = 0;
        src[7] = 0;
        src[11] = 0;
        src[12] = 0;
        src[13] = 0;
        src[14] = 0;
        src[15] = 0;
        return src;
    }
}
