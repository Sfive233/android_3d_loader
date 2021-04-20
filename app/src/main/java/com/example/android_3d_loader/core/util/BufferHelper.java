package com.example.android_3d_loader.core.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.List;

import static com.example.android_3d_loader.core.EngineConstants.BYTES_PER_FLOAT;
import static com.example.android_3d_loader.core.EngineConstants.BYTES_PER_INT;
import static com.example.android_3d_loader.core.EngineConstants.BYTES_PER_SHORT;

public class BufferHelper {

    private static final String TAG = "BufferHelper";

    public static FloatBuffer createFloatBuffer(float[] data){
        FloatBuffer floatBuffer = ByteBuffer.allocateDirect(data.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        floatBuffer.put(data);
        floatBuffer.position(0);
        return floatBuffer;
    }

    public static IntBuffer createIntBuffer(int[] data){
        IntBuffer intBuffer = ByteBuffer.allocateDirect(data.length * BYTES_PER_INT)
                .order(ByteOrder.nativeOrder())
                .asIntBuffer();
        intBuffer.put(data);
        intBuffer.position(0);
        return intBuffer;
    }

    public static FloatBuffer createFloatBuffer(float[][] data){
        int lengthY = data.length;
        int lengthX = data[0].length;
        float[] oneDData = new float[lengthY * lengthX];
        for (int i = 0; i < lengthY; i++){
            for (int j = 0; j < lengthX; j++){
                oneDData[i * lengthX + j] = data[i][j];
            }
        }

        return createFloatBuffer(oneDData);
    }

    public static ShortBuffer createShortBuffer(short[] data){
        ShortBuffer shortBuffer = ByteBuffer.allocateDirect(data.length * BYTES_PER_SHORT)
                .order(ByteOrder.nativeOrder())
                .asShortBuffer();
        shortBuffer.put(data);
        shortBuffer.position(0);
        return shortBuffer;
    }

    public static FloatBuffer convertToFloatBuffer(List<Float> data){
        float[] d=new float[data.size()];
        for (int i=0;i<d.length;i++){
            d[i]=data.get(i);
        }

        ByteBuffer buffer=ByteBuffer.allocateDirect(data.size()*4);
        buffer.order(ByteOrder.nativeOrder());
        FloatBuffer ret=buffer.asFloatBuffer();
        ret.put(d);
        ret.position(0);
        return ret;
    }
}
