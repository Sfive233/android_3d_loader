package com.example.android_3d_loader.controller.renderPass;

import android.opengl.GLES30;

import com.example.android_3d_loader.core.material.AxisMaterial;
import com.example.android_3d_loader.core.model.mesh.basicObject.AxisIndicator;
import com.example.android_3d_loader.core.model.mesh.basicObject.Plane;
import com.example.android_3d_loader.core.camera.OrbitCamera;
import com.example.android_3d_loader.core.cubeMap.Skybox;
import com.example.android_3d_loader.core.texture.buffer.BufferParam;
import com.example.android_3d_loader.core.texture.buffer.ColorBuffer;
import com.example.android_3d_loader.core.texture.buffer.DepthBuffer;
import com.example.android_3d_loader.core.framebuffer.FrameBuffer;
import com.example.android_3d_loader.core.light.DirectionLight;
import com.example.android_3d_loader.core.material.Material;
import com.example.android_3d_loader.core.material.PBRMaterial;
import com.example.android_3d_loader.core.material.TraditionalMaterial;
import com.example.android_3d_loader.core.material.WireFrameMaterial;
import com.example.android_3d_loader.core.model.mesh.Mesh;
import com.example.android_3d_loader.core.model.Model;
import com.example.android_3d_loader.controller.process.InitCameraProcess;
import com.example.android_3d_loader.controller.process.Process;

public class ScenePass extends BaseRenderPass{

    protected WireFrameMaterial wireFrameMaterial;
    protected TraditionalMaterial planeMaterial;
    protected ColorBuffer pbrColorBuffer;
    protected ColorBuffer brightColorBuffer;
    protected FrameBuffer pbrFramebuffer;

    protected OrbitCamera mainCamera;
    protected OrbitCamera axisCamera;
    protected AxisIndicator axisIndicator;
    protected AxisMaterial axisMaterial;
    private InitCameraProcess initCameraProcess;

    @Override
    public void renderInit() {
        wireFrameMaterial = new WireFrameMaterial(getContext());
        planeMaterial = new TraditionalMaterial(getContext());
        BufferParam bufferParam = new BufferParam();
        bufferParam.setBufferBit(BufferParam.BufferBit.BIT_16);
        bufferParam.setBufferType(BufferParam.BufferType.FLOAT);
        bufferParam.setBufferComponent(BufferParam.BufferComponent.RGB);
        pbrColorBuffer = new ColorBuffer(SRC_WIDTH, SRC_HEIGHT, bufferParam);
        brightColorBuffer = new ColorBuffer(SRC_WIDTH, SRC_HEIGHT, bufferParam);
        pbrFramebuffer = new FrameBuffer(pbrColorBuffer, brightColorBuffer);
        initCameraProcess = new InitCameraProcess(
                getContext(),
                new Process.OnProcessFinishedListener() {
                    @Override
                    public void onFinish(Object... objects) {
                        mainCamera = (OrbitCamera) objects[0];
                        axisCamera = (OrbitCamera) objects[1];
                        axisIndicator = (AxisIndicator) objects[2];
                        axisMaterial = (AxisMaterial) objects[3];
                    }
                },
                SRC_WIDTH, SRC_HEIGHT
        );
    }

    @Override
    public void renderReact() {
        initCameraProcess.doProcess();
    }

    @Override
    public Object[] renderUpdate(Object... inputs) {

        DepthBuffer shadowMap = (DepthBuffer) inputs[0];
        Model model = (Model) inputs[1];
        Plane plane = (Plane) inputs[2];
        DirectionLight directionLight = (DirectionLight) inputs[3];
        Skybox skybox = (Skybox) inputs[4];

        pbrFramebuffer.enable();
        openViewport(pbrFramebuffer);
        clearColorAndDepth();
        enableDepthTest();

        mModelMatrix.setIdentity();
        mModelMatrix.transform(0, -model.getCenterPosition()[1], 0);
        for (Mesh mesh: model.getMeshes()){
            Material currentMaterial = mesh.getCurrentMaterial();
            if (currentMaterial instanceof TraditionalMaterial){
                currentMaterial.setViewPosition(mainCamera.getCamPos());
                ((TraditionalMaterial) currentMaterial).setDirectionLight(directionLight);
                ((TraditionalMaterial) currentMaterial).setShadowMap(shadowMap);
            }
            if (currentMaterial instanceof PBRMaterial){
                currentMaterial.setViewPosition(mainCamera.getCamPos());
                ((PBRMaterial) currentMaterial).setDirectionLight(directionLight);
                ((PBRMaterial) currentMaterial).setShadowMap(shadowMap);
            }
            mesh.setCurrentMaterial(currentMaterial);
            if (mesh.getIsShowWireFrame().getVal()){
                GLES30.glEnable(GLES30.GL_POLYGON_OFFSET_FILL);
                GLES30.glPolygonOffset(1.0f, 1.0f);
                GLES30.glLineWidth(3.0f);
                mesh.draw(mainCamera, mModelMatrix);
                GLES30.glDisable(GLES30.GL_POLYGON_OFFSET_FILL);
                mesh.getWireFrameMesh().setCurrentMaterial(wireFrameMaterial);
                mesh.getWireFrameMesh().draw(mainCamera, mModelMatrix);
            }else {
                mesh.draw(mainCamera, mModelMatrix);
            }
        }

        planeMaterial.setDirectionLight(directionLight);
        planeMaterial.setShadowMap(shadowMap);
        planeMaterial.setViewPosition(mainCamera.getCamPos());
        planeMaterial.setDirectionLight(directionLight);
        plane.setCurrentMaterial(planeMaterial);
        mModelMatrix.setIdentity();
        mModelMatrix.transform(0, -(model.getCenterPosition()[1]- model.getMinY())-0.1f, 0);
        plane.draw(mainCamera, mModelMatrix);

        depthLE();
        skybox.draw(mainCamera);
        depthDefault();

        disableDepthTest();
        pbrFramebuffer.disable();

        return new Object[]{
                pbrColorBuffer, brightColorBuffer, axisCamera, axisIndicator, axisMaterial
        };
    }
}
