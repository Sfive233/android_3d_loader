package com.example.android_3d_loader.controller.process;

import android.content.Context;

import com.example.android_3d_loader.core.dataType.Boolean;
import com.example.android_3d_loader.core.material.BloomMaterial;

public class InitBloomProcess extends Process {

    public InitBloomProcess(Context context, OnProcessFinishedListener listener, Object... params) {
        super(context, listener, params);
    }

    @Override
    protected Object[] onProcess() {

        Boolean enableBloom;
        BloomMaterial bloomMaterial;

        if (glCommunicator.isSaveExist()){

            // bloom
            enableBloom = new Boolean(true);
            Boolean savedEnableBloom = (Boolean) glCommunicator.getData("EnableBloom");
            if (savedEnableBloom != null){
                enableBloom.setVal(savedEnableBloom.getVal());
            }
            glCommunicator.putData("EnableBloom", enableBloom);
            bloomMaterial = new BloomMaterial(getContext());

        }else {

            // Bloom
            enableBloom = new Boolean(true);
            glCommunicator.putData("EnableBloom", enableBloom);
            bloomMaterial = new BloomMaterial(getContext());
            glCommunicator.putData("BloomMaterial", bloomMaterial);

        }

        return new Object[]{
            enableBloom,
            bloomMaterial,
        };
    }
}
