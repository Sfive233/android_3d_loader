package com.example.android_3d_loader.core.material;

import android.content.Context;

import com.example.android_3d_loader.core.dataType.Matrix4;
import com.example.android_3d_loader.core.texture.Texture;
import com.example.android_3d_loader.core.dataType.Vector3;
import com.example.android_3d_loader.core.material.shader.Shader;
import com.google.gson.annotations.Expose;

import java.util.HashMap;

public abstract class Material {
    protected Context context;
    protected Matrix4 modelMatrix = new Matrix4();
    protected Matrix4 viewMatrix = new Matrix4();
    protected Matrix4 projectionMatrix = new Matrix4();
    protected static Matrix4 identityMatrix = new Matrix4();
    protected Vector3 viewPosition = new Vector3();
    private static HashMap<Integer, Shader> shaderInstanceMap;
    protected Shader shader;
    @Expose
    protected int vertexRes;
    @Expose
    protected int fragmentRes;

    public Material() {
    }

    public Material(Context context, int vertexRes, int fragmentRes){
        this.context = context;
        this.vertexRes = vertexRes;
        this.fragmentRes = fragmentRes;

        loadShader(context);
    }

    public void loadShader(Context context){
        if (context == null){
            throw new NullPointerException("Context is null");
        }
        this.context = context;
        if (shaderInstanceMap == null){
            shaderInstanceMap = new HashMap<>();
        }
        if (!shaderInstanceMap.containsKey(vertexRes)){
            Shader newShader = new Shader(context, vertexRes, fragmentRes);
            shaderInstanceMap.put(vertexRes, newShader);
        }
        this.shader = shaderInstanceMap.get(vertexRes);
//        this.shader = new Shader(context, vertexRes, fragmentRes);
        identityMatrix.setIdentity();
        Texture.createNullTexture(context);
    }

    public void init(){};
    public abstract void update();
    public void useMaterial(){
        shader.use();
        update();
        shader.setTexCountToOne();
    }

    protected boolean isDisableModelMatrix;
    public void setShaderModelMatrix(float[] modelMatrix) {
        shader.use();
        shader.setModelMatrix(isDisableModelMatrix? identityMatrix.getVal(): modelMatrix);
        this.modelMatrix.setVal(modelMatrix);
    }
    public void setShaderModelMatrix(Matrix4 modelMatrix) {
        shader.use();
        shader.setModelMatrix(isDisableModelMatrix? identityMatrix: modelMatrix);
        this.modelMatrix = modelMatrix;
    }

    public void setDisableModelMatrix(boolean disableModelMatrix) {
        isDisableModelMatrix = disableModelMatrix;
    }

    protected boolean isDisableViewMatrix;
    public void setShaderViewMatrix(Matrix4 viewMatrix) {
        shader.use();
        shader.setViewMatrix(isDisableViewMatrix? identityMatrix.getVal(): viewMatrix.getVal());
        this.viewMatrix.setVal(viewMatrix.getVal());
    }

    public void setDisableViewMatrix(boolean disableViewMatrix) {
        isDisableViewMatrix = disableViewMatrix;
    }

    protected boolean isDisableProjectionMatrix;
    public void setShaderProjectionMatrix(Matrix4 projectionMatrix) {
        shader.use();
        shader.setProjectionMatrix(isDisableProjectionMatrix? identityMatrix.getVal(): projectionMatrix.getVal());
        this.projectionMatrix.setVal(projectionMatrix.getVal());
    }

    public void setViewPosition(Vector3 viewPosition) {
//        shader.use();
//        shader.setViewPosition(viewPosition);
        this.viewPosition = viewPosition;
    }

    public void setDisableProjectionMatrix(boolean disableProjectionMatrix) {
        isDisableProjectionMatrix = disableProjectionMatrix;
    }

    public void regainShader(Context context){
        modelMatrix = new Matrix4();
        viewMatrix = new Matrix4();
        projectionMatrix = new Matrix4();
        viewPosition = new Vector3();
        identityMatrix = new Matrix4();
        loadShader(context);
    }

    public Shader getShader() {
        return shader;
    }
}
