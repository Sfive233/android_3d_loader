package com.example.android_3d_loader.controller.renderPass;

import com.example.android_3d_loader.core.model.mesh.basicObject.Plane;
import com.example.android_3d_loader.core.cubeMap.CubeMapPreRenderer;
import com.example.android_3d_loader.core.cubeMap.Skybox;
import com.example.android_3d_loader.core.texture.buffer.DepthBuffer;
import com.example.android_3d_loader.core.light.DirectionLight;
import com.example.android_3d_loader.core.material.SkyboxMaterial;
import com.example.android_3d_loader.core.model.Model;
import com.example.android_3d_loader.controller.process.LoadSkyboxProcess;
import com.example.android_3d_loader.controller.process.Process;

public class SkyboxPass extends BaseRenderPass{

    protected Skybox skybox;
    private LoadSkyboxProcess loadSkyboxProcess;

    @Override
    public void renderInit() {
        loadSkyboxProcess = new LoadSkyboxProcess(
                getContext(),
                new Process.OnProcessFinishedListener() {
                    @Override
                    public void onFinish(Object... objects) {
                        skybox = (Skybox) objects[0];
                    }
                }
        );
    }

    @Override
    public void renderReact() {
        loadSkyboxProcess.doProcess();
    }

    @Override
    public Object[] renderUpdate(Object... inputs) {
        DepthBuffer shadowMap = (DepthBuffer) inputs[0];
        Model model = (Model) inputs[1];
        Plane plane = (Plane) inputs[2];
        DirectionLight directionLight = (DirectionLight) inputs[3];

        return new Object[]{
                shadowMap, model, plane, directionLight, skybox
        };
    }
}
