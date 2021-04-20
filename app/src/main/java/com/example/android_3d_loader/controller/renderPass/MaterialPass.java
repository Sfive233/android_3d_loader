package com.example.android_3d_loader.controller.renderPass;

import com.example.android_3d_loader.controller.process.InitMaterialProcess;

public class MaterialPass extends BaseRenderPass{

    private InitMaterialProcess initMaterialProcess;

    @Override
    public void renderInit() {

        initMaterialProcess = new InitMaterialProcess(getContext(), null);
    }

    @Override
    public void renderReact() {
        initMaterialProcess.doProcess();
    }

    @Override
    public Object[] renderUpdate(Object... inputs) {
        return inputs;
    }
}
