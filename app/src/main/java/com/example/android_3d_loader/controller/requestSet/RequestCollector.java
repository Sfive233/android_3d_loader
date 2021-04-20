package com.example.android_3d_loader.controller.requestSet;

import com.example.android_3d_loader.controller.GLRequest;
import com.example.android_3d_loader.controller.renderPass.BaseRenderPass;
import com.example.android_3d_loader.controller.renderPass.BloomPass;
import com.example.android_3d_loader.controller.renderPass.FinalPass;
import com.example.android_3d_loader.controller.renderPass.InitModelPass;
import com.example.android_3d_loader.controller.renderPass.MaterialPass;
import com.example.android_3d_loader.controller.renderPass.RenderPass;
import com.example.android_3d_loader.controller.renderPass.ScenePass;
import com.example.android_3d_loader.controller.renderPass.ShadowPass;
import com.example.android_3d_loader.controller.renderPass.SkyboxPass;
import com.example.android_3d_loader.controller.renderPass.TexturePass;
import com.example.android_3d_loader.controller.renderPass.ToneMappingPass;

import java.util.HashMap;

public class RequestCollector {

    private static final HashMap<GLRequest, RenderPass[]> requestSetHashMap = new HashMap<>();

    public RequestCollector() {
        doCollect();
    }

    public void doCollect(){
        InitModelPass initModelPass = new InitModelPass();
        MaterialPass materialPass = new MaterialPass();
        TexturePass texturePass = new TexturePass();
        ShadowPass shadowPass = new ShadowPass();
        SkyboxPass skyboxPass = new SkyboxPass();
        ScenePass scenePass = new ScenePass();
        BloomPass bloomPass = new BloomPass();
        ToneMappingPass toneMappingPass = new ToneMappingPass();
        FinalPass finalPass = new FinalPass();

        addRequestSet(
                new RequestSet(
                        GLRequest.BOOT,
                        initModelPass, materialPass, texturePass, shadowPass, skyboxPass, scenePass, bloomPass, toneMappingPass, finalPass
                )
        );
        addRequestSet(
                new RequestSet(
                        GLRequest.RUNNING,
                        BaseRenderPass.getEmptyRenderPass()
                )
        );
        addRequestSet(
                new RequestSet(
                        GLRequest.BACK_FROM_BACKGROUND,
                        BaseRenderPass.getEmptyRenderPass()
                )
        );
        addRequestSet(
                new RequestSet(
                        GLRequest.BACK_FROM_SWAPPING_MODEL,
                        initModelPass, materialPass, texturePass
                )
        );
        addRequestSet(
                new RequestSet(
                        GLRequest.BACK_FROM_SWAPPING_HDRI,
                        skyboxPass
                )
        );
        addRequestSet(
                new RequestSet(
                        GLRequest.BACK_FROM_SWAPPING_TEXTURE,
                        texturePass
                )
        );
        addRequestSet(
                new RequestSet(
                        GLRequest.SWITCH_TONE_MAPPING,
                        toneMappingPass
                )
        );
        addRequestSet(
                new RequestSet(
                        GLRequest.SWITCH_SHADER,
                        materialPass
                )
        );
    }

    protected void addRequestSet(RequestSet requestSet){
        requestSetHashMap.put(requestSet.getRequest(), requestSet.getRenderPasses());
    }

    public HashMap<GLRequest, RenderPass[]> getRequestSetHashMap() {
        return requestSetHashMap;
    }
}
