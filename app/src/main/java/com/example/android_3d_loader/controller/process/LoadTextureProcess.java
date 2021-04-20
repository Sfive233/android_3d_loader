package com.example.android_3d_loader.controller.process;

import android.content.Context;

import com.example.android_3d_loader.core.material.PBRMaterial;
import com.example.android_3d_loader.core.material.TraditionalMaterial;
import com.example.android_3d_loader.core.model.mesh.Mesh;
import com.example.android_3d_loader.core.model.Model;

public class LoadTextureProcess extends Process {

    public LoadTextureProcess(Context context, OnProcessFinishedListener listener, Object... params) {
        super(context, listener, params);
    }

    @Override
    protected Object[] onProcess() {

        Model model = (Model) glCommunicator.getData("Model");

        for (Mesh mesh: model.getMeshes()){

            TraditionalMaterial traditionalMaterial = (TraditionalMaterial) glCommunicator.getData(mesh.getName() + "_shader_traditional");
            PBRMaterial pbrMaterial = (PBRMaterial) glCommunicator.getData(mesh.getName() + "_shader_physicalBased");
            if(traditionalMaterial.getDiffuseMap().isChanged()){
                traditionalMaterial.getDiffuseMap().reload(getContext());
            }
            if (traditionalMaterial.getSpecularMap().isChanged()){
                traditionalMaterial.getSpecularMap().reload(getContext());
            }
            if (traditionalMaterial.getNormalMap().isChanged()){
                traditionalMaterial.getNormalMap().reload(getContext());
            }
            if (traditionalMaterial.getHeightMap().isChanged()){
                traditionalMaterial.getHeightMap().reload(getContext());
            }
            if (pbrMaterial.getAlbedoMap().isChanged()){
                pbrMaterial.getAlbedoMap().reload(getContext());
            }
            if (pbrMaterial.getNormalMap().isChanged()){
                pbrMaterial.getMetallicMap().reload(getContext());
            }
            if (pbrMaterial.getNormalMap().isChanged()){
                pbrMaterial.getNormalMap().reload(getContext());
            }
            if (pbrMaterial.getRoughnessMap().isChanged()){
                pbrMaterial.getRoughnessMap().reload(getContext());
            }
            if (pbrMaterial.getHeightMap().isChanged()){
                pbrMaterial.getHeightMap().reload(getContext());
            }
            glCommunicator.putData(mesh.getName() + "_shader_traditional", traditionalMaterial);
            glCommunicator.putData(mesh.getName() + "_shader_physicalBased", pbrMaterial);
        }

        return new Object[0];
    }
}
