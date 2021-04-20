package com.example.android_3d_loader.core.model.mesh;

import android.opengl.GLES20;
import android.opengl.GLES30;

import com.example.android_3d_loader.core.camera.BaseCamera;
import com.example.android_3d_loader.core.dataType.Boolean;
import com.example.android_3d_loader.core.dataType.Int;
import com.example.android_3d_loader.core.dataType.Matrix4;
import com.example.android_3d_loader.core.dataType.Vector3;
import com.example.android_3d_loader.core.material.Material;
import com.example.android_3d_loader.core.util.BufferHelper;
import com.example.android_3d_loader.core.util.Geometry;
import com.google.gson.annotations.Expose;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.android_3d_loader.core.model.Constants.BI_TANGENT_SIZE;
import static com.example.android_3d_loader.core.model.Constants.BI_TANGENT_STRIDE;
import static com.example.android_3d_loader.core.model.Constants.BYTES_PER_FLOAT;
import static com.example.android_3d_loader.core.model.Constants.BYTES_PER_INT;
import static com.example.android_3d_loader.core.model.Constants.NORMAL_SIZE;
import static com.example.android_3d_loader.core.model.Constants.NORMAL_STRIDE;
import static com.example.android_3d_loader.core.model.Constants.POSITION_SIZE;
import static com.example.android_3d_loader.core.model.Constants.POSITION_STRIDE;
import static com.example.android_3d_loader.core.model.Constants.TANGENT_SIZE;
import static com.example.android_3d_loader.core.model.Constants.TANGENT_STRIDE;
import static com.example.android_3d_loader.core.model.Constants.TEXCOORD_SIZE;
import static com.example.android_3d_loader.core.model.Constants.TEXCOORD_STRIDE;

public class Mesh {
    public enum ArrayDataType{
        VERTEX,
        VERTEX_TEXCOORD,
        VERTEX_NORMAL,
        VERTEX_TEXCOORD_NORMAL,
        VERTEX_COLOR,
    }
    private static final String TAG = "Mesh";
    public static final int TYPE_TRIANGLES = GLES30.GL_TRIANGLES;
    public static final int TYPE_QUADS = GLES30.GL_TRIANGLE_STRIP;// 三角形带是连续不独立的
    public static final int TYPE_POINTS = GLES30.GL_POINTS;
    public static final int TYPE_LINES = GLES30.GL_LINES;
    @Expose
    protected String name;
    protected float[] vertices;
    protected float[] texcoords;
    protected float[] normals;
    protected float[] tangents;
    protected float[] biTangents;
    protected int[] indices;
    protected int vao;
    @Expose
    protected Int facetType = new Int(TYPE_TRIANGLES);
    protected Material originalMaterial;
    protected Material currentMaterial;
    protected ArrayDataType arrayDataType;
    protected boolean isNoUV = false;
    protected boolean isNoNormal = false;

    protected float maxX;
    protected float maxY;
    protected float maxZ;
    protected float minX;
    protected float minY;
    protected float minZ;
    protected float[] centerPosition = new float[3];
    protected boolean isCalCenter = false;
    protected float longestSide;
    protected boolean isCalLong = false;

    @Expose
    protected List<Mesh> meshes = new ArrayList<>();
    @Expose
    protected Boolean isVisible = new Boolean(true);

    protected Mesh wireFrameMesh;
    @Expose
    protected Boolean isShowWireFrame = new Boolean(false);
    @Expose
    protected Vector3 rotation = new Vector3();
    @Expose
    protected Vector3 scale = new Vector3(1.0f, 1.0f, 1.0f);

    public Mesh(){}

    public Mesh(String name){
        this.name = name;
    }

    public Mesh(String name, float[] vertices, float[] texcoords, float[] normals, int[] indices, int facetType){
        this.name = name;
        this.vertices = vertices;
        this.texcoords = texcoords;
        this.normals = normals;
        this.indices = indices;
        this.facetType.setVal(facetType);
        this.arrayDataType = ArrayDataType.VERTEX_TEXCOORD_NORMAL;
        calculateTangent();
        checkArrayData();
        smoothNormal();
        setUpMesh();
    }

