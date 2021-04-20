package com.example.android_3d_loader.core.model.stl;

import android.content.Context;
import android.util.Log;

import com.example.android_3d_loader.core.material.TraditionalMaterial;
import com.example.android_3d_loader.core.model.mesh.Mesh;
import com.example.android_3d_loader.core.model.Model;
import com.example.android_3d_loader.core.model.ModelReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.android_3d_loader.core.model.mesh.Mesh.TYPE_TRIANGLES;

public class ReaderSTL extends ModelReader {
    protected Model model;

    public ReaderSTL(Context context, String path) {
        super(context, path);
    }

    @Override
    public Model parse() {

        try {
            BufferedReader bufferedReader;
            if (path.startsWith("/")) {
                File file = new File(path);
                FileReader fileReader = new FileReader(file);
                bufferedReader = new BufferedReader(fileReader);
            } else {
                InputStream inputStream = context.getAssets().open(path);
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);
            }

            String currentLine;
            if ((currentLine = bufferedReader.readLine()) != null){
                if (currentLine.contains("solid")){
                    String[] subStr = currentLine.split(" ");
                    model = readASCII(bufferedReader, subStr[1]);
                }else {
                    bufferedReader.close();
                    model = readBin(path);
                }
            }
            bufferedReader.close();
            return model;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private Model readBin(String path) throws IOException {
        InputStream inputStream;
        if (path.startsWith("/")){
            File file = new File(path);
            inputStream = new FileInputStream(file);
        }else {
            inputStream = context.getAssets().open(path);
        }

        inputStream.skip(80);// 跳过文件头部分

        byte[] bytes = new byte[4];
        inputStream.read(bytes);// 获取三角面片数
        int facetCount = byteToInt(bytes, 0);

        bytes = new byte[50 * facetCount];
        inputStream.read(bytes);// 获取所有三角面片信息
        inputStream.close();// 关闭流

        float[] vertices = new float[facetCount * 3 * 3];
        float[] normals = new float[vertices.length];
        float[] uvs = new float[facetCount * 3 * 2];
        int[] indices = new int[facetCount * 3];
        int offset = 0;
        int index = 0;

        float maxX = 0;
        float maxY = 0;
        float maxZ = 0;
        float minX = 0;
        float minY = 0;
        float minZ = 0;

        for (int i = 0; i < facetCount; i++){// 遍历每个面
            for (int j = 0; j < 4; j++){// 遍历每个顶点
                float x = byteToFloat(bytes, offset);
                float y = byteToFloat(bytes, offset + 4);
                float z = byteToFloat(bytes, offset + 8);
                offset += 12;

                if (j == 0){// 第一个是法线
                    normals[i * 9] = x;
                    normals[i * 9 + 1] = y;
                    normals[i * 9 + 2] = z;
                    normals[i * 9 + 3] = x;
                    normals[i * 9 + 4] = y;
                    normals[i * 9 + 5] = z;
                    normals[i * 9 + 6] = x;
                    normals[i * 9 + 7] = y;
                    normals[i * 9 + 8] = z;
                }else if (j == 1){// 剩下的是顶点坐标
                    vertices[i * 9] = x;
                    vertices[i * 9 + 1] = y;
                    vertices[i * 9 + 2] = z;
                    maxX = Math.max(maxX, x);
                    maxY = Math.max(maxY, y);
                    maxZ = Math.max(maxZ, z);
                    minX = Math.min(minX, x);
                    minY = Math.min(minY, y);
                    minZ = Math.min(minZ, z);
                }else if (j == 2){
                    vertices[i * 9 + 3] = x;
                    vertices[i * 9 + 4] = y;
                    vertices[i * 9 + 5] = z;
                    maxX = Math.max(maxX, x);
                    maxY = Math.max(maxY, y);
                    maxZ = Math.max(maxZ, z);
                    minX = Math.min(minX, x);
                    minY = Math.min(minY, y);
                    minZ = Math.min(minZ, z);
                }else {
                    vertices[i * 9 + 6] = x;
                    vertices[i * 9 + 7] = y;
                    vertices[i * 9 + 8] = z;
                    maxX = Math.max(maxX, x);
                    maxY = Math.max(maxY, y);
                    maxZ = Math.max(maxZ, z);
                    minX = Math.min(minX, x);
                    minY = Math.min(minY, y);
                    minZ = Math.min(minZ, z);

                    // todo 顶点索引待优化
                    indices[i * 3] = index++;
                    indices[i * 3 + 1] = index++;
                    indices[i * 3 + 2] = index++;
                }
            }

            offset += 2;
        }

        String name = "default";
        Mesh mesh = new Mesh(name, vertices, uvs, normals, indices, TYPE_TRIANGLES);
//        mesh.setOriginalMaterial(new SimpleMaterial(context, SimpleMaterial.COLOR_NORMAL));
        mesh.setMaxX(maxX);
        mesh.setMaxY(maxY);
        mesh.setMaxZ(maxZ);
        mesh.setMinX(minX);
        mesh.setMinY(minY);
        mesh.setMinZ(minZ);
        mesh.setOriginalMaterial(new TraditionalMaterial(context));
        model = new Model(super.modelName);
        model.addMesh(mesh);
        return model;
    }

    private int byteToInt(byte[] bytes, int offset){
        int b3 = bytes[offset + 3] & 0xFF;
        int b2 = bytes[offset + 2] & 0xFF;
        int b1 = bytes[offset + 1] & 0xFF;
        int b0 = bytes[offset] & 0xFF;
        return (b3 << 24) | (b2 << 16) | (b1 << 8) | b0;
    }

    private float byteToFloat(byte[] bytes, int offset){
        return Float.intBitsToFloat(byteToInt(bytes, offset));
    }

    protected final String SOLID = "solid";
    protected final String FACET = "facet";
    protected final String OUTER = "outer";
    protected final String VERTEX = "vertex";
    protected final String END_LOOP = "endloop";
    protected final String END_FACET = "endfacet";
    protected final String END_SOLID = "endsolid";
    static class STLVertex{
        float x, y, z;
    }
    static class STLFace{
        List<STLVertex> vertices = new ArrayList<>();
        float[] normal = new float[3];
        public void addVertex(STLVertex stlVertex){
            vertices.add(stlVertex);
        }
    }
    private Model readASCII(BufferedReader bufferedReader, String meshName) throws Exception{
        List<STLFace> faceList = new ArrayList<>();
        STLFace tempFace = new STLFace();
        String currentLine;
        while (((currentLine = bufferedReader.readLine()) != null)){
            if(currentLine.length() == 0 || currentLine.charAt(0) == '#') {
                continue;
            }
            if (currentLine.indexOf(" ") == 0){
                currentLine = currentLine.replaceFirst("\\s+", "");
            }
            String[] subStr = currentLine.split("\\s+");
            if (subStr.length == 0){
                continue;
            }
            switch (subStr[0]){
                case FACET:
                    Log.d(TAG, "readASCII: "+ Arrays.toString(subStr));
                    tempFace.normal = new float[]{
                            Float.parseFloat(subStr[2]),
                            Float.parseFloat(subStr[3]),
                            Float.parseFloat(subStr[4])
                    };
                    break;
                case OUTER:
                    Log.d(TAG, "readASCII: "+ Arrays.toString(subStr));
                    break;
                case VERTEX:
                    Log.d(TAG, "readASCII: "+ Arrays.toString(subStr));
                    STLVertex stlVertex = new STLVertex();
                    stlVertex.x = Float.parseFloat(subStr[1]);
                    stlVertex.y = Float.parseFloat(subStr[2]);
                    stlVertex.z = Float.parseFloat(subStr[3]);
                    tempFace.addVertex(stlVertex);
                    break;
                case END_LOOP:
                    Log.d(TAG, "readASCII: "+ Arrays.toString(subStr));
                    break;
                case END_FACET:
                    Log.d(TAG, "readASCII: "+ Arrays.toString(subStr));
                    faceList.add(tempFace);
                    tempFace = new STLFace();
                    break;
                case END_SOLID:
                    Log.d(TAG, "readASCII: "+ Arrays.toString(subStr));

                    //todo 优化网格数据
                    float[] resultVertices = new float[faceList.size() * 3 * 3];
                    float[] resultNormals = new float[resultVertices.length];
                    float[] uvs = new float[faceList.size() * 3 * 2];
                    int[] indices = new int[faceList.size() * 3];
                    int i = 0;

                    float maxX = faceList.get(0).vertices.get(0).x;
                    float maxY = faceList.get(0).vertices.get(0).y;
                    float maxZ = faceList.get(0).vertices.get(0).z;
                    float minX = faceList.get(0).vertices.get(0).x;
                    float minY = faceList.get(0).vertices.get(0).y;
                    float minZ = faceList.get(0).vertices.get(0).z;
                    for (STLFace face: faceList){
                        for (STLVertex vertex: face.vertices){
                            resultVertices[i] = vertex.x;
                            resultVertices[i + 1] = vertex.y;
                            resultVertices[i + 2] = vertex.z;

                            maxX = Math.max(maxX, vertex.x);
                            maxY = Math.max(maxY, vertex.y);
                            maxZ = Math.max(maxZ, vertex.z);
                            minX = Math.min(minX, vertex.x);
                            minY = Math.min(minY, vertex.y);
                            minZ = Math.min(minZ, vertex.z);

                            resultNormals[i] = face.normal[0];
                            resultNormals[i + 1] = face.normal[1];
                            resultNormals[i + 2] = face.normal[2];
                            indices[i / 3] = i / 3;
                            i+=3;
                        }
                    }
                    Mesh mesh = new Mesh(meshName, resultVertices, uvs, resultNormals, indices, TYPE_TRIANGLES);
                    mesh.setMaxX(maxX);
                    mesh.setMaxY(maxY);
                    mesh.setMaxZ(maxZ);
                    mesh.setMinX(minX);
                    mesh.setMinY(minY);
                    mesh.setMinZ(minZ);
                    mesh.setOriginalMaterial(new TraditionalMaterial(context));
                    model = new Model(super.modelName);
                    model.addMesh(mesh);
                    break;
            }
        }
        return model;
    }
}
