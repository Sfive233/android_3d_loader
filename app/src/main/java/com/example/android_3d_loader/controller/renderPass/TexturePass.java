package com.example.android_3d_loader.controller.renderPass;

import android.content.Context;

import com.example.android_3d_loader.controller.process.LoadTextureProcess;

public class TexturePass extends BaseRenderPass{

    private LoadTextureProcess loadTextureProcess;

//    public TexturePass(Context context) {
//        super(context);
//    }

    @Override
    public void renderInit() {

        loadTextureProcess = new LoadTextureProcess(getContext(), null);
    }

    @Override
    public void renderReact() {
        loadTextureProcess.doProcess();
    }

    @Override
    public Object[] renderUpdate(Object... inputs) {
        return inputs;
    }
}
