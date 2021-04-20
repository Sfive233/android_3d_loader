package com.example.android_3d_loader.core.util;

import android.opengl.GLES30;

public class BufferObjectHelper {

    public static int createObject(){
        int[] temp = new int[1];
        GLES30.glGenVertexArrays(temp.length, temp, 0);
        return temp[0];
    }

    public static int[] createObject(int size){
        int[] temp = new int[size];
        GLES30.glGenVertexArrays(temp.length, temp, 0);
        return temp;
    }

}
