package com.example.android_3d_loader.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.android_3d_loader.R;
import com.example.android_3d_loader.controller.communicator.GLCommunicator;
import com.example.android_3d_loader.view.widget.propertyPanel.CameraPropertyPanel;
import com.example.android_3d_loader.view.widget.propertyPanel.EnvironmentPropertyPanel;
import com.example.android_3d_loader.view.widget.propertyPanel.MeshPropertyPanel;
import com.example.android_3d_loader.view.widget.propertyPanel.ModelPropertyPanel;
import com.example.android_3d_loader.view.widget.propertyPanel.ShadingPropertyPanel;
import com.example.android_3d_loader.view.widget.propertyPanel.SystemConfigPropertyPanel;
import com.example.android_3d_loader.view.widget.propertyPanel.WidgetPropertyPanel;

import java.util.ArrayList;
import java.util.List;

public class WidgetRightSidePanel extends LinearLayout {
    private static final String TAG = "WidgetRightSidePanel";
    private final ScrollView mPropertyPanelRoot;
    private final ImageView mOptionMaterial;
    private final ImageView mOptionEnvironment;
    private final ImageView mOptionShading;
    private final ImageView mOptionCamera;
    private final ImageView mOptionModel;
    private final ImageView mOptionSysSetting;
    private WidgetPropertyPanel mMaterialProperty;
    private WidgetPropertyPanel mEnvironmentProperty;
    private WidgetPropertyPanel mShadingProperty;
    private WidgetPropertyPanel mCameraProperty;
    private WidgetPropertyPanel mModelProperty;
    private WidgetPropertyPanel mSysSettingProperty;
    private final GLCommunicator mGLCommunicator;
    private final List<PropertyIconBundle> mPropertyIconBundleList = new ArrayList<>();

    public WidgetRightSidePanel(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.widget_right_side_panel, this);

        mOptionMaterial = findViewById(R.id.icon_material);
        mOptionModel = findViewById(R.id.icon_model);
        mOptionEnvironment = findViewById(R.id.icon_environment);
        mOptionShading = findViewById(R.id.icon_shading);
        mOptionCamera = findViewById(R.id.icon_camera);
        mOptionSysSetting = findViewById(R.id.icon_setting);

        mPropertyPanelRoot = findViewById(R.id.property_panel_root);

        mGLCommunicator = GLCommunicator.getInstance();

        mOptionMaterial.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mOptionMaterial.setSelected(true);
                mOptionEnvironment.setSelected(false);
                mOptionShading.setSelected(false);
                mOptionCamera.setSelected(false);
                mOptionModel.setSelected(false);
                mOptionSysSetting.setSelected(false);
                showProperty(mMaterialProperty);
            }
        });
        mOptionModel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mOptionMaterial.setSelected(false);
                mOptionEnvironment.setSelected(false);
                mOptionShading.setSelected(false);
                mOptionCamera.setSelected(false);
                mOptionModel.setSelected(true);
                mOptionSysSetting.setSelected(false);
                showProperty(mModelProperty);
            }
        });
        mOptionEnvironment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mOptionMaterial.setSelected(false);
                mOptionEnvironment.setSelected(true);
                mOptionShading.setSelected(false);
                mOptionCamera.setSelected(false);
                mOptionModel.setSelected(false);
                mOptionSysSetting.setSelected(false);
                showProperty(mEnvironmentProperty);
            }
        });
        mOptionShading.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mOptionMaterial.setSelected(false);
                mOptionEnvironment.setSelected(false);
                mOptionShading.setSelected(true);
                mOptionCamera.setSelected(false);
                mOptionModel.setSelected(false);
                mOptionSysSetting.setSelected(false);
                showProperty(mShadingProperty);
            }
        });
        mOptionCamera.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mOptionMaterial.setSelected(false);
                mOptionEnvironment.setSelected(false);
                mOptionShading.setSelected(false);
                mOptionCamera.setSelected(true);
                mOptionModel.setSelected(false);
                mOptionSysSetting.setSelected(false);
                showProperty(mCameraProperty);
            }
        });
        mOptionSysSetting.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mOptionMaterial.setSelected(false);
                mOptionEnvironment.setSelected(false);
                mOptionShading.setSelected(false);
                mOptionCamera.setSelected(false);
                mOptionModel.setSelected(false);
                mOptionSysSetting.setSelected(true);
                showProperty(mSysSettingProperty);
            }
        });
    }

    public void createProperties(){

        mModelProperty = new ModelPropertyPanel(getContext());
        mMaterialProperty = new MeshPropertyPanel(getContext());
        mEnvironmentProperty = new EnvironmentPropertyPanel(getContext());
        mShadingProperty = new ShadingPropertyPanel(getContext());
        mCameraProperty = new CameraPropertyPanel(getContext());
        mSysSettingProperty = new SystemConfigPropertyPanel(getContext());

        mPropertyIconBundleList.add(new PropertyIconBundle(mOptionModel, mModelProperty));
        mPropertyIconBundleList.add(new PropertyIconBundle(mOptionMaterial, mMaterialProperty));
        mPropertyIconBundleList.add(new PropertyIconBundle(mOptionEnvironment, mEnvironmentProperty));
        mPropertyIconBundleList.add(new PropertyIconBundle(mOptionShading, mShadingProperty));
        mPropertyIconBundleList.add(new PropertyIconBundle(mOptionCamera, mCameraProperty));
        mPropertyIconBundleList.add(new PropertyIconBundle(mOptionSysSetting, mSysSettingProperty));

        if (mGLCommunicator.isSaveExist()) {
            String savedCurrentSelectedOption = (String) mGLCommunicator.getData("CurrentSelectedOption");
            if (savedCurrentSelectedOption != null) {
                for (PropertyIconBundle bundle : mPropertyIconBundleList) {
                    if (bundle.widgetPropertyPanel.getClass().getName().equals(savedCurrentSelectedOption)) {
                        showProperty(bundle.widgetPropertyPanel);
                        bundle.icon.setSelected(true);
                    }
                }
            }else {
                showProperty(mMaterialProperty);
                mOptionMaterial.setSelected(true);
            }
        }else {
            showProperty(mMaterialProperty);
            mOptionMaterial.setSelected(true);
        }
    }

    private void showProperty(WidgetPropertyPanel propertyPanel){
        if (propertyPanel != null){
            mPropertyPanelRoot.removeAllViews();
            mPropertyPanelRoot.addView(propertyPanel);
            mGLCommunicator.putData("CurrentSelectedOption", propertyPanel.getClass().getName());
        }
    }

    private static class PropertyIconBundle{
        ImageView icon;
        WidgetPropertyPanel widgetPropertyPanel;

        public PropertyIconBundle(ImageView icon, WidgetPropertyPanel widgetPropertyPanel) {
            this.icon = icon;
            this.widgetPropertyPanel = widgetPropertyPanel;
        }
    }

    public void removeAllProperties(){
        mPropertyPanelRoot.removeAllViews();
    }
}
