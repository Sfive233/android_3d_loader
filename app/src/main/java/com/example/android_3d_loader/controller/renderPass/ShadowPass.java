package com.example.android_3d_loader.controller.renderPass;

import com.example.android_3d_loader.core.model.mesh.basicObject.Plane;
import com.example.android_3d_loader.core.dataType.Boolean;
import com.example.android_3d_loader.core.texture.buffer.DepthBuffer;
import com.example.android_3d_loader.core.framebuffer.FrameBuffer;
import com.example.android_3d_loader.core.light.DirectionLight;
import com.example.android_3d_loader.core.material.Material;
import com.example.android_3d_loader.core.material.ShadowMaterial;
import com.example.android_3d_loader.core.model.mesh.Mesh;
import com.example.android_3d_loader.core.model.Model;
import com.example.android_3d_loader.controller.process.InitLightingProcess;
import com.example.android_3d_loader.controller.process.Process;

public class ShadowPass extends BaseRenderPass{

    private ShadowMaterial shadowMaterial;
    private DepthBuffer shadowDepthBuffer;
    private FrameBuffer shadowFramebuffer;
    private DirectionLight directionLight;

    private InitLightingProcess initLightingProcess;

    private Boolean enableShadow;

//    public ShadowPass(Context context) {
//        super(context);
//    }

    @Override
    public void renderInit() {

        initLightingProcess = new InitLightingProcess(
                getContext(),
                new Process.OnProcessFinishedListener() {
                    @Override
                    public void onFinish(Object... objects) {
                        directionLight = (DirectionLight) objects[0];
                        enableShadow = (Boolean) objects[1];
                    }
                }
        );

        shadowMaterial = new ShadowMaterial(getContext());
        shadowDepthBuffer = new DepthBuffer(512);
        shadowFramebuffer = new FrameBuffer(shadowDepthBuffer);
    }

    @Override
    public void renderReact() {
        initLightingProcess.doProcess();
    }

    @Override
    public Object[] renderUpdate(Object... inputs) {

        Model model = (Model) inputs[0];
        Plane plane = (Plane) inputs[1];

        DepthBuffer shadowMap;
        if (enableShadow.getVal()) {
            shadowFramebuffer.enable();
            openViewport(shadowFramebuffer);
            setClearColor(1.0f, 0.0f, 0.0f);
            clearColorAndDepth();
            enableDepthTest();
            enableCullFace();
            cullFront();

            mModelMatrix.setIdentity();
            mModelMatrix.transform(0, -model.getCenterPosition()[1], 0);
            for (Mesh mesh : model.getMeshes()) {
                Material tempMaterial = mesh.getCurrentMaterial();
                mesh.setCurrentMaterial(shadowMaterial);
                directionLight.setCameraCloseness(model.getLongestSide() * 1.1f);
                mesh.draw(directionLight, mModelMatrix);
                mesh.setCurrentMaterial(tempMaterial);
            }

            disableCullFace();
            disableDepthTest();
            setClearColorToDefault();
            shadowFramebuffer.disable();

            shadowMap = shadowDepthBuffer;
        }else {
            shadowMap = DepthBuffer.getNullDepthBuffer();
        }

        return new Object[]{
                shadowMap, model, plane, directionLight
        };
    }
}
