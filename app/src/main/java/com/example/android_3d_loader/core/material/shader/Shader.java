package com.example.android_3d_loader.core.material.shader;

import android.content.Context;
import android.opengl.GLES30;

import com.example.android_3d_loader.core.dataType.Boolean;
import com.example.android_3d_loader.core.dataType.Float;
import com.example.android_3d_loader.core.dataType.Matrix4;
import com.example.android_3d_loader.core.texture.texture2D.Texture2D;
import com.example.android_3d_loader.core.dataType.Vector3;
import com.example.android_3d_loader.core.texture.Texture;
import com.example.android_3d_loader.core.util.ShaderHelper;

public class Shader {
    protected Context context;
    protected int program;
    protected int texCount = 1;

    public Shader(Context context, int vertexRes, int fragmentRes){
        this.context = context;
        program = ShaderHelper.buildProgram(context, vertexRes, fragmentRes);
    }

    public Shader(String vertexSource, String fragmentSource){
        program = ShaderHelper.buildProgram(vertexSource, fragmentSource);
    }

    public void use(){
        GLES30.glUseProgram(program);
    }

    public void setModelMatrix(float[] model){
        setMat4("Model", model);
    }

    public void setModelMatrix(Matrix4 modelMatrix){
        if (modelMatrix != null){
            setMat4("Model", modelMatrix.getVal());
        }
    }

    public void setViewMatrix(float[] view){
        setMat4("View", view);
    }

    public void setViewMatrix(Matrix4 viewMatrix){
        setMat4("View", viewMatrix.getVal());
    }

    public void setProjectionMatrix(float[] projection){
        setMat4("Projection", projection);
    }

    public void setProjectionMatrix(Matrix4 projectionMatrix){
        setMat4("Projection", projectionMatrix.getVal());
    }

    public void setFloat(String location, float value){
        GLES30.glUniform1f(GLES30.glGetUniformLocation(program, location), value);
    };

    public void setFloat(String location, Float value){
        GLES30.glUniform1f(GLES30.glGetUniformLocation(program, location), value.getVal());
    };

    public void setVec2(String location, float[] value){
        GLES30.glUniform2fv(GLES30.glGetUniformLocation(program, location),1,  value, 0);
    }

    public void setVec3(String location, float[] value){
        GLES30.glUniform3fv(GLES30.glGetUniformLocation(program, location),1,  value, 0);
    }

    public void setVec3(String location, Vector3 value){
        GLES30.glUniform3fv(GLES30.glGetUniformLocation(program, location),1,  value.getOriginalFloats(), 0);
    }

    public void setVec3(String location, float r, float g, float b){
        GLES30.glUniform3fv(GLES30.glGetUniformLocation(program, location),1,  new float[]{r, g, b}, 0);
    }

    public void setInt(String location, float value){
        GLES30.glUniform1f(GLES30.glGetUniformLocation(program, location), value);
    }

    public void setInt(String location, int value){
        GLES30.glUniform1i(GLES30.glGetUniformLocation(program, location), value);
    }

    public void setBool(String location, boolean value){
        GLES30.glUniform1i(GLES30.glGetUniformLocation(program, location), value? 1: 0);
    }

    public void setBool(String location, Boolean value){
        GLES30.glUniform1i(GLES30.glGetUniformLocation(program, location), value.getVal()? 1: 0);
    }

    public void setMat4(String location, float[] value){
        GLES30.glUniformMatrix4fv(GLES30.glGetUniformLocation(program, location), 1, false, value, 0);
    }

    public void setTexture(String location, Texture texture, int number){
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0+number);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texture.getTex());
        this.setInt(location, number);
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
    }

    public void setTexture(String location, Texture texture){
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0 + texCount);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texture.getTex());
        this.setInt(location, texCount);
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        texCount++;
    }

    public void setTexture(String location, Texture2D texture2D){
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0 + texCount);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texture2D.getTex());
        this.setInt(location, texCount);
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        texCount++;
    }

    public void setCubeMap(String location, Texture cubeMap, int number){
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0+number);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_CUBE_MAP, cubeMap.getTex());
        this.setInt(location, number);
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
    }

    public void setCubeMap(String location, Texture cubeMap){
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0+texCount);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_CUBE_MAP, cubeMap.getTex());
        this.setInt(location, texCount);
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        texCount++;
    }

    public int getProgram() {
        return program;
    }

    public void setProgram(int program) {
        this.program = program;
    }

    public void setTexCountToOne(){
        texCount = 1;
    }
}
