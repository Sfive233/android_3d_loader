package com.example.android_3d_loader.core.camera;

import com.example.android_3d_loader.core.dataType.Matrix4;
import com.example.android_3d_loader.core.dataType.Vector3;
import com.google.gson.annotations.Expose;

public abstract class BaseCamera{
    @Expose
    protected Vector3 camPos = new Vector3(1.0f);
    @Expose
    protected Vector3 lookAtPos = new Vector3(0.0f);
    @Expose
    protected Vector3 upPos = new Vector3(0.0f, 1.0f, 0.0f);
    protected Matrix4 projectionMatrix = new Matrix4();
    protected Matrix4 viewMatrix = new Matrix4();

    public Vector3 getCamPos() {
        return camPos;
    }

    public Vector3 getCamFrontPos(){
        return lookAtPos;
    }

    public void setCamFrontPos(Vector3 camFrontPos){
        this.lookAtPos.setXYZ(camFrontPos.getOriginalFloats());
    }

    public void setCamPos(Vector3 camPos) {
        this.camPos.setXYZ(camPos.getOriginalFloats());
    }

    public Vector3 getLookAtPos() {
        return lookAtPos;
    }

    public void setLookAtPos(Vector3 lookAtPos) {
        this.lookAtPos.setXYZ(lookAtPos.getOriginalFloats());
    }

    public Vector3 getUpPos() {
        return upPos;
    }

    public void setUpPos(Vector3 upPos) {
        this.upPos.setXYZ(upPos.getOriginalFloats());
    }

    public Matrix4 getProjectionMatrix() {
        return projectionMatrix;
    }

    public void setProjectionMatrix(Matrix4 projectionMatrix) {
        this.projectionMatrix.setVal(projectionMatrix.getVal());
    }

    public Matrix4 getViewMatrix() {
        return viewMatrix;
    }

    public void setViewMatrix(Matrix4 viewMatrix) {
        this.viewMatrix.setVal(viewMatrix.getVal());
    }
}
