package com.example.android_3d_loader.view.property;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class Range implements Serializable {
    @Expose
    private float minFloatVal;
    @Expose
    private float maxFloatVal;
    @Expose
    private float floatStep;
    @Expose
    private int minIntVal;
    @Expose
    private int maxIntVal;
    @Expose
    private int intStep;

    public Range() {
    }

    public Range(float minFloatVal, float maxFloatVal, float floatStep) {
//        if (minFloatVal > maxFloatVal){
//            throw new RuntimeException("Min value can't greater than max value");
//        }
        this.minFloatVal = minFloatVal;
        this.maxFloatVal = maxFloatVal;
        this.floatStep = floatStep;
    }

    public Range(int minIntVal, int maxIntVal, int intStep) {
//        if (minIntVal > maxIntVal){
//            throw new RuntimeException("Min value can't greater than max value");
//        }
        this.minIntVal = minIntVal;
        this.maxIntVal = maxIntVal;
        this.intStep = intStep;
    }

    public float getMinFloatVal() {
        return minFloatVal;
    }

    public float getMaxFloatVal() {
        return maxFloatVal;
    }

    public float getFloatStep() {
        return floatStep;
    }

    public int getMinIntVal() {
        return minIntVal;
    }

    public int getMaxIntVal() {
        return maxIntVal;
    }

    public int getIntStep() {
        return intStep;
    }

    public void setMinFloatVal(float minFloatVal) {
        this.minFloatVal = minFloatVal;
    }

    public void setMaxFloatVal(float maxFloatVal) {
        this.maxFloatVal = maxFloatVal;
    }

    public void setMinIntVal(int minIntVal) {
        this.minIntVal = minIntVal;
    }

    public void setMaxIntVal(int maxIntVal) {
        this.maxIntVal = maxIntVal;
    }
}
