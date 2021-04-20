package com.example.android_3d_loader.view.widget.propertyPanel;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.android_3d_loader.core.model.mesh.basicObject.Plane;
import com.example.android_3d_loader.core.cubeMap.Skybox;
import com.example.android_3d_loader.view.widget.propertyPanel.WidgetPropertyPanel;
import com.example.android_3d_loader.view.property.PropertyDefinition;
import com.example.android_3d_loader.view.property.PropertyGroup;

import static com.example.android_3d_loader.view.GLESWindow.OPEN_HDRI;

public class EnvironmentPropertyPanel extends WidgetPropertyPanel {

    private static final String TAG = "EnvironmentPropertyPanel";

    public EnvironmentPropertyPanel(Context context) {
        super(context);

        init();
    }

    private void init(){
        Skybox skybox = (Skybox) mGLCommunicator.getData("Skybox");
        String skyboxPath = (String) mGLCommunicator.getData("SkyboxPath");
        PropertyGroup skyboxGroup = new PropertyGroup("天空盒");
        skyboxGroup.add(new PropertyDefinition("导入", skyboxPath, new OnClickListener() {
            @Override
            public void onClick(View v) {
                int permissionGrantedCode = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
                if (permissionGrantedCode != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(((Activity) getContext()), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                }else {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    ((Activity) getContext()).startActivityForResult(intent, OPEN_HDRI);
                }
            }
        }));
        skyboxGroup.add(new PropertyDefinition("显示天空盒", skybox.getIsVisible()));
        addGroup(skyboxGroup);

        Plane plane = (Plane) mGLCommunicator.getData("Plane");
        PropertyGroup planeGroup = new PropertyGroup("落脚平面");
        planeGroup.add(new PropertyDefinition("显示落脚平面", plane.getIsVisible()));
        addGroup(planeGroup);
    }
}
