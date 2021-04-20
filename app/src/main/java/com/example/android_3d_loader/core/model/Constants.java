package com.example.android_3d_loader.core.model;

public class Constants {
    public static final int BYTES_PER_SHORT = 2;
    public static final int BYTES_PER_FLOAT = 4;
    public static final int BYTES_PER_INT = 4;
    public static final int POSITION_SIZE = 3;
    public static final int TEXCOORD_SIZE = 2;
    public static final int NORMAL_SIZE = 3;
    public static final int TANGENT_SIZE = 3;
    public static final int BI_TANGENT_SIZE = 3;
    public static final int POSITION_STRIDE = POSITION_SIZE * BYTES_PER_FLOAT;
    public static final int TEXCOORD_STRIDE = TEXCOORD_SIZE * BYTES_PER_FLOAT;
    public static final int NORMAL_STRIDE = NORMAL_SIZE * BYTES_PER_FLOAT;
    public static final int TANGENT_STRIDE = TANGENT_SIZE * BYTES_PER_FLOAT;
    public static final int BI_TANGENT_STRIDE = BI_TANGENT_SIZE * BYTES_PER_FLOAT;

}
