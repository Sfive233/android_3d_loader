package com.example.android_3d_loader.core.model.obj;

import com.example.android_3d_loader.core.texture.Texture;
import com.example.android_3d_loader.core.texture.texture2D.Texture2D;

public class MTL {
    private String newmtl;
    private float Ns;
    private float[] Ka = new float[]{0, 0, 0};// 环境光
    private float[] Kd = new float[]{1, 1, 1};// 漫反射
    private float[] Ks = new float[]{1, 1, 1};// 高光反射
    private float[] Ke = new float[3];//
    private float Ni;// 折射率
    private float d;// 不透明度
    private float Tr;// 透明度
    private float Tf;
    private int illum;// 1为无高光反射，2为有
    private Texture2D map_Ka;
    private Texture2D map_Kd;
    private Texture2D map_Ks;
    private Texture2D map_Bump;
    private Texture2D map_Tr;
    private Texture2D map_d;
    private Texture2D disp;// 置换贴图

    public MTL() {
    }

    public MTL(String newmtl) {
        this.newmtl = newmtl;

        this.map_Ka = Texture2D.getNullTexture2D();
        this.map_Kd = Texture2D.getNullTexture2D();
        this.map_Ks = Texture2D.getNullTexture2D();
        this.map_Bump = Texture2D.getNullTexture2D();
        this.map_Tr = Texture2D.getNullTexture2D();
        this.map_d = Texture2D.getNullTexture2D();
        this.disp = Texture2D.getNullTexture2D();
    }

    public Texture getMap_Ka() {
        return map_Ka;
    }

    public void setMap_Ka(Texture2D map_Ka) {
        this.map_Ka = map_Ka;
    }

    public String getNewmtl() {
        return newmtl;
    }

    public void setNewmtl(String newmtl) {
        this.newmtl = newmtl;
    }

    public float getNs() {
        return Ns;
    }

    public void setNs(float ns) {
        Ns = ns;
    }

    public float[] getKa() {
        return Ka;
    }

    public void setKa(float[] ka) {
        Ka = ka;
    }

    public float[] getKd() {
        return Kd;
    }

    public void setKd(float[] kd) {
        Kd = kd;
    }

    public float[] getKs() {
        return Ks;
    }

    public void setKs(float[] ks) {
        Ks = ks;
    }

    public float[] getKe() {
        return Ke;
    }

    public void setKe(float[] ke) {
        Ke = ke;
    }

    public float getNi() {
        return Ni;
    }

    public void setNi(float ni) {
        Ni = ni;
    }

    public float getD() {
        return d;
    }

    public void setD(float d) {
        this.d = d;
    }

    public float getTr() {
        return Tr;
    }

    public void setTr(float tr) {
        Tr = tr;
    }

    public float getTf() {
        return Tf;
    }

    public void setTf(float tf) {
        Tf = tf;
    }

    public int getIllum() {
        return illum;
    }

    public void setIllum(int illum) {
        this.illum = illum;
    }

    public Texture2D getMap_Kd() {
        return map_Kd;
    }

    public void setMap_Kd(Texture2D map_Kd) {
        this.map_Kd = map_Kd;
    }

    public Texture2D getMap_Ks() {
        return map_Ks;
    }

    public void setMap_Ks(Texture2D map_Ks) {
        this.map_Ks = map_Ks;
    }

    public Texture2D getMap_Tr() {
        return map_Tr;
    }

    public void setMap_Tr(Texture2D map_Tr) {
        this.map_Tr = map_Tr;
    }

    public Texture2D getMap_d() {
        return map_d;
    }

    public void setMap_d(Texture2D map_d) {
        this.map_d = map_d;
    }

    public Texture2D getDisp() {
        return disp;
    }

    public void setDisp(Texture2D disp) {
        this.disp = disp;
    }

    public Texture2D getMap_Bump() {
        return map_Bump;
    }

    public void setMap_Bump(Texture2D map_Bump) {
        this.map_Bump = map_Bump;
    }
}
