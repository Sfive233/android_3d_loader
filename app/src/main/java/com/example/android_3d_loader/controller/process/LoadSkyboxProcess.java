package com.example.android_3d_loader.controller.process;

import android.content.Context;

import com.example.android_3d_loader.core.EngineConstants;
import com.example.android_3d_loader.core.cubeMap.CubeMapPreRenderer;
import com.example.android_3d_loader.core.cubeMap.Skybox;
import com.example.android_3d_loader.core.texture.buffer.BufferParam;
import com.example.android_3d_loader.core.texture.buffer.BufferPreRenderer;
import com.example.android_3d_loader.core.material.HDRCubeMapMaterial;
import com.example.android_3d_loader.core.material.PBRCubeMapConvolutionMaterial;
import com.example.android_3d_loader.core.material.PBRCubeMapPreFilterMaterial;
import com.example.android_3d_loader.core.material.PBRPreBRDFMaterial;
import com.example.android_3d_loader.core.material.PBRMaterial;
import com.example.android_3d_loader.core.material.SkyboxMaterial;
import com.example.android_3d_loader.core.texture.buffer.ColorBuffer;
import com.example.android_3d_loader.core.texture.texture2D.Texture2D;
import com.example.android_3d_loader.core.texture.textureCube.TextureCube;

public class LoadSkyboxProcess extends Process {

    protected CubeMapPreRenderer cubeMapPreRenderer;
    protected SkyboxMaterial skyboxMaterial;

    public LoadSkyboxProcess(Context context, OnProcessFinishedListener listener, Object... params) {
        super(context, listener, params);
        cubeMapPreRenderer = new CubeMapPreRenderer(getContext());
        skyboxMaterial = new SkyboxMaterial(getContext());
    }

    @Override
    protected Object[] onProcess() {
        Skybox skybox = new Skybox();
        TextureCube hdrCubeMap;

        String skyboxPath;
        if (glCommunicator.isSaveExist()){
            Skybox savedSkybox = (Skybox) glCommunicator.getData("Skybox");
            if (savedSkybox != null){
                skybox.setIsVisible(savedSkybox.getIsVisible().getVal());
            }
            skybox.setCurrentMaterial(skyboxMaterial);
            glCommunicator.putData("Skybox", skybox);

            skyboxPath = (String) glCommunicator.getData("SkyboxPath");
            if (skyboxPath == null){
                skyboxPath = EngineConstants.DEFAULT_SKYBOX_PATH;
            }
        }else {
            skybox.setCurrentMaterial(skyboxMaterial);
            glCommunicator.putData("Skybox", skybox);

            skyboxPath = EngineConstants.DEFAULT_SKYBOX_PATH;
            glCommunicator.putData("SkyboxPath", skyboxPath);
        }
        HDRCubeMapMaterial hdrCubeMapMaterial = new HDRCubeMapMaterial(getContext());
        hdrCubeMapMaterial.setTexture(new Texture2D(getContext(), skyboxPath));
        cubeMapPreRenderer.setCurrentMaterial(hdrCubeMapMaterial);
        BufferParam bufferParam = new BufferParam();
        bufferParam.setBufferBit(BufferParam.BufferBit.BIT_16);
        bufferParam.setBufferType(BufferParam.BufferType.FLOAT);
        bufferParam.setBufferComponent(BufferParam.BufferComponent.RGB);
        bufferParam.setTextureMinFilter(BufferParam.TextureFilter.LINEAR);
        bufferParam.setTextureMagFilter(BufferParam.TextureFilter.LINEAR);
        bufferParam.setMapSize(512);
        cubeMapPreRenderer.setBufferParam(bufferParam);
        hdrCubeMap = cubeMapPreRenderer.outPutMap();
        skyboxMaterial.setCubeMap(hdrCubeMap);

        // IBL
        PBRCubeMapConvolutionMaterial pbrCubeMapConvolutionMaterial = new PBRCubeMapConvolutionMaterial(getContext());
        pbrCubeMapConvolutionMaterial.setCubeMap(hdrCubeMap);
        cubeMapPreRenderer.setCurrentMaterial(pbrCubeMapConvolutionMaterial);
        bufferParam.setBufferBit(BufferParam.BufferBit.BIT_16);
        bufferParam.setBufferType(BufferParam.BufferType.FLOAT);
        bufferParam.setBufferComponent(BufferParam.BufferComponent.RGB);
        bufferParam.setTextureMinFilter(BufferParam.TextureFilter.LINEAR);
        bufferParam.setTextureMagFilter(BufferParam.TextureFilter.LINEAR);
        bufferParam.setMapSize(32);
        cubeMapPreRenderer.setBufferParam(bufferParam);
        TextureCube convolutionMap = cubeMapPreRenderer.outPutMap();

        int maxReflectionLod = 5;
        PBRCubeMapPreFilterMaterial pbrCubeMapPreFilterMaterial = new PBRCubeMapPreFilterMaterial(getContext());
        pbrCubeMapPreFilterMaterial.setCubeMap(hdrCubeMap);
        cubeMapPreRenderer.setCurrentMaterial(pbrCubeMapPreFilterMaterial);
        bufferParam.setBufferBit(BufferParam.BufferBit.BIT_16);
        bufferParam.setBufferType(BufferParam.BufferType.FLOAT);
        bufferParam.setBufferComponent(BufferParam.BufferComponent.RGB);
        bufferParam.setTextureMinFilter(BufferParam.TextureFilter.LINEAR_MIP_MAP_LINEAR);
        bufferParam.setTextureMagFilter(BufferParam.TextureFilter.LINEAR);
        bufferParam.setMapSize(128);
        bufferParam.setGenMipmap(true);
        cubeMapPreRenderer.setBufferParam(bufferParam);
        cubeMapPreRenderer.setMaxMipLevels(maxReflectionLod);
        TextureCube preFilterMap = cubeMapPreRenderer.outPutMap();

        BufferPreRenderer bufferPreRenderer = new BufferPreRenderer();
        bufferPreRenderer.setCurrentMaterial(new PBRPreBRDFMaterial(getContext()));
        bufferParam.setBufferBit(BufferParam.BufferBit.BIT_16);
        bufferParam.setBufferType(BufferParam.BufferType.FLOAT);
        bufferParam.setBufferComponent(BufferParam.BufferComponent.RG);
        bufferParam.setTextureMinFilter(BufferParam.TextureFilter.LINEAR);
        bufferParam.setTextureMagFilter(BufferParam.TextureFilter.LINEAR);
        bufferParam.setMapSize(128);
        bufferParam.setGenMipmap(false);
        bufferPreRenderer.setBufferParam(bufferParam);
        ColorBuffer brdfLut = bufferPreRenderer.outputMap();

        PBRMaterial.setDiffuseIrradianceMap(convolutionMap);
        PBRMaterial.setSpecularPreFilterMap(preFilterMap);
        PBRMaterial.setMaxReflectionLod(maxReflectionLod);
        PBRMaterial.setSpecularBRDFLut(brdfLut);

        return new Object[]{
                skybox
        };
    }
}
