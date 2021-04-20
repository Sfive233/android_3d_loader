package com.example.android_3d_loader.core.material;

import android.content.Context;

import com.example.android_3d_loader.R;
import com.example.android_3d_loader.core.camera.BaseCamera;
import com.example.android_3d_loader.core.texture.texture2D.Texture2D;
import com.example.android_3d_loader.core.light.DirectionLight;

public class PBRDirectMaterial extends Material{

    private Texture2D albedoMap = new Texture2D();
    private Texture2D metallicMap = new Texture2D();
    private Texture2D roughnessMap = new Texture2D();
    private Texture2D aoMap = new Texture2D();
    private DirectionLight directionLight;
    private BaseCamera camera;

    public PBRDirectMaterial(Context context) {
        super(context, R.raw.pbr_direct_vs, R.raw.pbr_direct_fs);
    }

    @Override
    public void init() {

    }

    @Override
    public void update() {
//        if (albedoMap.getTexture() != Texture.getNullTexture()){
//            shader.setTexture("AlbedoMap", albedoMap);
//        }
//        if (albedoMap.getTexture() != Texture.getNullTexture()){
//            shader.setTexture("MetallicMap", metallicMap);
//        }
//        if (albedoMap.getTexture() != Texture.getNullTexture()){
//            shader.setTexture("RoughnessMap", roughnessMap);
//        }
//        if (albedoMap.getTexture() != Texture.getNullTexture()){
//            shader.setTexture("AOMap", aoMap);
//        }
//        if (directionLight != null){
//            shader.setVec3("LightDirection", directionLight.getLightDirection());
//            shader.setVec3("LightDiffuseColor", directionLight.getDiffuseLightColor());
//        }
//        shader.setVec3("ViewPos", camera.getCamPos());
    }

    public void setAlbedoMap(Texture2D albedoMap) {
        this.albedoMap = albedoMap;
    }

    public void setMetallicMap(Texture2D metallicMap) {
        this.metallicMap = metallicMap;
    }

    public void setRoughnessMap(Texture2D roughnessMap) {
        this.roughnessMap = roughnessMap;
    }

    public void setAoMap(Texture2D aoMap) {
        this.aoMap = aoMap;
    }

    public void setDirectionLight(DirectionLight directionLight) {
        this.directionLight = directionLight;
    }

    public void setCamera(BaseCamera camera) {
        this.camera = camera;
    }
}
