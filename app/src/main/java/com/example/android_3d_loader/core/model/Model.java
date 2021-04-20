package com.example.android_3d_loader.core.model;

import com.example.android_3d_loader.core.camera.BaseCamera;
import com.example.android_3d_loader.core.dataType.Matrix4;
import com.example.android_3d_loader.core.dataType.Vector3;
import com.example.android_3d_loader.core.material.Material;
import com.example.android_3d_loader.core.model.mesh.Mesh;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class Model extends Mesh {

    @Expose
    private String modelPath;

    public Model(String modelName) {
        super(modelName);
        this.meshes = new ArrayList<>();
    }

    public void createWireFrameMesh(){
        for (Mesh mesh: meshes){
            mesh.createWireFrameMesh();
        }
    }

    public void setFacetTypeToAll(int type){
        for (Mesh mesh: meshes){
            mesh.getFacetType().setVal(type);
        }
    }

    public void draw(Matrix4 modelMatrix, Matrix4 viewMatrix, Matrix4 projectionMatrix, Vector3 viewPosition){
        for (Mesh mesh: meshes){
            mesh.draw(modelMatrix, viewMatrix, projectionMatrix, viewPosition);
        }
    }

    public void draw(BaseCamera camera, Matrix4 modelMatrix){
        for (Mesh mesh: meshes){
            mesh.draw(camera, modelMatrix);
        }
    }

    public void setCurrentMaterialToAll(Material material){
        for (Mesh mesh: meshes){
            mesh.setCurrentMaterial(material);
        }
    }

    public void setOriginalMaterialToAll(Material material){
        for (Mesh mesh: meshes){
            mesh.setOriginalMaterial(material);
        }
    }

    public void addMesh(Mesh mesh){
        this.meshes.add(mesh);
    }

    public int getTotalVerticesNum(){
        int sum = 0;
        for (Mesh mesh: meshes){
            sum += mesh.getVerticesNum();
        }
        return sum;
    }

    public boolean isHaveUV(){
        for (Mesh mesh: meshes){
            if (mesh.getArrayDataType().equals(ArrayDataType.VERTEX_TEXCOORD) || mesh.getArrayDataType().equals(ArrayDataType.VERTEX_TEXCOORD_NORMAL)){
                return true;
            }
        }
        return false;
    }

    public boolean isHaveNormal(){
        for (Mesh mesh: meshes){
            if (mesh.getArrayDataType().equals(ArrayDataType.VERTEX_NORMAL) || mesh.getArrayDataType().equals(ArrayDataType.VERTEX_TEXCOORD_NORMAL)){
                return true;
            }
        }
        return false;
    }

    public String getModelPath() {
        return modelPath;
    }

    public void setModelPath(String modelPath) {
        this.modelPath = modelPath;
    }
}
