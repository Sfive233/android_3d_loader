package com.example.android_3d_loader.view.widget.propertyPanel;

import android.content.Context;

import com.example.android_3d_loader.core.dataType.Boolean;
import com.example.android_3d_loader.view.widget.WidgetDescription;
import com.example.android_3d_loader.view.widget.WidgetFPSCounter;
import com.example.android_3d_loader.view.widget.WidgetPropertyGroup;
import com.example.android_3d_loader.view.widget.propertyPanel.WidgetPropertyPanel;
import com.example.android_3d_loader.view.widget.WidgetSwitch;
import com.example.android_3d_loader.view.property.PropertyDefinition;
import com.example.android_3d_loader.view.property.PropertyGroup;

public class SystemConfigPropertyPanel extends WidgetPropertyPanel {

    private static final String TAG = "SystemConfigPropertyPanel";

    public SystemConfigPropertyPanel(Context context) {
        super(context);

        init();
    }

    private void init(){
        final WidgetFPSCounter fpsCounter = (WidgetFPSCounter) mGLCommunicator.getTemp("FPSCounter");
        WidgetPropertyGroup globalSettingGroupView = new WidgetPropertyGroup(getContext(), new PropertyGroup("全局设定"));
        globalSettingGroupView.setClosable(false);
        globalSettingGroupView.setShowTitle(true);
        globalSettingGroupView.addSubView(
                new WidgetSwitch(
                        getContext(),
                        new PropertyDefinition(
                                "显示帧数显示器",
                                new Boolean(true),
                                new WidgetSwitch.OnSwitchChangeListener() {
                                    @Override
                                    public void onSwitchChanged(boolean val) {
                                        fpsCounter.changeVisibility(val);
                                    }
                                }
                        )
                )
        );
        addGroupView(globalSettingGroupView);

        WidgetPropertyGroup sysInfoGroupView = new WidgetPropertyGroup(getContext(), new PropertyGroup("系统信息"));
        sysInfoGroupView.setClosable(false);
        sysInfoGroupView.setShowTitle(true);
        WidgetDescription resView = new WidgetDescription(
                getContext(),
                new PropertyDefinition(
                        "屏幕分辨率",
                        mGLCommunicator.getGLSurfaceViewWidth() + "x" + mGLCommunicator.getGLSurfaceViewHeight()
                )
        );
        WidgetDescription gpuView = new WidgetDescription(
                getContext(),
                new PropertyDefinition(
                        "GPU型号",
                        mGLCommunicator.getRenderHardwareName()
                )
        );
        WidgetDescription vendorView = new WidgetDescription(
                getContext(),
                new PropertyDefinition(
                        "GPU供应商",
                        mGLCommunicator.getRenderHardwareVendor()
                )
        );
        WidgetDescription glslView = new WidgetDescription(
                getContext(),
                new PropertyDefinition(
                        "OpenGL版本",
                        mGLCommunicator.getGLSLVersion()
                )
        );
        sysInfoGroupView.addSubView(resView);
        sysInfoGroupView.addSubView(gpuView);
        sysInfoGroupView.addSubView(vendorView);
        sysInfoGroupView.addSubView(glslView);
        addGroupView(sysInfoGroupView);
    }
}
