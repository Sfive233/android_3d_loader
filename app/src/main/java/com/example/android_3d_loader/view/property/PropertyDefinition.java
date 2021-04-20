package com.example.android_3d_loader.view.property;

import android.view.View;
import android.widget.AdapterView;

import com.example.android_3d_loader.core.dataType.Boolean;
import com.example.android_3d_loader.core.dataType.DataType;
import com.example.android_3d_loader.core.dataType.Float;
import com.example.android_3d_loader.core.dataType.Int;
import com.example.android_3d_loader.core.texture.texture2D.Texture2D;
import com.example.android_3d_loader.core.dataType.Vector3;
import com.example.android_3d_loader.view.widget.WidgetSwitch;
import com.example.android_3d_loader.view.widget.dropDownList.WidgetDropDownList;
import com.google.gson.annotations.Expose;

import java.util.List;

public class PropertyDefinition {
    @Expose
    protected String name;
    @Expose
    protected PropertyType propertyType;
    @Expose
    protected DataType dataType;
    @Expose
    protected Float bindFloatVal;
    @Expose
    protected Int bindIntVal;
    @Expose
    protected Vector3 bindVector3;
    @Expose
    protected Boolean bindBooleanVal;
    @Expose
    protected Texture2D bindTexture;
    @Expose
    protected Range range;
    @Expose
    protected String hint;
    @Expose
    protected List<String> objectList;
    protected int mDefaultItemPosition;
    protected WidgetDropDownList.OnItemSelectedListener mOnItemSelectedListener;
    protected WidgetSwitch.OnSwitchChangeListener mOnSwitchChangeListener;
    @Expose
    protected String description;
    protected View.OnClickListener mOnClickListener;

    @Expose
    protected boolean isPropertyShow = true;

    public PropertyDefinition() {
    }

    public PropertyDefinition(String name, Range range, Float bindFloatVal){
        this.name = name;
        this.propertyType = PropertyType.DragBar;
        this.bindFloatVal = bindFloatVal;
        this.dataType = DataType.Float;
        this.range = range;
    }

    public PropertyDefinition(String name, Range range, Int bindIntVal){
        this.name = name;
        this.propertyType = PropertyType.DragBar;
        this.bindIntVal = bindIntVal;
        this.dataType = DataType.Int;
        this.range = range;
    }

    public PropertyDefinition(String name, Range range, Vector3 vector3){
        this.name = name;
        this.propertyType = PropertyType.DragBarVec3;
        this.bindVector3 = vector3;
        this.dataType = DataType.Float;
        this.range = range;
    }

    public PropertyDefinition(String name, Boolean bindVal){
        this.name = name;
        this.propertyType = PropertyType.Switch;
        this.bindBooleanVal = bindVal;
        this.dataType = DataType.Boolean;
    }

    public PropertyDefinition(String name, Boolean bindVal, boolean defaultVal){
        this.name = name;
        this.propertyType = PropertyType.Switch;
        this.bindBooleanVal = bindVal;
        this.bindBooleanVal.setVal(defaultVal);
        this.dataType = DataType.Boolean;
    }

    public PropertyDefinition(String name, Boolean bindBooleanVal, WidgetSwitch.OnSwitchChangeListener onSwitchChangeListener){
        this.name = name;
        this.propertyType = PropertyType.Switch;
        this.bindBooleanVal = bindBooleanVal;
        this.bindBooleanVal.setVal(bindBooleanVal.getVal());
        this.dataType = DataType.Boolean;
        this.mOnSwitchChangeListener = onSwitchChangeListener;
    }

    public PropertyDefinition(String name, Texture2D texture2D){
        this.name = name;
        this.propertyType = PropertyType.Texture;
        this.bindTexture = texture2D;
        this.dataType = DataType.String;
    }

    public PropertyDefinition(String name, String hint, View.OnClickListener onClickListener){
        this.name = name;
        this.hint = hint;
        this.mOnClickListener = onClickListener;
        this.propertyType = PropertyType.Button;
        this.dataType = DataType.String;
    }

    public PropertyDefinition(String name, List<String> dataList, WidgetDropDownList.OnItemSelectedListener onItemClickListener, int defaultItemPosition){
        this.name = name;
        this.objectList = dataList;
        this.propertyType = PropertyType.DropDownList;
        this.dataType = DataType.String;
        this.mOnItemSelectedListener = onItemClickListener;
        this.mDefaultItemPosition = defaultItemPosition;
    }

    public PropertyDefinition(String name, String description){
        this.name = name;
        this.description = description;
        this.propertyType = PropertyType.Text;
        this.dataType = DataType.String;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    public Float getBindFloatVal() {
        return bindFloatVal;
    }

    public void setBindFloatVal(float floatVal) {
        this.bindFloatVal.setVal(floatVal);
    }

    public void setBindIntVal(int bindIntVal) {
        this.bindIntVal.setVal(bindIntVal);
    }

    public void setBindBooleanVal(boolean booleanVal){
        this.bindBooleanVal.setVal(booleanVal);
    }

    public Boolean getBindBooleanVal() {
        return bindBooleanVal;
    }

    public void setRange(Range range) {
        this.range = range;
    }

    public Int getBindIntVal() {
        return bindIntVal;
    }

    public DataType getDataType() {
        return dataType;
    }

    public Range getRange() {
        return range;
    }

    public Texture2D getBindTexture() {
        return bindTexture;
    }

    public Vector3 getBindVector3() {
        return bindVector3;
    }

    public void setBindVector3X(float x) {
        this.bindVector3.x.setVal(x);
    }

    public void setBindVector3Y(float y) {
        this.bindVector3.y.setVal(y);
    }

    public void setBindVector3Z(float z) {
        this.bindVector3.z.setVal(z);
    }

    public String getHint() {
        return hint;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public View.OnClickListener getOnClickListener() {
        return mOnClickListener;
    }

    public void setOnClickListener(View.OnClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }

    public WidgetSwitch.OnSwitchChangeListener getOnSwitchChangeListener() {
        return mOnSwitchChangeListener;
    }

    public void setOnSwitchChangeListener(WidgetSwitch.OnSwitchChangeListener mOnSwitchChangeListener) {
        this.mOnSwitchChangeListener = mOnSwitchChangeListener;
    }

    public void setPropertyShow(boolean propertyShow) {
        isPropertyShow = propertyShow;
    }

    public List<String> getObjectList() {
        return objectList;
    }

    public void setObjectList(List<String> objectList) {
        this.objectList = objectList;
    }

    public int getDefaultItemPosition() {
        return mDefaultItemPosition;
    }

    public void setDefaultItemPosition(int mDefaultItemPosition) {
        this.mDefaultItemPosition = mDefaultItemPosition;
    }

    public WidgetDropDownList.OnItemSelectedListener getOnItemSelectedListener() {
        return mOnItemSelectedListener;
    }

    public void setOnItemSelectedListener(WidgetDropDownList.OnItemSelectedListener mOnItemSelectedListener) {
        this.mOnItemSelectedListener = mOnItemSelectedListener;
    }
}
