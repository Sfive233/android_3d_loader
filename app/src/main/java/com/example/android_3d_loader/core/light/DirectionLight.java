package com.example.android_3d_loader.core.light;

import com.example.android_3d_loader.core.dataType.Float;
import com.example.android_3d_loader.core.dataType.Matrix4;
import com.example.android_3d_loader.core.dataType.Vector3;
import com.example.android_3d_loader.core.camera.OrthogonalCamera;
import com.google.gson.annotations.Expose;

public class DirectionLight extends OrthogonalCamera {
    private static final String TAG = "DirectionLight";
    @Expose
    protected Vector3 ambientLightColor = new Vector3(.3f, .3f, .3f);
    @Expose
    protected Vector3 diffuseLightColor = new Vector3(.7f, .7f, .7f);
    @Expose
    protected Vector3 specularLightColor = new Vector3(.3f, .3f, .3f);
    @Expose
    protected Float yaw = new Float(-60.0f);
    @Expose
    protected Float pitch = new Float(60.0f);

    public DirectionLight() {

    }

    public Vector3 getLightDirection(){

        Vector3 begin = camPos;
        Vector3 end = lookAtPos;
        Vector3 result = end.minus(begin);
        return result.normalize();
    }

    @Override
    public Matrix4 getViewMatrix() {
        camPos.x.setVal((float) Math.cos(Math.toRadians(yaw.getVal())) * (float) Math.cos(Math.toRadians(pitch.getVal())));
        camPos.y.setVal((float) Math.sin(Math.toRadians(pitch.getVal())));
        camPos.z.setVal(-(float) Math.sin(Math.toRadians(yaw.getVal())) * (float) Math.cos(Math.toRadians(pitch.getVal())));

        return super.getViewMatrix();
    }

    public Vector3 getAmbientLightColor(){
        return ambientLightColor;
    }

    public void setAmbientLightColor(float r, float g, float b){
        this.ambientLightColor.x.setVal(r);
        this.ambientLightColor.y.setVal(g);
        this.ambientLightColor.z.setVal(b);
    }

    public Vector3 getDiffuseLightColor() {
        return diffuseLightColor;
    }

    public void setDiffuseLightColor(float r, float g, float b) {
        this.diffuseLightColor.x.setVal(r);
        this.diffuseLightColor.y.setVal(g);
        this.diffuseLightColor.z.setVal(b);
    }

    public Vector3 getSpecularLightColor() {
        return specularLightColor;
    }

    public void setSpecularLightColor(float r, float g, float b) {
        this.specularLightColor.x.setVal(r);
        this.specularLightColor.y.setVal(g);
        this.specularLightColor.z.setVal(b);
    }

    public Float getYaw() {
        return yaw;
    }

    public Float getPitch() {
        return pitch;
    }

    public void setYaw(float yaw) {
        this.yaw.setVal(yaw);
    }

    public void setPitch(float pitch) {
        this.pitch.setVal(pitch);
    }
}
