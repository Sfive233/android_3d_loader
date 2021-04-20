package com.example.android_3d_loader.core.dataType;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class Boolean{
    @Expose
    private boolean booleanVal;

    public Boolean(){
        this.booleanVal = false;
    }

    public Boolean(boolean booleanVal) {
        this.booleanVal = booleanVal;
    }

    public boolean getVal(){
        return booleanVal;
    }

    public void setVal(boolean booleanVal){
        this.booleanVal = booleanVal;
    }

    public void swapData(Object newDataObject){
        if (newDataObject instanceof Boolean){
            Boolean newBoolean = (Boolean) newDataObject;
            this.booleanVal = newBoolean.getVal();
        }else {
            throw new IllegalArgumentException("Object \"" + newDataObject.getClass().getName() + "\" is not instance of " + this.getClass().getName());
        }
    }
}
