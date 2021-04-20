package com.example.android_3d_loader.core.camera;

import android.opengl.Matrix;

import androidx.core.math.MathUtils;

import com.example.android_3d_loader.core.dataType.Float;
import com.example.android_3d_loader.core.dataType.Matrix4;
import com.example.android_3d_loader.core.dataType.Vector3;
import com.google.gson.annotations.Expose;

public class OrbitCamera extends PerspectiveCamera{
    private static String TAG = "OrbitCamera";
    @Expose
    private Float yaw = new Float(-45f);// 水平的角度
    @Expose
    private Float pitch = new Float(30f);// y的角度 90度正上 0度中间 -90度正下
    private Matrix4 mView = new Matrix4();
    private Matrix4 mProj = new Matrix4();

    private Float dragHorizontal = new Float();
    private Float dragVertical = new Float();

    private Float lastDragHorizontal = new Float();
    private Float lastDragVertical = new Float();
    @Expose
    private Float zoom = new Float(3.0f);

    public OrbitCamera() {
    }

    public OrbitCamera(int resW, int resH){
        super(resW, resH);
        float defaultDistance = Vector3.distanceBetween(camPos, lookAtPos);
        zoom.setVal(defaultDistance);
        zoom.getVal();
    }

    public OrbitCamera(float x, float y, float z, int resW, int resH){
        super(x, y, z, resW, resH);

        float defaultDistance = Vector3.distanceBetween(camPos, lookAtPos);
        zoom.setVal(defaultDistance);
        zoom.getVal();
    }

    public void handleRotate(float xOffset, float yOffset){
        yaw.setVal(yaw.getVal() + xOffset / 16f);
        pitch.setVal(pitch.getVal() + yOffset / 16f);

        pitch.setVal(MathUtils.clamp(pitch.getVal(), -89.0f, 89.0f));
        if (yaw.getVal() > 180.0f){
            yaw.setVal(yaw.getVal() - 360.0f);
        }
        if (yaw.getVal() < -180.0f){
            yaw.setVal(360.0f + yaw.getVal());
        }

    }
    private void updatePanning(){
        if (lastDragHorizontal.getVal() != dragHorizontal.getVal()) {
            float tempFrontX = (float) Math.sin(Math.toRadians(180 + yaw.getVal())) * (dragHorizontal.getVal() - lastDragHorizontal.getVal());
            float tempFrontZ = (float) Math.cos(Math.toRadians(180 + yaw.getVal())) * (dragHorizontal.getVal() - lastDragHorizontal.getVal());
            lookAtPos.x.plusEqual(tempFrontX);
            lookAtPos.z.plusEqual(tempFrontZ);
            lastDragHorizontal.setVal(dragHorizontal.getVal());
        }

        if (lastDragVertical.getVal() != dragVertical.getVal()){
            float tempFrontX = (float) Math.cos(Math.toRadians(180 + yaw.getVal())) * (float) Math.sin(Math.toRadians(pitch.getVal())) * (dragVertical.getVal() - lastDragVertical.getVal());
            float tempFrontY = (float) Math.cos(Math.toRadians(pitch.getVal())) * (dragVertical.getVal() - lastDragVertical.getVal());
            float tempFrontZ = -(float) Math.sin(Math.toRadians(180 + yaw.getVal())) * (float) Math.sin(Math.toRadians(pitch.getVal())) * (dragVertical.getVal() - lastDragVertical.getVal());
            lookAtPos.x.plusEqual(tempFrontX);
            lookAtPos.y.plusEqual(tempFrontY);
            lookAtPos.z.plusEqual(tempFrontZ);
            lastDragVertical.setVal(dragVertical.getVal());
        }
    }

    private void updateLook(){
        camPos.x.setVal(lookAtPos.x.getVal() + (float) Math.cos(Math.toRadians(yaw.getVal())) * (float) Math.cos(Math.toRadians(pitch.getVal())) * zoom.getVal());
        camPos.y.setVal(lookAtPos.y.getVal() + (float) Math.sin(Math.toRadians(pitch.getVal())) * zoom.getVal());
        camPos.z.setVal(lookAtPos.z.getVal() - (float) Math.sin(Math.toRadians(yaw.getVal())) * (float) Math.cos(Math.toRadians(pitch.getVal())) * zoom.getVal());
    }

    public Matrix4 getViewMatrix(){

        updatePanning();
        updateLook();

        mView.setIdentity();
        Matrix.setLookAtM(mView.getVal(), 0,
                camPos.x.getVal(),
                camPos.y.getVal(),
                camPos.z.getVal(),
                lookAtPos.x.getVal(),
                lookAtPos.y.getVal(),
                lookAtPos.z.getVal(),
                upPos.x.getVal() , upPos.y.getVal(), upPos.z.getVal());
        return mView;
    }

    public Matrix4 getProjectionMatrix(){
        mProj.setIdentity();
        Matrix.perspectiveM(mProj.getVal(), 0,
                fov.getVal(),
                aspectWidth.getVal() / aspectHeight.getVal(),
                near.getVal(), far.getVal()
        );
        return mProj;
    }

    public Float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw.setVal(yaw);
    }

    public void handleDragHorizontal(float h){
        dragHorizontal.setVal(dragHorizontal.getVal() - h / 200.0f);
    }

    public void handleDragVertical(float v){
        dragVertical.setVal(dragVertical.getVal() + v / 200.0f);
    }

    public Float getZoom() {
        return zoom;
    }

    public void handleZoom(float d){
        this.zoom.setVal(this.zoom.getVal() / d);
    }
}
