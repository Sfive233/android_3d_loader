package com.example.android_3d_loader.core.util;

import java.util.List;

public class ArrayListUtil {

    public static float[] arrayListToVertices(List<Geometry.Vector> arrayList){
        float[] vertices = new float[arrayList.size() * 3];
        int i = 0;
        for(Geometry.Vector vector: arrayList){
            vertices[i++] = vector.x;
            vertices[i++] = vector.y;
            vertices[i++] = vector.z;
        }

        return vertices;
    }
}
