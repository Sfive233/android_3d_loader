package com.example.android_3d_loader.core.dataType;

import android.util.Log;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class Float {
    @Expose
    private float val;

    public Float(){
        this.val = 0.0f;
    }

    public Float(float val){
        this.val = val;
    }

    public float getVal() {
        return val;
    }

    public void setVal(float val) {
        this.val = val;
    }

    public void plusEqual(float newVal){
        this.val += newVal;
    }

    public void swapData(Object newDataObject){
        if (newDataObject instanceof Float){
            Float newFloat = (Float) newDataObject;
            this.val = newFloat.getVal();
        }else {
            throw new IllegalArgumentException("Object \"" + newDataObject.getClass().getName() + "\" is not instance of " + this.getClass().getName());
        }
    }
}
