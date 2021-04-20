package com.example.android_3d_loader.controller.process;

import android.content.Context;

import com.example.android_3d_loader.core.model.mesh.basicObject.AxisIndicator;
import com.example.android_3d_loader.core.camera.OrbitCamera;
import com.example.android_3d_loader.core.material.AxisMaterial;

public class InitCameraProcess extends Process {

    protected Integer SRC_WIDTH;
    protected Integer SRC_HEIGHT;

    public InitCameraProcess(Context context, OnProcessFinishedListener listener, Object... params) {
        super(context, listener, params);
        SRC_WIDTH = (Integer) params[0];
        SRC_HEIGHT = (Integer) params[1];
    }

    @Override
    protected Object[] onProcess() {

        OrbitCamera mainCamera;
        OrbitCamera axisCamera;
        AxisIndicator axisIndicator;
        AxisMaterial axisMaterial;

        //
        if (glCommunicator.isSaveExist()){
            mainCamera = (OrbitCamera) glCommunicator.getData("MainCamera");
            axisCamera = (OrbitCamera) glCommunicator.getData("AxisCamera");

            AxisIndicator savedAxisIndicator = (AxisIndicator) glCommunicator.getData("AxisIndicator");
            axisIndicator = new AxisIndicator();
            axisMaterial = new AxisMaterial(context);
            if (savedAxisIndicator != null){
                axisIndicator.setIsVisible(savedAxisIndicator.getIsVisible().getVal());
            }
            glCommunicator.putData("AxisIndicator", axisIndicator);
        }else {
            mainCamera = new OrbitCamera(
                    SRC_WIDTH, SRC_HEIGHT
            );
            axisCamera = new OrbitCamera(
                    3.0f, 3.0f, 3.0f,
                    SRC_WIDTH, SRC_HEIGHT
            );
            axisIndicator = new AxisIndicator();
            axisIndicator.setIsVisible(false);
            axisMaterial = new AxisMaterial(context);
            glCommunicator.putData("MainCamera", mainCamera);
            glCommunicator.putData("AxisCamera", axisCamera);
            glCommunicator.putData("AxisIndicator", axisIndicator);
        }

        return new Object[]{
                mainCamera,
                axisCamera,
                axisIndicator,
                axisMaterial
        };
    }
}
