package com.example.android_3d_loader.view.property;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class PropertyGroup {
    @Expose
    private String name;
    @Expose
    private List<PropertyDefinition> properties;
    @Expose
    private boolean isClosable = true;
    @Expose
    private boolean isClosed = false;
    @Expose
    private boolean isShowTitle = true;

    public PropertyGroup(){

    }

    public PropertyGroup(String name){
        this.name = name;
        this.properties = new ArrayList<>();
    }

    public void add(PropertyDefinition propertyDefinition){
        properties.add(propertyDefinition);
    }

    public List<PropertyDefinition> getProperties() {
        return properties;
    }

    public PropertyDefinition getPropertyDefinition(String name){
        for (PropertyDefinition propertyDefinition: properties){
            if (propertyDefinition.getName().equals(name)){
                return propertyDefinition;
            }
        }
        throw new IllegalArgumentException("Key \""+name+"\" is not exists");
    }

    public String getName() {
        return name;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setShowTitle(boolean showTitle) {
        isShowTitle = showTitle;
    }

    public void setClosed(boolean groupClosed) {
        isClosed = groupClosed;
    }

    public boolean isShowTitle() {
        return isShowTitle;
    }

    public boolean isClosable() {
        return isClosable;
    }

    public void setClosable(boolean closable) {
        isClosable = closable;
    }
}