    public Mesh(String name, float[] vertices, float[] texcoords, float[] normals, int[] indices, int facetType, ArrayDataType arrayDataType) {
        this.name = name;
        this.vertices = vertices;
        this.texcoords = texcoords;
        this.normals = normals;
        this.indices = indices;
        this.facetType.setVal(facetType);
        this.arrayDataType = arrayDataType;
        if (arrayDataType == ArrayDataType.VERTEX_TEXCOORD_NORMAL){
            calculateTangent();
        }
        checkArrayData();
        smoothNormal();
        setUpMesh();
    }

    public Mesh(String name, float[] vertices, int[] indices){
        this.name = name;
        this.vertices = vertices;
        this.indices = indices;
        this.facetType.setVal(TYPE_LINES);
        this.arrayDataType = ArrayDataType.VERTEX;
        checkArrayData();
        modifyIndicesForWireFrame();
        setUpMesh();
    }

    public void setUpMesh() {
        // 对象生成
        int[] VAO = new int[1];
        int[] VBO = new int[selectArrayDataNum()];
        int[] EBO = new int[1];
        GLES30.glGenVertexArrays(VAO.length, VAO, 0);
        GLES30.glGenBuffers(VBO.length, VBO, 0);
        GLES30.glGenBuffers(EBO.length, EBO, 0);

        // 分配缓冲
        FloatBuffer vertexBuffer = BufferHelper.createFloatBuffer(vertices);
        FloatBuffer texcoordBuffer = isNoUV? null: BufferHelper.createFloatBuffer(texcoords);
        FloatBuffer normalBuffer = isNoNormal? null: BufferHelper.createFloatBuffer(normals);
        FloatBuffer tangentBuffer = isNoUV || isNoNormal? null: BufferHelper.createFloatBuffer(tangents);
        FloatBuffer biTangentBuffer = isNoUV || isNoNormal? null: BufferHelper.createFloatBuffer(biTangents);
        IntBuffer intBuffer = BufferHelper.createIntBuffer(indices);

        // 顶点坐标
        GLES30.glBindVertexArray(VAO[0]);
        GLES30.glBindBuffer(GLES20.GL_ARRAY_BUFFER, VBO[0]);
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER,vertexBuffer.capacity() * BYTES_PER_FLOAT,
                vertexBuffer, GLES30.GL_STATIC_DRAW);
        GLES30.glEnableVertexAttribArray(0);
        GLES30.glVertexAttribPointer(0, POSITION_SIZE,
                GLES30.GL_FLOAT,false, POSITION_STRIDE,0);

