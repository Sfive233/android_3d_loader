package com.example.android_3d_loader.core.material;

import android.content.Context;
import android.opengl.Matrix;

import com.example.android_3d_loader.R;
import com.example.android_3d_loader.core.dataType.Boolean;
import com.example.android_3d_loader.core.dataType.Float;
import com.example.android_3d_loader.core.texture.buffer.ColorBuffer;
import com.example.android_3d_loader.core.texture.buffer.DepthBuffer;
import com.example.android_3d_loader.core.texture.texture2D.Texture2D;
import com.example.android_3d_loader.core.dataType.Vector3;
import com.example.android_3d_loader.core.light.DirectionLight;
import com.example.android_3d_loader.core.texture.textureCube.TextureCube;
import com.google.gson.annotations.Expose;

public class PBRMaterial extends Material{

    @Expose
    private Boolean isUseAlbedoMap = new Boolean(true);
    @Expose
    private Texture2D albedoMap;
    @Expose
    private Vector3 albedoVal = new Vector3(1.0f, 0.0f, 0.0f);
    @Expose
    private Boolean isUseAOMap = new Boolean(false);
    @Expose
    private Texture2D aoMap;

    @Expose
    private Boolean isHasHeightMap = new Boolean(false);
    @Expose
    private Texture2D heightMap;
    @Expose
    private Boolean isUseMetallicMap = new Boolean(false);
    @Expose
    private Texture2D metallicMap;
    @Expose
    private Float metallicVal = new Float(0.0f);

    @Expose
    private Boolean isHasNormal = new Boolean(false);
    @Expose
    private Texture2D normalMap;
    @Expose
    private Boolean isUseRoughnessMap = new Boolean(false);
    @Expose
    private Texture2D roughnessMap;
    @Expose
    private Float roughnessVal = new Float(1.0f);

    private static DirectionLight directionLight;
    private Vector3 viewPosition;

    private static TextureCube diffuseIrradianceMap = TextureCube.getNullTextureCube();
    private static TextureCube specularPreFilterMap = TextureCube.getNullTextureCube();
    private static int maxReflectionLod;
    private static ColorBuffer specularBRDFLut = ColorBuffer.getNullColorBuffer();

    private DepthBuffer shadowMap = DepthBuffer.getNullDepthBuffer();
    private int softShadowSampleNum;
    @Expose
    private Float bias = new Float(-0.001f);

    @Expose
    private Float maxLayerNum = new Float(16.0f);
    @Expose
    private Float minLayerNum = new Float(2.0f);
    @Expose
    private Float heightScale = new Float(0.03f);
    @Expose
    private Boolean useHalfHeightRange = new Boolean(false);
    @Expose
    private Boolean discardEdge = new Boolean(false);

    public PBRMaterial(Context context) {
        super(context, R.raw.pbr_ibl_vs, R.raw.pbr_ibl_fs);
        albedoMap = new Texture2D();
        aoMap = new Texture2D(context, "default/white.png");
        heightMap = new Texture2D();
        roughnessMap = new Texture2D(context, "default/white.png");
        normalMap = new Texture2D();
        metallicMap = new Texture2D(context, "default/black.png");

        softShadowSampleNum = 3;
    }

    @Override
    public void update() {
        shader.setBool("IsUseAlbedoMap", isUseAlbedoMap);
        shader.setTexture("AlbedoMap", albedoMap);
        shader.setVec3("AlbedoVal", albedoVal);

        shader.setBool("IsUseAOMap", isUseAOMap);
        shader.setTexture("AOMap", aoMap);

        shader.setBool("IsHasHeightMap", isHasHeightMap.getVal());
        shader.setTexture("HeightMap", heightMap);

        shader.setBool("IsUseMetallicMap", isUseMetallicMap);
        shader.setTexture("MetallicMap", metallicMap);
        shader.setFloat("MetallicVal", metallicVal);

        shader.setBool("IsHasNormalMap", !normalMap.equals(Texture2D.getNullTexture2D()) && isHasNormal.getVal());
        shader.setTexture("NormalMap", normalMap);

        shader.setBool("IsUseRoughnessMap", isUseRoughnessMap);
        shader.setTexture("RoughnessMap", roughnessMap);
        shader.setFloat("RoughnessVal", roughnessVal);

        shader.setCubeMap("DiffuseIrradianceMap", diffuseIrradianceMap);
        shader.setCubeMap("SpecularPreFilterMap", specularPreFilterMap);
        shader.setFloat("MaxReflectionLod", maxReflectionLod - 1.0f);
        shader.setTexture("SpecularBRDFLut", specularBRDFLut);

        shader.setBool("IsUseShadowMapping", !shadowMap.equals(Texture2D.getNullTexture2D()));
        float[] lightProjectionViewMatrix = new float[16];
        Matrix.multiplyMM(lightProjectionViewMatrix, 0, directionLight.getProjectionMatrix().getVal(), 0, directionLight.getViewMatrix().getVal(), 0);
        shader.setMat4("LightProjectionViewMatrix", lightProjectionViewMatrix);
        shader.setTexture("ShadowMap", shadowMap);
        shader.setInt("SoftShadowSampleNum", softShadowSampleNum);
        shader.setFloat("Bias", bias);

        shader.setFloat("MaxLayerNum", maxLayerNum);
        shader.setFloat("MinLayerNum", minLayerNum);
        shader.setFloat("HeightScale", heightScale);
        shader.setBool("DiscardEdge", discardEdge);
        shader.setBool("UseHalfHeightRange", useHalfHeightRange);

        shader.setVec3("LightDirection", directionLight.getLightDirection());
        shader.setVec3("LightDiffuseColor", directionLight.getDiffuseLightColor());
        shader.setVec3("ViewPos", viewPosition);
    }

