package com.example.android_3d_loader.core.material;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.Matrix;

import com.example.android_3d_loader.R;
import com.example.android_3d_loader.core.dataType.Boolean;
import com.example.android_3d_loader.core.dataType.Float;
import com.example.android_3d_loader.core.texture.buffer.DepthBuffer;
import com.example.android_3d_loader.core.texture.texture2D.Texture2D;
import com.example.android_3d_loader.core.dataType.Vector3;
import com.example.android_3d_loader.core.light.DirectionLight;
import com.example.android_3d_loader.core.texture.Texture;
import com.google.gson.annotations.Expose;

public class TraditionalMaterial extends Material{
    protected static String TAG = "StandardMaterial";
    protected static DirectionLight directionLight = new DirectionLight();
    @Expose
    protected Vector3 objectColor = new Vector3(1.0f, 0.0f, 0.0f);
    @Expose
    protected Vector3 objectSpecular = new Vector3(1.0f, 1.0f, 1.0f);
    @Expose
    protected Texture2D diffuseMap = new Texture2D();
    @Expose
    protected Texture2D specularMap = new Texture2D();
    @Expose
    protected Texture2D normalMap = new Texture2D();
    @Expose
    protected Texture2D heightMap = new Texture2D();
    @Expose
    protected Boolean enableLighting = new Boolean(true);
    @Expose
    protected Float shininess = new Float(32.0f);// 反光率越大，散射越小
    @Expose
    protected Boolean isUseHeightMapHalfRange = new Boolean(false);
    @Expose
    protected Boolean isDiscardHeightMapEdge = new Boolean(false);
    @Expose
    protected Float heightScale = new Float(0.01f);
    @Expose
    protected Float maxLayerNum = new Float(64.0f);
    @Expose
    protected Float minLayerNum = new Float(8.0f);

    @Expose
    protected DepthBuffer shadowMap = DepthBuffer.getNullDepthBuffer();
    protected float bias = -0.001f;
    @Expose
    protected Boolean isUseSoftShadow = new Boolean(true);
    protected int softShadowSampleNum = 3;

    @Expose
    protected Boolean isUseDiffuseMap = new Boolean(true);
    @Expose
    protected Boolean isUseSpecularMap = new Boolean(true);

    // todo 是否重新开启保存
    protected Boolean isUseTangentSpace = new Boolean(false);
    protected Boolean isUseDisplacementMap = new Boolean(false);

    public TraditionalMaterial(Context context) {
        super(context, R.raw.standard_vs, R.raw.standard_fs);
    }

    @Override
    public void init() {

    }

