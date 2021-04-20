package com.example.android_3d_loader.core.model.obj;

import android.content.Context;

import com.example.android_3d_loader.core.exception.TextureNotFoundException;
import com.example.android_3d_loader.core.material.TraditionalMaterial;
import com.example.android_3d_loader.core.model.mesh.Mesh;
import com.example.android_3d_loader.core.model.Model;
import com.example.android_3d_loader.core.model.ModelReader;
import com.example.android_3d_loader.core.model.util.ModelLog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.example.android_3d_loader.core.model.mesh.Mesh.TYPE_QUADS;
import static com.example.android_3d_loader.core.model.mesh.Mesh.TYPE_TRIANGLES;

public class ReaderOBJ extends ModelReader {
    protected final String VERTEX = "v";
    protected final String FACE = "f";
    protected final String TEXCOORD = "vt";
    protected final String NORMAL = "vn";
    protected final String OBJECT = "o";
    protected final String GROUP = "g";
    protected final String MATERIAL_LIB = "mtllib";
    protected final String USE_MATERIAL = "usemtl";
    protected final String NEW_MATERIAL = "newmtl";
    protected final String DIFFUSE_COLOR = "Kd";
    protected final String DIFFUSE_TEX_MAP = "map_Kd";
    protected Model model;

    public ReaderOBJ(Context context, String path) {
        super(context, path);
    }