    public void setDirectionLight(DirectionLight directionLight) {
        PBRMaterial.directionLight = directionLight;
    }

    public void setViewPosition(Vector3 viewPosition){
        this.viewPosition = viewPosition;
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

    public static void setDiffuseIrradianceMap(TextureCube diffuseIrradianceMap) {
        PBRMaterial.diffuseIrradianceMap = diffuseIrradianceMap;
    }

    public static void setSpecularPreFilterMap(TextureCube specularPreFilterMap) {
        PBRMaterial.specularPreFilterMap = specularPreFilterMap;
    }

    public static void setSpecularBRDFLut(ColorBuffer specularBRDFLut) {
        PBRMaterial.specularBRDFLut = specularBRDFLut;
    }

    public static void setMaxReflectionLod(int maxReflectionLod) {
        PBRMaterial.maxReflectionLod = maxReflectionLod;
    }

    public void setShadowMap(DepthBuffer shadowMap) {
        this.shadowMap = shadowMap;
    }

    public void setSoftShadowSampleNum(int softShadowSampleNum) {
        this.softShadowSampleNum = softShadowSampleNum;
    }

    public void setNormalMap(Texture2D normalMap) {
        this.normalMap = normalMap;
    }

    public void setUseHalfHeightRange(boolean useHalfHeightRange) {
        this.useHalfHeightRange.setVal(useHalfHeightRange);
    }

    public void setHeightMap(Texture2D heightMap) {
        this.heightMap = heightMap;
    }

    public Texture2D getAlbedoMap() {
        return albedoMap;
    }

    public Texture2D getAoMap() {
        return aoMap;
    }

    public Texture2D getHeightMap() {
        return heightMap;
    }

    public Texture2D getMetallicMap() {
        return metallicMap;
    }

    public Texture2D getNormalMap() {
        return normalMap;
    }

    public Texture2D getRoughnessMap() {
        return roughnessMap;
    }

    public Boolean getIsHasNormal() {
        return isHasNormal;
    }

    public void setIsHasNormal(boolean isHasNormal) {
        this.isHasNormal.setVal(isHasNormal);
    }

    public Boolean getIsHasHeightMap() {
        return isHasHeightMap;
    }

    public void setIsHasHeightMap(boolean isHasHeightMap) {
        this.isHasHeightMap.setVal(isHasHeightMap);
    }

    public Float getHeightScale() {
        return heightScale;
    }

    public void setHeightScale(float heightScale) {
        this.heightScale.setVal(heightScale);
    }

    public Boolean getUseHalfHeightRange() {
        return useHalfHeightRange;
    }

    public Boolean getDiscardEdge() {
        return discardEdge;
    }

    public void setDiscardEdge(boolean discardEdge) {
        this.discardEdge.setVal(discardEdge);
    }

    public Boolean getIsUseAlbedoMap() {
        return isUseAlbedoMap;
    }

    public void setIsUseAlbedoMap(boolean isUseAlbedoMap) {
        this.isUseAlbedoMap.setVal(isUseAlbedoMap);
    }

    public Vector3 getAlbedoVal() {
        return albedoVal;
    }

    public void setAlbedoVal(float[] albedoVal) {
        this.albedoVal.setXYZ(albedoVal);
    }

    public Boolean getIsUseMetallicMap() {
        return isUseMetallicMap;
    }

    public void setIsUseMetallicMap(boolean isUseMetallicMap) {
        this.isUseMetallicMap.setVal(isUseMetallicMap);
    }

    public Float getMetallicVal() {
        return metallicVal;
    }

    public void setMetallicVal(float metallicVal) {
        this.metallicVal.setVal(metallicVal);
    }

    public Boolean getIsUseRoughnessMap() {
        return isUseRoughnessMap;
    }

    public void setIsUseRoughnessMap(boolean isUseRoughnessMap) {
        this.isUseRoughnessMap.setVal(isUseRoughnessMap);
    }

    public Float getRoughnessVal() {
        return roughnessVal;
    }

    public void setRoughnessVal(float roughnessVal) {
        this.roughnessVal.setVal(roughnessVal);
    }

    public Boolean getIsUseAOMap() {
        return isUseAOMap;
    }

    public void setIsUseAOMap(boolean isUseAOMap) {
        this.isUseAOMap.setVal(isUseAOMap);
    }
}
