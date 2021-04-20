package com.example.android_3d_loader.controller.process;

import android.content.Context;

import com.example.android_3d_loader.core.material.Material;
import com.example.android_3d_loader.core.material.PBRMaterial;
import com.example.android_3d_loader.core.material.TraditionalMaterial;
import com.example.android_3d_loader.core.model.mesh.Mesh;
import com.example.android_3d_loader.core.model.Model;

import java.util.ArrayList;
import java.util.List;

public class InitMaterialProcess extends Process {

    protected TraditionalMaterial modelTraditionalMaterial;
    protected PBRMaterial modelPBRMaterial;
    
    public InitMaterialProcess(Context context, OnProcessFinishedListener listener, Object... params) {
        super(context, listener, params);
    }

    @Override
    protected Object[] onProcess() {

        List<String> shaderList = new ArrayList<>();
        shaderList.add("传统着色");
        shaderList.add("PBR着色");
        glCommunicator.putData("ShaderList", shaderList);

        Model model = (Model) glCommunicator.getData("Model");

        if (glCommunicator.isSaveExist()) {

            for (Mesh mesh : model.getMeshes()) {
                Material meshOwnMaterial = mesh.getOriginalMaterial();
                modelTraditionalMaterial = (TraditionalMaterial) glCommunicator.getData(mesh.getName() + "_shader_traditional");
                modelPBRMaterial = (PBRMaterial) glCommunicator.getData(mesh.getName() + "_shader_physicalBased");
                String meshCurrentSelectedShader = (String) glCommunicator.getData(mesh.getName() + "_currentSelectedShader");
                if (meshCurrentSelectedShader != null) {
                    switch (meshCurrentSelectedShader) {
                        case "传统着色":
                            TraditionalMaterial tempStdMaterial = new TraditionalMaterial(getContext());
                            tempStdMaterial.setIsUseDiffuseMap(modelTraditionalMaterial.getIsUseDiffuseMap().getVal());
                            tempStdMaterial.setDiffuseMap(modelTraditionalMaterial.getDiffuseMap());
                            tempStdMaterial.setObjectColor(modelTraditionalMaterial.getObjectColor().getOriginalFloats());
                            tempStdMaterial.setSpecularMap(modelTraditionalMaterial.getSpecularMap());
                            tempStdMaterial.setObjectSpecular(modelTraditionalMaterial.getObjectSpecular().getOriginalFloats());
//                            tempStdMaterial.setNormalMap(modelTraditionalMaterial.getNormalMap());
                            modelTraditionalMaterial = tempStdMaterial;
                            mesh.setCurrentMaterial(modelTraditionalMaterial);
                            break;
                        case "PBR着色":
                            PBRMaterial tempMaterial = new PBRMaterial(getContext());
                            tempMaterial.setIsUseAlbedoMap(modelPBRMaterial.getIsUseAlbedoMap().getVal());
                            tempMaterial.setAlbedoMap(modelPBRMaterial.getAlbedoMap());
                            tempMaterial.setAlbedoVal(modelPBRMaterial.getAlbedoVal().getOriginalFloats());
                            tempMaterial.setIsUseMetallicMap(modelPBRMaterial.getIsUseMetallicMap().getVal());
                            tempMaterial.setMetallicMap(modelPBRMaterial.getMetallicMap());
                            tempMaterial.setMetallicVal(modelPBRMaterial.getMetallicVal().getVal());
//                            tempMaterial.setIsHasNormal(modelPBRMaterial.getIsHasNormal().getVal());
//                            tempMaterial.setNormalMap(modelPBRMaterial.getNormalMap());
                            tempMaterial.setIsUseRoughnessMap(modelPBRMaterial.getIsUseRoughnessMap().getVal());
                            tempMaterial.setRoughnessMap(modelPBRMaterial.getRoughnessMap());
                            tempMaterial.setRoughnessVal(modelPBRMaterial.getRoughnessVal().getVal());
                            modelPBRMaterial = tempMaterial;
                            mesh.setCurrentMaterial(modelPBRMaterial);
                            break;
                    }
                } else {
                    modelTraditionalMaterial = new TraditionalMaterial(getContext());
                    modelPBRMaterial = new PBRMaterial(getContext());
                    modelTraditionalMaterial.setDiffuseMap(((TraditionalMaterial) meshOwnMaterial).getDiffuseMap());
                    modelTraditionalMaterial.setSpecularMap(((TraditionalMaterial) meshOwnMaterial).getSpecularMap());
                    modelTraditionalMaterial.setNormalMap(((TraditionalMaterial) meshOwnMaterial).getNormalMap());
                    glCommunicator.putData(mesh.getName() + "_currentSelectedShader", "传统着色");
                    mesh.setCurrentMaterial(modelTraditionalMaterial);
                }
                glCommunicator.putData(mesh.getName() + "_shader_traditional", modelTraditionalMaterial);
                glCommunicator.putData(mesh.getName() + "_shader_physicalBased", modelPBRMaterial);
            }
        } else {

            for (Mesh mesh : model.getMeshes()) {
                Material meshOwnMaterial = mesh.getOriginalMaterial();

                String meshCurrentSelectedShader = (String) glCommunicator.getData(mesh.getName() + "_currentSelectedShader");
                if (meshCurrentSelectedShader != null) {
                    switch (meshCurrentSelectedShader) {
                        case "传统着色":
                            mesh.setCurrentMaterial(modelTraditionalMaterial);
                            break;
                        case "PBR着色":
                            mesh.setCurrentMaterial(modelPBRMaterial);
                            break;
                    }
                } else {

                    modelTraditionalMaterial = new TraditionalMaterial(getContext());
                    modelPBRMaterial = new PBRMaterial(getContext());

                    modelTraditionalMaterial.setDiffuseMap(((TraditionalMaterial) meshOwnMaterial).getDiffuseMap());
                    modelTraditionalMaterial.setSpecularMap(((TraditionalMaterial) meshOwnMaterial).getSpecularMap());
                    modelTraditionalMaterial.setNormalMap(((TraditionalMaterial) meshOwnMaterial).getNormalMap());
                    glCommunicator.putData(mesh.getName() + "_currentSelectedShader", "传统着色");
                    mesh.setCurrentMaterial(modelTraditionalMaterial);
                }
                glCommunicator.putData(mesh.getName() + "_shader_traditional", modelTraditionalMaterial);
                glCommunicator.putData(mesh.getName() + "_shader_physicalBased", modelPBRMaterial);
            }
        }

        return null;
    }
}
