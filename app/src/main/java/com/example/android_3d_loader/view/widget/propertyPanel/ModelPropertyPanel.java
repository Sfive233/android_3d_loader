package com.example.android_3d_loader.view.widget.propertyPanel;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.android_3d_loader.core.model.Model;
import com.example.android_3d_loader.view.widget.propertyPanel.WidgetPropertyPanel;
import com.example.android_3d_loader.view.property.PropertyDefinition;
import com.example.android_3d_loader.view.property.PropertyGroup;

import static com.example.android_3d_loader.view.GLESWindow.OPEN_MODEL;

public class ModelPropertyPanel extends WidgetPropertyPanel {

    private static final String TAG = "ModelPropertyPanel";

    public ModelPropertyPanel(Context context) {
        super(context);

        init();
    }

    private void init(){
        Model model = (Model) mGLCommunicator.getData("Model");
        PropertyGroup modelGroup = new PropertyGroup("模型文件");
        modelGroup.setShowTitle(true);
        modelGroup.setClosable(false);
        modelGroup.add(new PropertyDefinition("Import", model.getModelPath(), new OnClickListener() {
            @Override
            public void onClick(View v) {
                int permissionGrantedCode = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
                if (permissionGrantedCode != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(((Activity) getContext()), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                }else {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    ((Activity) getContext()).startActivityForResult(intent, OPEN_MODEL);
                }
            }
        }));
        addGroup(modelGroup);

        PropertyGroup modelInfoGroup = new PropertyGroup("详细信息");
        modelInfoGroup.setShowTitle(true);
        modelInfoGroup.setClosable(false);
        modelInfoGroup.add(new PropertyDefinition("格式", model.getModelPath().substring(model.getModelPath().lastIndexOf("."))));
        modelInfoGroup.add(new PropertyDefinition("顶点数", model.getTotalVerticesNum()+""));
        modelInfoGroup.add(new PropertyDefinition("网格数", model.getMeshes().size()+""));
        modelInfoGroup.add(new PropertyDefinition("是否有纹理坐标", model.isHaveUV()? "有": "无"));
        modelInfoGroup.add(new PropertyDefinition("是否有法线", model.isHaveNormal()? "有": "无"));
        addGroup(modelInfoGroup);
    }
}
