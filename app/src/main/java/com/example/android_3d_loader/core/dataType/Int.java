package com.example.android_3d_loader.core.dataType;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class Int {
    @Expose
    private int intVal;

    public Int(int intVal){
        this.intVal = intVal;
    }

    public int getVal(){
        return intVal;
    }

    public void setVal(int intVal) {
        this.intVal = intVal;
    }
}
