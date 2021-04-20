package com.example.android_3d_loader.controller.process;

import android.content.Context;

import com.example.android_3d_loader.core.dataType.Boolean;
import com.example.android_3d_loader.core.material.toneMapping.ACESToneMappingMaterial;
import com.example.android_3d_loader.core.material.toneMapping.CEToneMappingMaterial;
import com.example.android_3d_loader.core.material.toneMapping.ToneMappingMaterial;

import java.util.ArrayList;
import java.util.List;

public class InitToneMappingProcess extends Process {

    protected ToneMappingMaterial toneMappingMaterial;
    protected CEToneMappingMaterial ceToneMappingMaterial;
    protected ACESToneMappingMaterial acesToneMappingMaterial;
    protected Boolean enableToneMapping;

    public InitToneMappingProcess(Context context, OnProcessFinishedListener listener, Object... params) {
        super(context, listener, params);
        toneMappingMaterial = (ToneMappingMaterial) params[0];
        ceToneMappingMaterial = (CEToneMappingMaterial) params[1];
        acesToneMappingMaterial = (ACESToneMappingMaterial) params[2];
        enableToneMapping = new Boolean();
    }

    @Override
    protected Object[] onProcess() {

        List<String> toneMappingList = new ArrayList<>();
        toneMappingList.add("Reinhard");
        toneMappingList.add("ACES");
        glCommunicator.putData("ToneMappingList", toneMappingList);

        if (glCommunicator.isSaveExist()){

            Boolean savedEnableToneMapping = (Boolean) glCommunicator.getData("EnableToneMapping");
            if (savedEnableToneMapping != null){
                enableToneMapping.setVal(savedEnableToneMapping.getVal());
            }
            glCommunicator.putData("EnableToneMapping", enableToneMapping);

            String savedCurrentToneMapping = (String) glCommunicator.getData("CurrentSelectedToneMapping");
            ToneMappingMaterial savedToneMappingMaterial = (ToneMappingMaterial) glCommunicator.getData("ToneMappingMaterial");
            if (savedCurrentToneMapping != null) {
                switch (savedCurrentToneMapping) {
                    case "Reinhard":
                        ceToneMappingMaterial.setExposure(savedToneMappingMaterial.getExposure().getVal());
                        toneMappingMaterial = ceToneMappingMaterial;
                        break;
                    case "ACES":
                        acesToneMappingMaterial.setExposure(savedToneMappingMaterial.getExposure().getVal());
                        toneMappingMaterial = acesToneMappingMaterial;
                        break;
                }
            }else {
                toneMappingMaterial = ceToneMappingMaterial;
                glCommunicator.putData("CurrentSelectedToneMapping", "Reinhard");
            }
            glCommunicator.putData("ToneMappingMaterial", toneMappingMaterial);
        }else {

            Boolean savedEnableToneMapping = (Boolean) glCommunicator.getData("EnableToneMapping");
            if (savedEnableToneMapping != null){
                enableToneMapping.setVal(savedEnableToneMapping.getVal());
            }
            glCommunicator.putData("EnableToneMapping", enableToneMapping);

            String currentSelectedToneMapping = (String) glCommunicator.getData("CurrentSelectedToneMapping");
            if (currentSelectedToneMapping != null) {
                switch (currentSelectedToneMapping) {
                    case "Reinhard":
                        toneMappingMaterial = ceToneMappingMaterial;
                        break;
                    case "ACES":
                        toneMappingMaterial = acesToneMappingMaterial;
                        break;
                }
            }else {
                toneMappingMaterial = ceToneMappingMaterial;
                glCommunicator.putData("CurrentSelectedToneMapping", "Reinhard");
            }
            glCommunicator.putData("ToneMappingMaterial", toneMappingMaterial);

            ToneMappingMaterial savedToneMappingMaterial = (ToneMappingMaterial) glCommunicator.getData("ToneMappingMaterial");
            if (savedToneMappingMaterial != null){
                toneMappingMaterial.setExposure(savedToneMappingMaterial.getExposure().getVal());
            }

        }

        return new Object[]{
                enableToneMapping,
                toneMappingMaterial
        };
    }
}