    @Override
    public void update() {

        GLES30.glActiveTexture(GLES30.GL_TEXTURE1);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, diffuseMap.getTex());
        shader.setInt("DiffuseMap", 1);
        GLES30.glActiveTexture(GLES30.GL_TEXTURE2);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, specularMap.getTex());
        shader.setInt("SpecularMap", 2);
        GLES30.glActiveTexture(GLES30.GL_TEXTURE3);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, normalMap.getTex());
        shader.setInt("NormalMap", 3);
        GLES30.glActiveTexture(GLES30.GL_TEXTURE4);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, heightMap.getTex());
        shader.setInt("HeightMap", 4);
        GLES30.glActiveTexture(GLES30.GL_TEXTURE5);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, shadowMap.getTex());
        shader.setInt("ShadowMap", 5);
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);


        shader.setVec3("LightDirection", directionLight.getLightDirection());
        shader.setBool("IsHasNormalMap", !normalMap.equals(Texture2D.getNullTexture2D()) && isUseTangentSpace.getVal());
        shader.setVec3("ViewPos", viewPosition);
        float[] lightProjectionViewMatrix = new float[16];
        Matrix.multiplyMM(lightProjectionViewMatrix, 0, directionLight.getProjectionMatrix().getVal(), 0, directionLight.getViewMatrix().getVal(), 0);
        shader.setMat4("LightProjectionViewMatrix", lightProjectionViewMatrix);

        shader.setVec3("AmbientLightColor", directionLight.getAmbientLightColor());
        shader.setVec3("DiffuseLightColor", directionLight.getDiffuseLightColor());
        shader.setVec3("SpecularLightColor", directionLight.getSpecularLightColor());
        shader.setVec3("ObjectColor", objectColor);
        shader.setVec3("ObjectSpecular", objectSpecular);
        shader.setBool("IsUseLighting", enableLighting.getVal());
        shader.setBool("IsUseDiffuseMap", isUseDiffuseMap.getVal());
        shader.setBool("IsUseGammaCorrection", false);
        shader.setBool("IsUseSpecularMap", isUseSpecularMap.getVal());
        shader.setFloat("Shininess", shininess.getVal());

        shader.setBool("IsHasHeightMap", isUseDisplacementMap.getVal());
        shader.setBool("IsUseHeightMapHalfRange", isUseHeightMapHalfRange.getVal());
        shader.setBool("IsDiscardHeightMapEdge", isDiscardHeightMapEdge.getVal());
        shader.setFloat("HeightScale", heightScale.getVal());
        shader.setFloat("MaxLayerNum", maxLayerNum.getVal());
        shader.setFloat("MinLayerNum", minLayerNum.getVal());

        shader.setBool("IsUseShadowMap", !shadowMap.equals(DepthBuffer.getNullDepthBuffer()));
        shader.setFloat("Bias", bias);
        shader.setInt("SoftShadowSampleNum", softShadowSampleNum);
        shader.setBool("IsUseSoftShadow", isUseSoftShadow.getVal());
    }

    public Texture2D getDiffuseMap(){
        return diffuseMap;
    }

    public void setDiffuseMap(Texture2D diffuseMap){
        this.diffuseMap = diffuseMap;
    }

    public void setObjectColor(float[] objectColor) {
        this.objectColor.x.setVal(objectColor[0]);
        this.objectColor.y.setVal(objectColor[1]);
        this.objectColor.z.setVal(objectColor[2]);
    }

    public void setObjectColor(float r, float g, float b) {
        this.objectColor.x.setVal(r);
        this.objectColor.y.setVal(g);
        this.objectColor.z.setVal(b);
    }

    public Vector3 getObjectColor() {
        return objectColor;
    }

    public Vector3 getObjectSpecular() {
        return objectSpecular;
    }

    public void setObjectSpecular(float[] objectSpecular) {
        this.objectSpecular.x.setVal(objectSpecular[0]);
        this.objectSpecular.y.setVal(objectSpecular[1]);
        this.objectSpecular.z.setVal(objectSpecular[2]);
    }

    public void setObjectSpecular(float r, float g, float b) {
        this.objectSpecular.x.setVal(r);
        this.objectSpecular.y.setVal(g);
        this.objectSpecular.z.setVal(b);
    }

    public DirectionLight getDirectionLight() {
        return directionLight;
    }

    public void setDirectionLight(DirectionLight directionLight) {
        TraditionalMaterial.directionLight = directionLight;
    }

    public void setMaxLayerNum(float maxLayerNum) {
        this.maxLayerNum.setVal(maxLayerNum);
    }

    public Float getMaxLayerNum() {
        return maxLayerNum;
    }

    public Float getMinLayerNum() {
        return minLayerNum;
    }

    public void setMinLayerNum(float minLayerNum) {
        this.minLayerNum.setVal(minLayerNum);
    }

    public void setEnableLighting(boolean enableLighting){
        this.enableLighting.setVal(enableLighting);
    }

    public void setShininess(float shininess) {
        this.shininess.setVal(shininess);
    }

    public Float getShininess() {
        return shininess;
    }

    public Texture2D getSpecularMap() {
        return specularMap;
    }

    public void setSpecularMap(Texture2D specularMap) {
        this.specularMap = specularMap;
    }

    public Texture2D getNormalMap() {
        return normalMap;
    }

    public void setNormalMap(Texture2D normalMap) {
        this.normalMap = normalMap;
    }

    public Texture2D getHeightMap() {
        return heightMap;
    }

    public void setHeightMap(Texture2D heightMap) {
        this.heightMap = heightMap;
    }

    public void setUseHeightMapHalfRange(boolean useHeightMapHalfRange) {
        isUseHeightMapHalfRange.setVal(useHeightMapHalfRange);
    }

    public Boolean getIsUseHeightMapHalfRange() {
        return isUseHeightMapHalfRange;
    }

    public void setDiscardHeightMapEdge(boolean discardHeightMapEdge) {
        isDiscardHeightMapEdge.setVal(discardHeightMapEdge);
    }

    public Boolean getDiscardHeightMapEdge() {
        return isDiscardHeightMapEdge;
    }

    public void setHeightScale(float heightScale) {
        this.heightScale.setVal(heightScale);
    }

    public Float getHeightScale() {
        return heightScale;
    }

    public void setShadowMap(Texture shadowMap) {
        this.shadowMap = (DepthBuffer) shadowMap;
    }

    public void setBias(float bias) {
        this.bias = bias;
    }

    public void setIsUseSoftShadow(Boolean isUseSoftShadow) {
        this.isUseSoftShadow = isUseSoftShadow;
    }

    public void setIsUseSoftShadow(boolean bool){
        this.isUseSoftShadow.setVal(bool);
    }

    public void setSoftShadowSampleNum(int softShadowSampleNum) {
        this.softShadowSampleNum = softShadowSampleNum;
    }

    public Boolean getIsUseDiffuseMap() {
        return isUseDiffuseMap;
    }

    public void setIsUseDiffuseMap(boolean isUseDiffuseMap) {
        this.isUseDiffuseMap.setVal(isUseDiffuseMap);
    }

    public Boolean getIsUseSpecularMap() {
        return isUseSpecularMap;
    }

    public void setIsUseSpecularMap(boolean isUseSpecularMap) {
        this.isUseSpecularMap.setVal(isUseSpecularMap);
    }

    public Boolean getIsUseTangentSpace() {
        return isUseTangentSpace;
    }

    public void setIsUseTangentSpace(boolean isUseTangentSpace) {
        this.isUseTangentSpace.setVal(isUseTangentSpace);
    }

    public Boolean getIsUseDisplacementMap() {
        return isUseDisplacementMap;
    }

    public void setIsUseDisplacementMap(boolean isUseDisplacementMap) {
        this.isUseDisplacementMap.setVal(isUseDisplacementMap);
    }
}
