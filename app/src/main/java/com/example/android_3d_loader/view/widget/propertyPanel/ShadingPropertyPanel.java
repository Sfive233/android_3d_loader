package com.example.android_3d_loader.view.widget.propertyPanel;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;

import com.example.android_3d_loader.controller.GLRequest;
import com.example.android_3d_loader.core.GLRenderer;
import com.example.android_3d_loader.core.dataType.Boolean;
import com.example.android_3d_loader.core.light.DirectionLight;
import com.example.android_3d_loader.core.material.toneMapping.ToneMappingMaterial;
import com.example.android_3d_loader.view.util.ListUtil;
import com.example.android_3d_loader.view.widget.WidgetDivider;
import com.example.android_3d_loader.view.widget.dragBar.WidgetDragBar;
import com.example.android_3d_loader.view.widget.propertyPanel.WidgetPropertyPanel;
import com.example.android_3d_loader.view.widget.dropDownList.WidgetDropDownList;
import com.example.android_3d_loader.view.widget.WidgetPropertyGroup;
import com.example.android_3d_loader.view.widget.WidgetSwitch;
import com.example.android_3d_loader.view.property.PropertyDefinition;
import com.example.android_3d_loader.view.property.PropertyGroup;
import com.example.android_3d_loader.view.property.Range;

import java.util.List;

public class ShadingPropertyPanel extends WidgetPropertyPanel {

    private static final String TAG = "ShadingPropertyPanel";

    public ShadingPropertyPanel(Context context) {
        super(context);

        init();
    }

    private void init(){
        if (mGLCommunicator.getCurrentGLRequest().equals(GLRequest.RUNNING)){
//        if(mGLCommunicator.isFree()){
            DirectionLight directionLight = (DirectionLight) mGLCommunicator.getData("DirectionLight");
            PropertyGroup lightingGroup = new PropertyGroup("定向光");
            lightingGroup.add(new PropertyDefinition("倾斜角", new Range(0.0f, 180.0f, 1.0f), directionLight.getPitch()));
            lightingGroup.add(new PropertyDefinition("偏航角", new Range(-180.0f, 180.0f, 1.0f), directionLight.getYaw()));
            lightingGroup.add(new PropertyDefinition("环境光颜色", new Range(0.0f, 3.0f, 0.05f), directionLight.getAmbientLightColor()));
            lightingGroup.add(new PropertyDefinition("漫反射颜色", new Range(0.0f, 3.0f, 0.05f), directionLight.getDiffuseLightColor()));
            lightingGroup.add(new PropertyDefinition("高光反射颜色", new Range(0.0f, 3.0f, 0.05f), directionLight.getSpecularLightColor()));
            addGroup(lightingGroup);

            Boolean savedEnableShadow = (Boolean) mGLCommunicator.getData("EnableShadow");
            final PropertyGroup shadowGroup = new PropertyGroup("阴影");
            shadowGroup.add(new PropertyDefinition("开启阴影", savedEnableShadow));
            addGroup(shadowGroup);



            postProcessGroupView = new WidgetPropertyGroup(getContext(), new PropertyGroup("后期处理特效"));

            Boolean savedEnableBloom = (Boolean) mGLCommunicator.getData("EnableBloom");
            WidgetSwitch enableBloomView = new WidgetSwitch(getContext(), new PropertyDefinition("开启泛光", savedEnableBloom));
            postProcessGroupView.addSubView(enableBloomView);

            postProcessGroupView.addSubView(new WidgetDivider(getContext()));

            Boolean savedEnableToneMapping = (Boolean) mGLCommunicator.getData("EnableToneMapping");
            WidgetSwitch enableToneMappingView = new WidgetSwitch(getContext(), new PropertyDefinition("开启色调映射", savedEnableToneMapping));
            postProcessGroupView.addSubView(enableToneMappingView);

            List<String> toneMappingList = (List<String>) mGLCommunicator.getData("ToneMappingList");
            String savedCurrentSelectedToneMapping = (String) mGLCommunicator.getData("CurrentSelectedToneMapping");
            WidgetDropDownList toneMappingListView = new WidgetDropDownList(getContext(), new PropertyDefinition("色调映射方式", toneMappingList, new WidgetDropDownList.OnItemSelectedListener() {
                @Override
                public void OnItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedItem = (String) parent.getItemAtPosition(position);
                    mGLCommunicator.putData("CurrentSelectedToneMapping", selectedItem);
                    mGLCommunicator.setCurrentGLStatus(GLRequest.SWITCH_TONE_MAPPING);
                    GLRenderer.isReLoad = true;
                    postProcessGroupView.removeSubView(exposureView);
                    refreshExposure();
                }
            }, ListUtil.getIndex(toneMappingList, savedCurrentSelectedToneMapping)));
            postProcessGroupView.addSubView(toneMappingListView);

            ToneMappingMaterial savedToneMappingMaterial = (ToneMappingMaterial) mGLCommunicator.getData("ToneMappingMaterial");
            exposureView = new WidgetDragBar(getContext(), new PropertyDefinition("曝光度", new Range(0.0f, 5.0f, 0.1f), savedToneMappingMaterial.getExposure()));
            postProcessGroupView.addSubView(exposureView);

            addGroupView(postProcessGroupView);

        }else {
            init();
        }
    }

    private WidgetPropertyGroup postProcessGroupView;
    private WidgetDragBar exposureView;
    protected void refreshExposure(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mGLCommunicator.getCurrentGLRequest().equals(GLRequest.RUNNING)) {
//                if(mGLCommunicator.isFree()){
                    ToneMappingMaterial savedToneMappingMaterial = (ToneMappingMaterial) mGLCommunicator.getData("ToneMappingMaterial");
                    exposureView = new WidgetDragBar(getContext(), new PropertyDefinition("曝光度", new Range(0.0f, 5.0f, 0.1f), savedToneMappingMaterial.getExposure()));
                    postProcessGroupView.addSubView(exposureView);
                }else {
                    refreshExposure();
                }
            }
        }, 10);
    }
}
