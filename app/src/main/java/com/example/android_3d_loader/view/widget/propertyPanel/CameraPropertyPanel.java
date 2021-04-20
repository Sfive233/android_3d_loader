package com.example.android_3d_loader.view.widget.propertyPanel;

import android.content.Context;

import com.example.android_3d_loader.core.camera.OrbitCamera;
import com.example.android_3d_loader.core.model.mesh.basicObject.AxisIndicator;
import com.example.android_3d_loader.view.widget.propertyPanel.WidgetPropertyPanel;
import com.example.android_3d_loader.view.property.PropertyDefinition;
import com.example.android_3d_loader.view.property.PropertyGroup;
import com.example.android_3d_loader.view.property.Range;

public class CameraPropertyPanel extends WidgetPropertyPanel {

    private static final String TAG = "CameraPropertyPanel";

    public CameraPropertyPanel(Context context) {
        super(context);

        init();
    }

    private void init(){
        AxisIndicator axisIndicator = (AxisIndicator) mGLCommunicator.getData("AxisIndicator");
        OrbitCamera mainCamera = (OrbitCamera) mGLCommunicator.getData("MainCamera");
        PropertyGroup settingGroup = new PropertyGroup("摄像机");
        settingGroup.add(new PropertyDefinition("显示坐标轴指示器", axisIndicator.getIsVisible()));
        settingGroup.add(new PropertyDefinition("视场角", new Range(10.0f, 120.0f, 1.0f), mainCamera.getFov()));
        addGroup(settingGroup);
    }
}
