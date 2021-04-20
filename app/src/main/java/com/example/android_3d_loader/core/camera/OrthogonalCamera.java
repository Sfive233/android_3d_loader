package com.example.android_3d_loader.core.camera;

import android.opengl.Matrix;

import com.example.android_3d_loader.core.dataType.Int;
import com.example.android_3d_loader.core.dataType.Matrix4;
import com.example.android_3d_loader.core.dataType.Vector3;

public class OrthogonalCamera extends BaseCamera {

    protected float viewPortSize = 13.0f;
    protected float nearPlane = -1.0f;
    protected float farPlane = 17.0f;

    public OrthogonalCamera(){

    }

    public OrthogonalCamera(float camPosX, float camPosY, float camPosZ){
        this.camPos = new Vector3(camPosX, camPosY, camPosZ);
    }

    public Matrix4 getProjectionMatrix() {
        Matrix.setIdentityM(projectionMatrix.getVal(), 0);
        Matrix.orthoM(projectionMatrix.getVal(), 0, -viewPortSize, viewPortSize, -viewPortSize, viewPortSize, nearPlane, farPlane);
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

    public void setCameraCloseness(float viewPortSize){
        this.viewPortSize = viewPortSize;
    }
}
