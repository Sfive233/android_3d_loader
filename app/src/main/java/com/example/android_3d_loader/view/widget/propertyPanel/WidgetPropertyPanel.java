package com.example.android_3d_loader.view.widget.propertyPanel;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.example.android_3d_loader.R;
import com.example.android_3d_loader.controller.communicator.GLCommunicator;
import com.example.android_3d_loader.view.property.PropertyGroup;
import com.example.android_3d_loader.view.widget.WidgetPropertyGroup;

public class WidgetPropertyPanel extends LinearLayout {

    private static final String TAG = "WidgetPropertyPanel";
    protected LinearLayout propertyPanel;
    protected GLCommunicator mGLCommunicator;


    public WidgetPropertyPanel(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.widget_property_panel, this);
        propertyPanel = findViewById(R.id.property_panel);
        mGLCommunicator = GLCommunicator.getInstance();
    }

    protected void addGroup(PropertyGroup group){

        propertyPanel.addView(new WidgetPropertyGroup(getContext(), group));
    }

    protected void addGroupView(WidgetPropertyGroup widgetPropertyGroup){
        propertyPanel.addView(widgetPropertyGroup);
    }
}
