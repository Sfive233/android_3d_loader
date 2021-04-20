package com.example.android_3d_loader.controller.renderPass;

import com.example.android_3d_loader.core.camera.OrbitCamera;
import com.example.android_3d_loader.core.material.AxisMaterial;
import com.example.android_3d_loader.core.model.mesh.basicObject.AxisIndicator;
import com.example.android_3d_loader.core.model.mesh.basicObject.ScreenQuad;
import com.example.android_3d_loader.core.texture.buffer.ColorBuffer;
import com.example.android_3d_loader.core.material.ScreenMaterial;

public class FinalPass extends BaseRenderPass{

    protected ScreenMaterial screenMaterial;

    @Override
    public void renderInit() {
        screenMaterial = new ScreenMaterial(getContext());
    }

    @Override
    public void renderReact() {

    }

    @Override
    public Object[] renderUpdate(Object... inputs) {

        ColorBuffer finalColor = (ColorBuffer) inputs[0];
        ScreenQuad screenQuad = (ScreenQuad) inputs[1];
        OrbitCamera axisCamera = (OrbitCamera) inputs[2];
        AxisIndicator axisIndicator = (AxisIndicator) inputs[3];
        AxisMaterial axisMaterial = (AxisMaterial) inputs[4];

        openViewport();
        clearColorAndDepth();
        screenMaterial.setShowTexture(finalColor);
        screenMaterial.setGammaCorrection(true);
        screenQuad.setCurrentMaterial(screenMaterial);
        screenQuad.draw();

        if (axisIndicator.getIsVisible().getVal()){
            enableDepthTest();
            mModelMatrix.setIdentity();
            mModelMatrix.transform(axisCamera.getCamFrontPos());
            axisIndicator.setCurrentMaterial(axisMaterial);
            axisIndicator.draw(axisCamera, mModelMatrix);
            disableDepthTest();
        }

        return new Object[0];
    }
}
