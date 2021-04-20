package com.example.android_3d_loader.core.cubeMap;

import com.example.android_3d_loader.core.model.mesh.basicObject.Cube;
import com.example.android_3d_loader.core.camera.BaseCamera;
import com.example.android_3d_loader.core.material.Material;

public class Skybox extends Cube {

    @Override
    public void setCurrentMaterial(Material currentMaterial) {
        super.setCurrentMaterial(currentMaterial);
    }

    public void draw(BaseCamera camera){
        super.draw(null, camera.getViewMatrix(), camera.getProjectionMatrix(), null);
    }
}
