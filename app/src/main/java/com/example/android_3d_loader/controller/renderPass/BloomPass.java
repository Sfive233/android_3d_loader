package com.example.android_3d_loader.controller.renderPass;

import com.example.android_3d_loader.core.model.mesh.basicObject.ScreenQuad;
import com.example.android_3d_loader.core.dataType.Boolean;
import com.example.android_3d_loader.core.texture.buffer.BufferParam;
import com.example.android_3d_loader.core.texture.buffer.ColorBuffer;
import com.example.android_3d_loader.core.framebuffer.FrameBuffer;
import com.example.android_3d_loader.core.material.BloomMaterial;
import com.example.android_3d_loader.controller.process.InitBloomProcess;
import com.example.android_3d_loader.controller.process.Process;

public class BloomPass extends BaseRenderPass{

    protected ScreenQuad screenQuad;
    private BloomMaterial bloomMaterial;
    private ColorBuffer bloomBlurHResultBuffer0;
    private FrameBuffer bloomBlurHFramebuffer0;
    private ColorBuffer bloomBlurVResultBuffer0;
    private FrameBuffer bloomBlurVFramebuffer0;
    private ColorBuffer bloomBlurHResultBuffer1;
    private FrameBuffer bloomBlurHFramebuffer1;
    private ColorBuffer bloomBlurVResultBuffer1;
    private FrameBuffer bloomBlurVFramebuffer1;
    private ColorBuffer bloomBlurCompleteBuffer;
    private FrameBuffer bloomBlurCompleteFramebuffer;

    private InitBloomProcess initBloomProcess;

    private Boolean enableBloom;

    @Override
    public void renderInit() {
        screenQuad = new ScreenQuad();
        BufferParam bufferParam = new BufferParam();
        bufferParam.setBufferBit(BufferParam.BufferBit.BIT_16);
        bufferParam.setBufferType(BufferParam.BufferType.FLOAT);
        bufferParam.setBufferComponent(BufferParam.BufferComponent.RGB);
        bufferParam.setTextureMinFilter(BufferParam.TextureFilter.LINEAR);
        bufferParam.setTextureMagFilter(BufferParam.TextureFilter.LINEAR);
        bloomBlurHResultBuffer0 = new ColorBuffer(SRC_WIDTH / 2, SRC_HEIGHT / 2, bufferParam);
        bloomBlurHFramebuffer0 = new FrameBuffer(bloomBlurHResultBuffer0);
        bloomBlurVResultBuffer0 = new ColorBuffer(SRC_WIDTH / 2, SRC_HEIGHT / 2, bufferParam);
        bloomBlurVFramebuffer0 = new FrameBuffer(bloomBlurVResultBuffer0);
        bloomBlurHResultBuffer1 = new ColorBuffer(SRC_WIDTH / 4, SRC_HEIGHT / 4, bufferParam);
        bloomBlurHFramebuffer1 = new FrameBuffer(bloomBlurHResultBuffer1);
        bloomBlurVResultBuffer1 = new ColorBuffer(SRC_WIDTH / 4, SRC_HEIGHT / 4, bufferParam);
        bloomBlurVFramebuffer1 = new FrameBuffer(bloomBlurVResultBuffer1);
        bloomBlurCompleteBuffer = new ColorBuffer(SRC_WIDTH, SRC_HEIGHT, bufferParam);
        bloomBlurCompleteFramebuffer = new FrameBuffer(bloomBlurCompleteBuffer);

        initBloomProcess = new InitBloomProcess(
                getContext(),
                new Process.OnProcessFinishedListener() {
                    @Override
                    public void onFinish(Object... objects) {
                        enableBloom = (Boolean) objects[0];
                        bloomMaterial = (BloomMaterial) objects[1];
                    }
                }
        );
    }

    @Override
    public void renderReact() {
        initBloomProcess.doProcess();

    }

    @Override
    public Object[] renderUpdate(Object... inputs) {
        int i = 0;
        ColorBuffer pbrColorBuffer = (ColorBuffer) inputs[i++];
        ColorBuffer brightColorBuffer = (ColorBuffer) inputs[i++];

        ColorBuffer passResult;
        if (enableBloom.getVal()) {
            bloomBlurHFramebuffer0.enable();
            openViewport(bloomBlurHFramebuffer0);
            clearColor();
            bloomMaterial.setBloomMap(brightColorBuffer);
            bloomMaterial.setHorizontal(true);
            screenQuad.setCurrentMaterial(bloomMaterial);
            screenQuad.draw();
            bloomBlurHFramebuffer0.disable();

            bloomBlurVFramebuffer0.enable();
            openViewport(bloomBlurVFramebuffer0);
            clearColor();
            bloomMaterial.setBloomMap(bloomBlurHResultBuffer0);
            bloomMaterial.setHorizontal(false);
            screenQuad.setCurrentMaterial(bloomMaterial);
            screenQuad.draw();
            bloomBlurVFramebuffer0.disable();

            bloomBlurHFramebuffer1.enable();
            openViewport(bloomBlurHFramebuffer1);
            clearColor();
            bloomMaterial.setBloomMap(bloomBlurVResultBuffer0);
            bloomMaterial.setHorizontal(true);
            screenQuad.setCurrentMaterial(bloomMaterial);
            screenQuad.draw();
            bloomBlurHFramebuffer1.disable();

            bloomBlurVFramebuffer1.enable();
            openViewport(bloomBlurVFramebuffer1);
            clearColor();
            bloomMaterial.setBloomMap(bloomBlurHResultBuffer1);
            bloomMaterial.setHorizontal(false);
            screenQuad.setCurrentMaterial(bloomMaterial);
            screenQuad.draw();
            bloomBlurVFramebuffer1.disable();

            bloomBlurCompleteFramebuffer.enable();
            openViewport(bloomBlurCompleteFramebuffer);
            clearColor();
            bloomMaterial.setBloomMap(bloomBlurVResultBuffer1);
            bloomMaterial.setSourceMap(pbrColorBuffer);
            bloomMaterial.setComplete(true);
            screenQuad.setCurrentMaterial(bloomMaterial);
            screenQuad.draw();
            bloomBlurCompleteFramebuffer.disable();

            passResult = bloomBlurCompleteBuffer;
        }else {
            passResult = pbrColorBuffer;
        }

        return new Object[]{
                passResult,
                screenQuad,
                inputs[2], inputs[3], inputs[4]
        };
    }
}