        // 纹理坐标
        if (!isNoUV) {
            GLES30.glBindBuffer(GLES20.GL_ARRAY_BUFFER, VBO[1]);
            GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, texcoordBuffer.capacity() * BYTES_PER_FLOAT,
                    texcoordBuffer, GLES30.GL_STATIC_DRAW);
            GLES30.glEnableVertexAttribArray(1);
            GLES30.glVertexAttribPointer(1, TEXCOORD_SIZE,
                    GLES30.GL_FLOAT, false, TEXCOORD_STRIDE, 0);
        }

        // 法线
        if (!isNoNormal){
            GLES30.glBindBuffer(GLES20.GL_ARRAY_BUFFER, VBO[2]);
            GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER,normalBuffer.capacity() * BYTES_PER_FLOAT,
                    normalBuffer, GLES30.GL_STATIC_DRAW);
            GLES30.glEnableVertexAttribArray(2);
            GLES30.glVertexAttribPointer(2, NORMAL_SIZE,
                    GLES30.GL_FLOAT,false, NORMAL_STRIDE,0);
        }

        // 切线副切线
        if (!isNoUV && !isNoNormal){
            GLES30.glBindBuffer(GLES20.GL_ARRAY_BUFFER, VBO[3]);
            GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER,tangentBuffer.capacity() * BYTES_PER_FLOAT,
                    tangentBuffer, GLES30.GL_STATIC_DRAW);
            GLES30.glEnableVertexAttribArray(3);
            GLES30.glVertexAttribPointer(3, TANGENT_SIZE,
                    GLES30.GL_FLOAT,false, TANGENT_STRIDE,0);
            GLES30.glBindBuffer(GLES20.GL_ARRAY_BUFFER, VBO[4]);
            GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER,biTangentBuffer.capacity() * BYTES_PER_FLOAT,
                    biTangentBuffer, GLES30.GL_STATIC_DRAW);
            GLES30.glEnableVertexAttribArray(4);
            GLES30.glVertexAttribPointer(4, BI_TANGENT_SIZE,
                    GLES30.GL_FLOAT,false, BI_TANGENT_STRIDE,0);
        }

        // 索引
        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, EBO[0]);
        GLES30.glBufferData(GLES30.GL_ELEMENT_ARRAY_BUFFER, intBuffer.capacity() * BYTES_PER_INT,
                intBuffer, GLES30.GL_STATIC_DRAW);

        vao = VAO[0];
        GLES30.glBindVertexArray(0);
    }

    public void draw(Matrix4 modelMatrix, Matrix4 viewMatrix, Matrix4 projectionMatrix, Vector3 viewPosition){
        if (isVisible.getVal()){
            if (null == currentMaterial){
                throw new NullPointerException("未设置Material");
            }
            currentMaterial.setShaderModelMatrix(modelMatrix);
            currentMaterial.setShaderViewMatrix(viewMatrix);
            currentMaterial.setShaderProjectionMatrix(projectionMatrix);
            currentMaterial.setViewPosition(viewPosition);
            currentMaterial.useMaterial();

            GLES30.glBindVertexArray(vao);
            GLES30.glDrawElements(facetType.getVal(), indices.length, GLES30.GL_UNSIGNED_INT, 0);
            GLES30.glBindVertexArray(0);
        }
    }

    public void draw(BaseCamera camera, Matrix4 modelMatrix){
        if (isVisible.getVal()){
            if (null == currentMaterial){
                throw new NullPointerException("未设置Material");
            }
            currentMaterial.setShaderModelMatrix(modelMatrix);
            currentMaterial.setShaderViewMatrix(camera.getViewMatrix());
            currentMaterial.setShaderProjectionMatrix(camera.getProjectionMatrix());
            currentMaterial.useMaterial();

            GLES30.glBindVertexArray(vao);
            GLES30.glDrawElements(facetType.getVal(), indices.length, GLES30.GL_UNSIGNED_INT, 0);
            GLES30.glBindVertexArray(0);
        }
    }

    public void draw(){
        if (isVisible.getVal()){
            if (null == currentMaterial){
                throw new NullPointerException("未设置Material");
            }
            currentMaterial.useMaterial();

            GLES30.glBindVertexArray(vao);
            GLES30.glDrawElements(facetType.getVal(), indices.length, GLES30.GL_UNSIGNED_INT, 0);
            GLES30.glBindVertexArray(0);
        }
    }

    public float[] getCenterPosition(){
        if (!isCalCenter){
            for (int i = 0; i < meshes.size(); i++){
                maxX = Math.max(meshes.get(i).getMaxX(), maxX);
                maxY = Math.max(meshes.get(i).getMaxY(), maxY);
                maxZ = Math.max(meshes.get(i).getMaxZ(), maxZ);
                minX = Math.min(meshes.get(i).getMinX(), minX);
                minY = Math.min(meshes.get(i).getMinY(), minY);
                minZ = Math.min(meshes.get(i).getMinZ(), minZ);
            }
            centerPosition = new float[]{
                    (maxX + minX) / 2.0f,
                    (maxY + minY) / 2.0f,
                    (maxZ + minZ) / 2.0f
            };
            isCalCenter = true;
        }
        return centerPosition;
    }

    public float getLongestSide(){
        if (!isCalLong){
            getCenterPosition();
            this.longestSide = maxX - minX;
            this.longestSide = Math.max(longestSide, maxY - minY);
            this.longestSide = Math.max(longestSide, maxZ - minZ);
        }
        return longestSide;
    }

    public void calculateTangent(){
        this.tangents = new float[normals.length];
        this.biTangents = new float[normals.length];
        for (int i = 0; i < indices.length; i += 3){
            Geometry.Vector pos1 = new Geometry.Vector(
                    vertices[indices[i] * 3], vertices[indices[i] * 3 + 1], vertices[indices[i] * 3 + 2]
            );
            Geometry.Vector pos2 = new Geometry.Vector(
                    vertices[indices[i + 1] * 3], vertices[indices[i + 1] * 3 + 1], vertices[indices[i + 1] * 3 + 2]
            );
            Geometry.Vector pos3 = new Geometry.Vector(
                    vertices[indices[i + 2] * 3], vertices[indices[i + 2] * 3 + 1], vertices[indices[i + 2] * 3 + 2]
            );
            Geometry.Vector2D uv1 = new Geometry.Vector2D(
                    texcoords[indices[i] * 2], texcoords[indices[i] * 2 + 1]
            );
            Geometry.Vector2D uv2 = new Geometry.Vector2D(
                    texcoords[indices[i + 1] * 2], texcoords[indices[i + 1] * 2 + 1]
            );
            Geometry.Vector2D uv3 = new Geometry.Vector2D(
                    texcoords[indices[i + 2] * 2], texcoords[indices[i + 2] * 2 + 1]
            );
            Geometry.Vector edge1 = pos2.minus(pos1);
            Geometry.Vector edge2 = pos3.minus(pos1);
            Geometry.Vector2D deltaUV1 = uv2.minus(uv1);
            Geometry.Vector2D deltaUV2 = uv3.minus(uv1);
            float f = 1.0f / (deltaUV1.x * deltaUV2.y - deltaUV2.x * deltaUV1.y);
            Geometry.Vector tangent = new Geometry.Vector(
                    f * (deltaUV2.y * edge1.x - deltaUV1.y * edge2.x),
                    f * (deltaUV2.y * edge1.y - deltaUV1.y * edge2.y),
                    f * (deltaUV2.y * edge1.z - deltaUV1.y * edge2.z)
            );
            tangent = tangent.normalize();
            Geometry.Vector biTangent = new Geometry.Vector(
                    f * (-deltaUV2.x * edge1.x + deltaUV1.x * edge2.x),
                    f * (-deltaUV2.x * edge1.y + deltaUV1.x * edge2.y),
                    f * (-deltaUV2.x * edge1.z + deltaUV1.x * edge2.z)
            );
            biTangent = biTangent.normalize();
            tangents[indices[i] * 3] = tangent.x;
            tangents[indices[i] * 3 + 1] = tangent.y;
            tangents[indices[i] * 3 + 2] = tangent.z;
            tangents[indices[i + 1] * 3] = tangent.x;
            tangents[indices[i + 1] * 3 + 1] = tangent.y;
            tangents[indices[i + 1] * 3 + 2] = tangent.z;
            tangents[indices[i + 2] * 3] = tangent.x;
            tangents[indices[i + 2] * 3 + 1] = tangent.y;
            tangents[indices[i + 2] * 3 + 2] = tangent.z;
            biTangents[indices[i] * 3] = biTangent.x;
            biTangents[indices[i] * 3 + 1] = biTangent.y;
            biTangents[indices[i] * 3 + 2] = biTangent.z;
            biTangents[indices[i + 1] * 3] = biTangent.x;
            biTangents[indices[i + 1] * 3 + 1] = biTangent.y;
            biTangents[indices[i + 1] * 3 + 2] = biTangent.z;
            biTangents[indices[i + 2] * 3] = biTangent.x;
            biTangents[indices[i + 2] * 3 + 1] = biTangent.y;
            biTangents[indices[i + 2] * 3 + 2] = biTangent.z;
        }
    }

    public void smoothNormal(){
        boolean debug = true;
        if (!debug) {
            float[] tempNormals = normals.clone();
            HashMap<String, Geometry.Vector> tempNormalHashMap = new HashMap<>();
            for (int index : indices) {
                float tempVertexX = vertices[index * 3];
                float tempVertexY = vertices[index * 3 + 1];
                float tempVertexZ = vertices[index * 3 + 2];
                float tempNormalX = normals[index * 3];
                float tempNormalY = normals[index * 3 + 1];
                float tempNormalZ = normals[index * 3 + 2];
                tempNormalHashMap.clear();
                tempNormalHashMap.put(tempNormalX + "/" + tempNormalY + "/" + tempNormalZ, new Geometry.Vector(tempNormalX, tempNormalY, tempNormalZ));
                for (int j : indices) {
                    float tempX = vertices[j * 3];
                    float tempY = vertices[j * 3 + 1];
                    float tempZ = vertices[j * 3 + 2];
                    float otherNormalX = normals[j * 3];
                    float otherNormalY = normals[j * 3 + 1];
                    float otherNormalZ = normals[j * 3 + 2];
                    if (tempVertexX == tempX && tempVertexY == tempY && tempVertexZ == tempZ) {
                        if (otherNormalX == tempNormalX && otherNormalY == tempNormalY && otherNormalZ == tempNormalZ) {
                            continue;
                        }
                        String otherKey = otherNormalX + "/" + otherNormalY + "/" + otherNormalZ;
                        if (!tempNormalHashMap.containsKey(otherKey)) {
                            tempNormalHashMap.put(otherKey, new Geometry.Vector(normals[j * 3], normals[j * 3 + 1], normals[j * 3 + 2]));
                        }
                    }
                }
                Geometry.Vector resultNormal = new Geometry.Vector(0, 0, 0);
                for (Map.Entry<String, Geometry.Vector> entry : tempNormalHashMap.entrySet()) {
                    Geometry.Vector currentNormal = entry.getValue();
                    resultNormal = resultNormal.plus(currentNormal);
                }
                resultNormal = resultNormal.normalize();
                tempNormals[index * 3] = resultNormal.x;
                tempNormals[index * 3 + 1] = resultNormal.y;
                tempNormals[index * 3 + 2] = resultNormal.z;
            }
            normals = tempNormals;
        }
    }

    protected void modifyIndicesForWireFrame(){
        int[] finalIndices = new int[indices.length * 2];
        int counter = 0;
        for (int i = 0; i < indices.length; i += 3){
            int p0 = indices[i];
            int p1 = indices[i + 1];
            int p2 = indices[i + 2];
            finalIndices[counter] = p0;
            finalIndices[counter + 1] = p1;
            finalIndices[counter + 2] = p1;
            finalIndices[counter + 3] = p2;
            finalIndices[counter + 4] = p2;
            finalIndices[counter + 5] = p0;
            counter += 6;
        }
        this.indices = finalIndices;
    }

    public void createWireFrameMesh(){
        this.wireFrameMesh = new Mesh(name+"_wireframe", vertices, indices);
    }

    protected void checkArrayData(){
        switch (arrayDataType){
            case VERTEX_TEXCOORD:
                isNoNormal = true;
                isNoUV = false;
                break;
            case VERTEX_TEXCOORD_NORMAL:
                isNoNormal = false;
                isNoUV = false;
                break;
            case VERTEX_NORMAL:
                isNoUV = true;
                isNoNormal = false;
                break;
            case VERTEX:
                isNoUV = true;
                isNoNormal = true;
                break;
        }
    }

    private int selectArrayDataNum(){
        switch (arrayDataType){
            case VERTEX:
                return 1;
            case VERTEX_TEXCOORD:
            case VERTEX_NORMAL:
                return 2;
            case VERTEX_TEXCOORD_NORMAL:
                return 5;
            default:
                return 0;
        }
    }

    public int getVerticesNum(){
        return vertices.length;
    }

    public Material getOriginalMaterial() {
        return originalMaterial;
    }

    public void setOriginalMaterial(Material originalMaterial) {
        this.originalMaterial = originalMaterial;
    }

    public Material getCurrentMaterial() {
        return currentMaterial;
    }

    public void setCurrentMaterial(Material currentMaterial) {
        this.currentMaterial = currentMaterial;
    }

    public void setVertices(float[] vertices) {
        this.vertices = vertices;
    }

    public void setTexcoords(float[] texcoords) {
        this.texcoords = texcoords;
    }

    public void setNormals(float[] normals) {
        this.normals = normals;
    }

    public void setIndices(int[] indices) {
        this.indices = indices;
    }

    public float[] getVertices() {
        return vertices;
    }

    public int[] getIndices() {
        return indices;
    }

    public String getName() {
        return name;
    }

    public float getMaxX() {
        return maxX;
    }

    public void setMaxX(float maxX) {
        this.maxX = maxX;
    }

    public float getMaxY() {
        return maxY;
    }

    public void setMaxY(float maxY) {
        this.maxY = maxY;
    }

    public float getMaxZ() {
        return maxZ;
    }

    public void setMaxZ(float maxZ) {
        this.maxZ = maxZ;
    }

    public float getMinX() {
        return minX;
    }

    public void setMinX(float minX) {
        this.minX = minX;
    }

    public float getMinY() {
        return minY;
    }

    public void setMinY(float minY) {
        this.minY = minY;
    }

    public float getMinZ() {
        return minZ;
    }

    public void setMinZ(float minZ) {
        this.minZ = minZ;
    }

    public float[] getTexcoords() {
        return texcoords;
    }

    public float[] getNormals() {
        return normals;
    }

    public List<Mesh> getMeshes() {
        return meshes;
    }

    public Mesh getMesh(int i){
        return meshes.get(i);
    }

    public int getVao() {
        return vao;
    }

    public void setWireFrameMesh(Mesh wireFrameMesh) {
        this.wireFrameMesh = wireFrameMesh;
    }

    public Mesh getWireFrameMesh() {
        return wireFrameMesh;
    }

    public Boolean getIsShowWireFrame() {
        return isShowWireFrame;
    }

    public void setShowWireframe(boolean isShow){
        this.isShowWireFrame.setVal(isShow);
    }

    public void setMeshes(List<Mesh> meshes) {
        this.meshes = meshes;
    }

    public void addMesh(Mesh mesh){
        this.meshes.add(mesh);
    }

    public Boolean getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(boolean isVisible) {
        this.isVisible.setVal(isVisible);
    }

    public Int getFacetType() {
        return facetType;
    }

    public void setFacetType(int facetType){
        this.facetType.setVal(facetType);
    }

    public Vector3 getRotation() {
        return rotation;
    }

    public void setRotation(float x, float y, float z) {
        this.rotation.x.setVal(x);
        this.rotation.y.setVal(y);
        this.rotation.z.setVal(z);
    }

    public void setRotation(float[] xyz){
        this.rotation.x.setVal(xyz[0]);
        this.rotation.y.setVal(xyz[1]);
        this.rotation.z.setVal(xyz[2]);
    }

    public Vector3 getScale() {
        return scale;
    }

    public void setScale(float x, float y, float z) {
        this.scale.x.setVal(x);
        this.scale.y.setVal(y);
        this.scale.z.setVal(z);
    }

    public ArrayDataType getArrayDataType() {
        return arrayDataType;
    }
}