    @Override
    public Model parse() throws TextureNotFoundException {
        model = new Model(super.modelName);
        List<UniqueMesh> meshes = new ArrayList<>();
        String tempObjectName = "default";
        UniqueMesh firstMesh = new UniqueMesh(tempObjectName);
        UniqueVertex.count = 0;
        firstMesh.mtl = new MTL("default");
        meshes.add(firstMesh);
        int currentMeshIndex = 0;
        boolean isFirstMesh = true;
        boolean isNoUV = false;
        boolean isNoNormal = false;
        HashMap<String, MTL> modelMTL = new HashMap<>();

        // 创建读取器
        BufferedReader bufferedReader = null;
        try {
            // 判断资源是来自外部还是内部的
            if (path.startsWith("/")) {
                File file = new File(path);
                FileReader fileReader = new FileReader(file);
                bufferedReader = new BufferedReader(fileReader);
            } else {
                InputStream inputStream = context.getAssets().open(path);
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        String currentLine;
        // 开始逐行扫描
        try {
            while (((currentLine = bufferedReader.readLine()) != null)) {
                if (currentLine.length() == 0 || currentLine.charAt(0) == '#') {
                    continue;
                }
                String[] subStr = currentLine.split("\\s+");
                if (subStr.length == 0) {
                    continue;
                }
                switch (subStr[0]) {
                    case MATERIAL_LIB:
                        ModelLog.d(Arrays.toString(subStr));
                        StringBuilder mtlFileName = new StringBuilder(subStr[1]);
                        if (subStr.length > 2) {
                            for (int i = 2; i < subStr.length; i++) {
                                mtlFileName.append(" ").append(subStr[i]);
                            }
                        }
                        ReaderMTL readerMTL = new ReaderMTL(context, directory, mtlFileName.toString());
                        modelMTL = readerMTL.parse();
                        break;
                    case OBJECT:
                        ModelLog.d(Arrays.toString(subStr));
                        tempObjectName = subStr[1];
                        break;
                    case USE_MATERIAL:
                        ModelLog.d(Arrays.toString(subStr));
                        if (isFirstMesh) {
                            meshes.clear();
                            isFirstMesh = false;
                        } else {
                            currentMeshIndex++;
                            isNoUV = false;
                            isNoNormal = false;
                        }
                        // 一个网格可能同时使用多个mtl
                        meshes.add(new UniqueMesh(tempObjectName + "_" + subStr[1]));
                        UniqueVertex.count = 0;
                        if (null == modelMTL) {
                            meshes.get(currentMeshIndex).mtl = new MTL(subStr[1]);
                        } else {
                            meshes.get(currentMeshIndex).mtl = modelMTL.get(subStr[1]);
                        }
                        break;
                    case VERTEX:
                        ModelLog.d(Arrays.toString(subStr));
                        // 收集顶点
                        vertices.add(Float.parseFloat(subStr[1]));
                        vertices.add(Float.parseFloat(subStr[2]));
                        vertices.add(Float.parseFloat(subStr[3]));
                        break;
                    case TEXCOORD:
                        ModelLog.d(Arrays.toString(subStr));
                        // 收集纹理坐标
                        texcoords.add(Float.parseFloat(subStr[1]));
                        texcoords.add(1.0f - Float.parseFloat(subStr[2]));
                        break;
                    case NORMAL:
                        ModelLog.d(Arrays.toString(subStr));
                        // 收集法线
                        normals.add(Float.parseFloat(subStr[1]));
                        normals.add(Float.parseFloat(subStr[2]));
                        normals.add(Float.parseFloat(subStr[3]));
                        break;
                    case FACE:// 收集面
                        ModelLog.d(Arrays.toString(subStr));
                        List<Integer> tempQuadIndices = new ArrayList<>();
                        for (int i = 1; i < subStr.length; i++) {// 遍历每组顶点索引

                            int slashNum = countChar(subStr[i], "/");
                            isNoUV = slashNum == 0 || subStr[i].contains("//");
                            isNoNormal = slashNum <= 1;

                            if (meshes.get(currentMeshIndex).vertices.containsKey(subStr[i])) {
                                tempQuadIndices.add(meshes.get(currentMeshIndex).vertices.get(subStr[i]).index);
                            } else {
                                String[] subPart = subStr[i].split("/");

                                // 获取索引
                                int vertexIndex = (Integer.parseInt(subPart[0]) - 1) * 3;
                                int texcoordIndex = isNoUV ? -1 : (Integer.parseInt(subPart[1]) - 1) * 2;
                                int normalIndex = isNoNormal ? -1 : (Integer.parseInt(subPart[2]) - 1) * 3;

                                // 处理负值索引
                                vertexIndex = vertexIndex < 0 ? vertices.size() + vertexIndex + 3 : vertexIndex;
                                texcoordIndex = isNoUV ? texcoordIndex : (texcoordIndex < 0 ? texcoords.size() + texcoordIndex + 2 : texcoordIndex);
                                normalIndex = isNoNormal ? normalIndex : (normalIndex < 0 ? normals.size() + normalIndex + 3 : normalIndex);

                                // 每新建一个独一无二的顶点时，会自动累加顶点数，以作为该顶点的序号。
                                UniqueVertex tempUniqueVertex = new UniqueVertex(
                                        vertices.get(vertexIndex),
                                        vertices.get(vertexIndex + 1),
                                        vertices.get(vertexIndex + 2),
                                        isNoUV ? 0 : texcoords.get(texcoordIndex),
                                        isNoUV ? 0 : texcoords.get(texcoordIndex + 1),
                                        isNoNormal ? 0 : normals.get(normalIndex),
                                        isNoNormal ? 0 : normals.get(normalIndex + 1),
                                        isNoNormal ? 0 : normals.get(normalIndex + 2)
                                );
                                meshes.get(currentMeshIndex).vertices.put(subStr[i], tempUniqueVertex);
                                tempQuadIndices.add(tempUniqueVertex.index);
                            }
                        }
                        UniqueFace currentFace;
                        // 如果当前面只有三组顶点索引，则处理为一个三角面，四组为2个三角面，五组为3个，按n-2类推
                        for (int i = 2; i < tempQuadIndices.size(); i++) {
                            currentFace = new UniqueFace(3);
                            currentFace.addIndex(tempQuadIndices.get(0));
                            currentFace.addIndex(tempQuadIndices.get(i - 1));
                            currentFace.addIndex(tempQuadIndices.get(i));
                            meshes.get(currentMeshIndex).faces.add(currentFace);
                            meshes.get(currentMeshIndex).isQuad = false;
                            meshes.get(currentMeshIndex).isNoUV = isNoUV;
                            meshes.get(currentMeshIndex).isNoNormal = isNoNormal;
                        }
                        break;
                }
            }
            bufferedReader.close();
        }catch (IOException e){
            e.printStackTrace();
        }

        for (UniqueMesh mesh: meshes){// 汇总
            model.addMesh(assembleData(mesh));
        }

        return model;
    }

    private Mesh assembleData(UniqueMesh mesh){
        float[] vertices = new float[mesh.vertices.size() * 3];
        float[] texcoords = new float[mesh.vertices.size() * 2];
        float[] normals = new float[mesh.vertices.size() * 3];
        int[] indices = new int[mesh.faces.size() * (mesh.isQuad? 4: 3)];
        int i = 0;
        for(Map.Entry<String, UniqueVertex> entry: mesh.vertices.entrySet()){
            UniqueVertex tempVertex = entry.getValue();
            int j = 0;
            vertices[i*3] = tempVertex.vertex[j++];
            vertices[i*3+1] = tempVertex.vertex[j++];
            vertices[i*3+2] = tempVertex.vertex[j++];
            texcoords[i*2] = tempVertex.vertex[j++];
            texcoords[i*2+1] = tempVertex.vertex[j++];
            normals[i*3] = tempVertex.vertex[j++];
            normals[i*3+1] = tempVertex.vertex[j++];
            normals[i*3+2] = tempVertex.vertex[j];
            i++;
        }
        i = 0;
        int spacing = mesh.isQuad? 4: 3;
        for (UniqueFace tempFace: mesh.faces){
            indices[i*spacing] = tempFace.vertexIndices[0];
            indices[i*spacing+1] = tempFace.vertexIndices[1];
            indices[i*spacing+2] = tempFace.vertexIndices[2];
            if (mesh.isQuad){
                indices[i*spacing+3] = tempFace.vertexIndices[3];
            }
            i++;
        }
        int type = mesh.isQuad? TYPE_QUADS: TYPE_TRIANGLES;
        Mesh finalMesh = null;
        if (mesh.isNoUV && mesh.isNoNormal){
            finalMesh = new Mesh(mesh.name, vertices, texcoords, normals, indices, type, Mesh.ArrayDataType.VERTEX);
        }else if (mesh.isNoNormal){
            finalMesh = new Mesh(mesh.name, vertices, texcoords, normals, indices, type, Mesh.ArrayDataType.VERTEX_TEXCOORD);
        }else if(mesh.isNoUV){
            finalMesh = new Mesh(mesh.name, vertices, texcoords, normals, indices, type, Mesh.ArrayDataType.VERTEX_NORMAL);
        }else{
            finalMesh = new Mesh(mesh.name, vertices, texcoords, normals, indices, type, Mesh.ArrayDataType.VERTEX_TEXCOORD_NORMAL);
        }
        // 计算网格最大点最小点
        finalMesh.setMaxX(vertices[0]);
        finalMesh.setMaxY(vertices[1]);
        finalMesh.setMaxZ(vertices[2]);
        finalMesh.setMinX(vertices[0]);
        finalMesh.setMinY(vertices[1]);
        finalMesh.setMinZ(vertices[2]);
        for (int j = 3; j < vertices.length; j+=3){
            finalMesh.setMaxX(Math.max(finalMesh.getMaxX(), vertices[j]));
            finalMesh.setMaxY(Math.max(finalMesh.getMaxY(), vertices[j + 1]));
            finalMesh.setMaxZ(Math.max(finalMesh.getMaxZ(), vertices[j + 2]));
            finalMesh.setMinX(Math.min(finalMesh.getMinX(), vertices[j]));
            finalMesh.setMinY(Math.min(finalMesh.getMinY(), vertices[j + 1]));
            finalMesh.setMinZ(Math.min(finalMesh.getMinZ(), vertices[j + 2]));
        }


        MTL mtl = mesh.getMtl();
        TraditionalMaterial textureMaterial = new TraditionalMaterial(context);
        if (mtl != null) {
            textureMaterial.setDiffuseMap(mtl.getMap_Kd());
            textureMaterial.setSpecularMap(mtl.getMap_Ks());
            textureMaterial.setNormalMap(mtl.getMap_Bump());
            textureMaterial.setObjectColor(mtl.getKd());
            textureMaterial.setObjectSpecular(mtl.getKs());
        }
        finalMesh.setOriginalMaterial(textureMaterial);
        return finalMesh;
    }



    private int countChar(String sourceStr, String c){
        int count = 0;
        if(sourceStr.contains(c)){
            count++;
            count += countChar(sourceStr.substring(sourceStr.indexOf(c)+1), c);
        }else {
            return count;
        }
        return count;
    }

    private static class UniqueVertex{
        static int count = 0;
        int index;
        float[] vertex;
        public UniqueVertex(float verX, float verY, float verZ,  float texS, float texT,
                                float norX, float norY, float norZ) {
            this.vertex = new float[]{ verX, verY, verZ, texS, texT, norX, norY, norZ };
            index = count;
            count++;
        }
    }
    static class UniqueFace{
        int[] vertexIndices;
        int i = 0;
        public UniqueFace(int vertexNum) {
            this.vertexIndices = new int[vertexNum];
        }
        public void addIndex(int index){
            this.vertexIndices[i++] = index;
        }
    }
    static class UniqueMesh{
        String name;
        LinkedHashMap<String, UniqueVertex> vertices;
        List<UniqueFace> faces;
        MTL mtl;
        boolean isQuad = false;
        boolean isNoUV = false;
        boolean isNoNormal = false;

        public UniqueMesh(String name){
            this.name = name;
            vertices = new LinkedHashMap<>();
            faces = new ArrayList<>();
        }
        public void appendName(String name){
            this.name = this.name + "_" + name;
        }
        public LinkedHashMap<String, UniqueVertex> getVertices() {
            return vertices;
        }
        public void setVertices(LinkedHashMap<String, UniqueVertex> vertices) {
            this.vertices = vertices;
        }
        public List<UniqueFace> getFaces() {
            return faces;
        }
        public void setFaces(List<UniqueFace> faces) {
            this.faces = faces;
        }
        public boolean isQuad() {
            return isQuad;
        }
        public void setQuad(boolean quad) {
            isQuad = quad;
        }
        public MTL getMtl() {
            return mtl;
        }
        public void setMtl(MTL mtl) {
            this.mtl = mtl;
        }
    }
}
