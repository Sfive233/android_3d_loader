package com.example.android_3d_loader.core.dataType;

import android.opengl.Matrix;

import com.example.android_3d_loader.core.material.Material;

public class Matrix4 {
    private float[] val = new float[16];

    public Matrix4(float[] val) {
        this.val = val;
    }

    public Matrix4() {
        Matrix.setIdentityM(val, 0);
    }

    public void setIdentity(){
        Matrix.setIdentityM(val, 0);
    }

    public void scale(float scale){
        Matrix.scaleM(val, 0, scale, scale, scale);
    }

    public void transform(float x, float y, float z){
        Matrix.translateM(val, 0, x, y, z);
    }

    public void transform(Vector3 vector3){
        Matrix.translateM(val, 0, vector3.x.getVal(), vector3.y.getVal(), vector3.z.getVal());
    }

    public void rotate(Vector3 vector3){
        Matrix.rotateM(val, 0, vector3.x.getVal(), 1.0f, 0.0f, 0.0f);
        Matrix.rotateM(val, 0, vector3.y.getVal(), 0.0f, 1.0f, 0.0f);
        Matrix.rotateM(val, 0, vector3.z.getVal(), 0.0f, 0.0f, 1.0f);
    }

    public void rotate(float x, float y, float z){
        Matrix.rotateM(val, 0, x, 1.0f, 0.0f, 0.0f);
        Matrix.rotateM(val, 0, y, 0.0f, 1.0f, 0.0f);
        Matrix.rotateM(val, 0, z, 0.0f, 0.0f, 1.0f);
    }

    public void setVal(float[] val){
        this.val = val;
    }

    public float[] getVal(){
        return val;
    }
}
