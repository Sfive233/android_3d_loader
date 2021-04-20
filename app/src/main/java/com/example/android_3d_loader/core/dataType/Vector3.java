package com.example.android_3d_loader.core.dataType;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class Vector3 {
    @Expose
    public Float x = new Float(0.0f);
    @Expose
    public Float y = new Float(0.0f);
    @Expose
    public Float z = new Float(0.0f);

    public Vector3(){
        this.x.setVal(0f);
        this.y.setVal(0f);
        this.z.setVal(0f);
    }

    public Vector3(float x, float y, float z) {
        this.x.setVal(x);
        this.y.setVal(y);
        this.z.setVal(z);
    }

    public Vector3(float val){
        this.x.setVal(val);
        this.y.setVal(val);
        this.z.setVal(val);
    }

    public Vector3(float[] xyz){
        this.x.setVal(xyz[0]);
        this.y.setVal(xyz[1]);
        this.z.setVal(xyz[2]);
    }

    public float[] getOriginalFloats(){
        return new float[]{
                this.x.getVal(),
                this.y.getVal(),
                this.z.getVal()
        };
    }

    public void setXYZ(float[] val){
        this.x.setVal(val[0]);
        this.y.setVal(val[1]);
        this.z.setVal(val[2]);
    }

    public Vector3 getInverse(){
        return new Vector3(-x.getVal(), -y.getVal(), -z.getVal());
    }

    public Vector3 plus(Vector3 by){
        return new Vector3(
                this.x.getVal() + by.x.getVal(),
                this.y.getVal() + by.y.getVal(),
                this.z.getVal() + by.z.getVal()
        );
    }

    public Vector3 minus(Vector3 by){
        return new Vector3(
                this.x.getVal() - by.x.getVal(),
                this.y.getVal() - by.y.getVal(),
                this.z.getVal() - by.z.getVal()
        );
    }

    public Vector3 multiply(float by){
        return new Vector3(
                this.x.getVal() * by,
                this.y.getVal() * by,
                this.z.getVal() * by
        );
    }

    @Override
    public String toString() {
        return "Vector{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    public void swapData(Object newDataObject){
        if (newDataObject instanceof Vector3) {
            Vector3 newVector3 = (Vector3) newDataObject;
            x.setVal(newVector3.x.getVal());
            y.setVal(newVector3.y.getVal());
            z.setVal(newVector3.z.getVal());
        }else {
            throw new IllegalArgumentException("Object \"" + newDataObject.getClass().getName() + "\" is not instance of " + this.getClass().getName());
        }
    }

    /**
     * 由两点求出空间向量(末点减起点)
     * @param from 起点
     * @param to 末点
     * @return 空间向量
     */
    public static Vector3 vectorBetween(Vector3 from, Vector3 to){
        return new Vector3(
                to.x.getVal() - from.x.getVal(),
                to.y.getVal() - from.y.getVal(),
                to.z.getVal() - from.z.getVal()
        );
    }

    public static float distanceBetween(Vector3 from, Vector3 to){
        return vectorBetween(from, to).length();
    }

    /**
     * 空间向量叉乘
     * @param other 另一个向量
     * @return
     */
    public Vector3 crossProduct(Vector3 other) {
        return new Vector3(
                (this.y.getVal() * other.z.getVal()) - (this.z.getVal() * other.y.getVal()),
                (this.z.getVal() * other.x.getVal()) - (this.x.getVal() * other.z.getVal()),
                (this.x.getVal() * other.y.getVal()) - (this.y.getVal() * other.x.getVal())
        );
    }

    /**
     * 空间向量点乘
     * @param other 另一个向量
     * @return
     */
    public float dotProduct(Vector3 other) {
//            if (this.length() == 0 || other.length() == 0){
//                return 0.0f;
//            }
        return (this.x.getVal() * other.x.getVal() + this.y.getVal() * other.y.getVal() +
                this.z.getVal() * other.z.getVal()) / (this.length() * other.length());
    }

    /**
     * 空间向量
     * @param f
     * @return
     */
    public Vector3 scale(float f){
        return new Vector3(
                x.getVal() * f,
                y.getVal() * f,
                z.getVal() * f
        );
    }

    /**
     * 空间向量的长度（勾股定理）
     * @return
     */
    public float length(){
        return (float) Math.sqrt(x.getVal() * x.getVal() + y.getVal() * y.getVal() + z.getVal() * z.getVal());
    }

    /**
     * 单位化向量（待验证）
     * @return Vector
     */
    public Vector3 normalize(){
        float length = this.length();
        return new Vector3(
                x.getVal() / length,
                y.getVal() / length,
                z.getVal() / length
        );
    }
}
