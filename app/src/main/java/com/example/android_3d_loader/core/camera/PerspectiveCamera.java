package com.example.android_3d_loader.core.camera;

import android.opengl.Matrix;

import com.example.android_3d_loader.core.dataType.Float;
import com.example.android_3d_loader.core.dataType.Matrix4;
import com.example.android_3d_loader.core.dataType.Vector3;
import com.google.gson.annotations.Expose;

public class PerspectiveCamera extends BaseCamera {

    @Expose
    protected Float aspectWidth = new Float(1280);
    @Expose
    protected Float aspectHeight = new Float(720);
    @Expose
    protected Float fov = new Float(45.0f);
    protected Float near = new Float(0.1f);
    protected Float far = new Float(100000.0f);

    public PerspectiveCamera() {
    }

    public PerspectiveCamera(int aspectWidth, int aspectHeight){
//        this.camPos = new Vector3(camPosX, camPosY, camPosZ);
        this.aspectWidth.setVal(aspectWidth);
        this.aspectHeight.setVal(aspectHeight);
    }

    public PerspectiveCamera(float camPosX, float camPosY, float camPosZ, int aspectWidth, int aspectHeight){
        this.camPos = new Vector3(camPosX, camPosY, camPosZ);
        this.aspectWidth.setVal(aspectWidth);
        this.aspectHeight.setVal(aspectHeight);
    }

    public Matrix4 getProjectionMatrix() {
        Matrix.setIdentityM(projectionMatrix.getVal(), 0);
        Matrix.perspectiveM(projectionMatrix.getVal(), 0,
                fov.getVal(),
                aspectWidth.getVal() / aspectHeight.getVal(),
                near.getVal(), far.getVal()
        );
        return projectionMatrix;
    }

    public Matrix4 getViewMatrix() {
        Matrix.setIdentityM(viewMatrix.getVal(), 0);
        Matrix.setLookAtM(viewMatrix.getVal(), 0,
                camPos.x.getVal(), camPos.y.getVal(), camPos.z.getVal(),
                lookAtPos.x.getVal(), lookAtPos.y.getVal(), lookAtPos.z.getVal(),
                upPos.x.getVal(), upPos.y.getVal(), upPos.z.getVal());
        return viewMatrix;
    }

    public float getFar() {
        return far.getVal();
    }

    public void setFar(float far) {
        this.far.setVal(far);
    }

    public Float getFov() {
        return fov;
    }

    public void setFov(float val){
        fov.setVal(val);
    }
}
