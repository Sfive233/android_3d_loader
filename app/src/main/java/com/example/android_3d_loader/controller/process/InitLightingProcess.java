package com.example.android_3d_loader.controller.process;

import android.content.Context;

import com.example.android_3d_loader.core.dataType.Boolean;
import com.example.android_3d_loader.core.light.DirectionLight;

public class InitLightingProcess extends Process {

    public InitLightingProcess(Context context, OnProcessFinishedListener listener, Object... params) {
        super(context, listener, params);
    }

    @Override
    protected Object[] onProcess() {

        DirectionLight directionLight;
        Boolean enableShadow;

        if (glCommunicator.isSaveExist()){
            // lighting
            directionLight = new DirectionLight();
            DirectionLight savedDirectionLight = (DirectionLight) glCommunicator.getData("DirectionLight");
            if (savedDirectionLight != null){
                directionLight = savedDirectionLight;
            }
//            directionLight.setCameraCloseness(model.getLongestSide() * 1.1f);
            glCommunicator.putData("DirectionLight", directionLight);

            // shadow
            enableShadow = new Boolean(true);
            Boolean savedEnableShadow = (Boolean) glCommunicator.getData("EnableShadow");
            if (savedEnableShadow != null){
                enableShadow.setVal(savedEnableShadow.getVal());
            }
            glCommunicator.putData("EnableShadow", enableShadow);
        }else {
            // Shadow
            enableShadow = new Boolean(true);
            glCommunicator.putData("EnableShadow", enableShadow);

            // Lighting
            directionLight = new DirectionLight();
            directionLight.setDiffuseLightColor(1.0f, 1.0f, 1.0f);
//            directionLight.setCameraCloseness(model.getLongestSide() * 1.1f);
            glCommunicator.putData("DirectionLight", directionLight);
        }
        return new Object[]{
                directionLight,
                enableShadow
        };
    }
}
