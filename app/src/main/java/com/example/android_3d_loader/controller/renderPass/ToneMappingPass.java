package com.example.android_3d_loader.controller.renderPass;

import com.example.android_3d_loader.core.model.mesh.basicObject.ScreenQuad;
import com.example.android_3d_loader.core.dataType.Boolean;
import com.example.android_3d_loader.core.texture.buffer.BufferParam;
import com.example.android_3d_loader.core.texture.buffer.ColorBuffer;
import com.example.android_3d_loader.core.framebuffer.FrameBuffer;
import com.example.android_3d_loader.core.material.toneMapping.ACESToneMappingMaterial;
import com.example.android_3d_loader.core.material.toneMapping.CEToneMappingMaterial;
import com.example.android_3d_loader.core.material.toneMapping.ToneMappingMaterial;
import com.example.android_3d_loader.controller.process.InitToneMappingProcess;
import com.example.android_3d_loader.controller.process.Process;

public class ToneMappingPass extends BaseRenderPass{

    protected FrameBuffer toneMappingFramebuffer;
    protected ToneMappingMaterial toneMappingMaterial;
    protected ColorBuffer toneMappingBuffer;

    private InitToneMappingProcess initToneMappingProcess;

    private Boolean enableToneMapping;

    @Override
    public void renderInit() {

        BufferParam bufferParam = new BufferParam();
        bufferParam.setBufferBit(BufferParam.BufferBit.BIT_16);
        bufferParam.setBufferType(BufferParam.BufferType.FLOAT);
        bufferParam.setBufferComponent(BufferParam.BufferComponent.RGB);
        bufferParam.setTextureMinFilter(BufferParam.TextureFilter.NEAREST);
        bufferParam.setTextureMagFilter(BufferParam.TextureFilter.NEAREST);
        bufferParam.setGenMipmap(false);
        toneMappingBuffer = new ColorBuffer(SRC_WIDTH, SRC_HEIGHT, bufferParam);
        toneMappingFramebuffer = new FrameBuffer(toneMappingBuffer);

        CEToneMappingMaterial ceToneMappingMaterial = new CEToneMappingMaterial(getContext());
        ACESToneMappingMaterial acesToneMappingMaterial = new ACESToneMappingMaterial(getContext());
        initToneMappingProcess = new InitToneMappingProcess(
                getContext(),
                new Process.OnProcessFinishedListener() {
                    @Override
                    public void onFinish(Object... objects) {
                        enableToneMapping = (Boolean) objects[0];
                        toneMappingMaterial = (ToneMappingMaterial) objects[1];
                    }
                },
                toneMappingMaterial, ceToneMappingMaterial, acesToneMappingMaterial
        );
    }

    @Override
    public void renderReact() {
        initToneMappingProcess.doProcess();

    }

    @Override
    public Object[] renderUpdate(Object... inputs) {

        ColorBuffer sourceImage = (ColorBuffer) inputs[0];
        ScreenQuad screenQuad = (ScreenQuad) inputs[1];

        ColorBuffer passResult;
        if (enableToneMapping.getVal()) {
            toneMappingFramebuffer.enable();
            openViewport(toneMappingFramebuffer);
            clearColor();

            toneMappingMaterial.setSourceImage(sourceImage);
            screenQuad.setCurrentMaterial(toneMappingMaterial);
            screenQuad.draw();

            toneMappingFramebuffer.disable();
            passResult = toneMappingBuffer;
        }else {
            passResult = sourceImage;
        }

        return new Object[]{
                passResult,
                screenQuad,
                inputs[2], inputs[3], inputs[4]
        };
    }
}
