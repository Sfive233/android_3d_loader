package com.example.android_3d_loader.view.widget.propertyPanel;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.example.android_3d_loader.controller.GLRequest;
import com.example.android_3d_loader.core.GLRenderer;
import com.example.android_3d_loader.core.dataType.Int;
import com.example.android_3d_loader.core.material.Material;
import com.example.android_3d_loader.core.material.PBRMaterial;
import com.example.android_3d_loader.core.material.TraditionalMaterial;
import com.example.android_3d_loader.core.model.mesh.Mesh;
import com.example.android_3d_loader.core.model.Model;
import com.example.android_3d_loader.view.util.ListUtil;
import com.example.android_3d_loader.view.widget.WidgetDivider;
import com.example.android_3d_loader.view.widget.dragBar.WidgetDragBar;
import com.example.android_3d_loader.view.widget.WidgetPropertyGroup;
import com.example.android_3d_loader.view.widget.WidgetSwitch;
import com.example.android_3d_loader.view.widget.WidgetTexture;
import com.example.android_3d_loader.view.widget.dragBar.WidgetDragBarVec3;
import com.example.android_3d_loader.view.widget.dropDownList.WidgetDropDownList;
import com.example.android_3d_loader.view.property.PropertyDefinition;
import com.example.android_3d_loader.view.property.PropertyGroup;
import com.example.android_3d_loader.view.property.Range;

import java.util.HashMap;
import java.util.List;

public class MeshPropertyPanel extends WidgetPropertyPanel {

    private static final String TAG = "MaterialPropertyPanel";

    private Model model;
    private WidgetPropertyGroup[] mMeshPropertyGroupViews;
    private WidgetSwitch[] mShowMeshViews;
    private WidgetSwitch[] mShowWireFrameViews;
    private WidgetSwitch[] mShowDiffuseViews;
    private WidgetTexture[] mDiffuseViews;
    private WidgetDragBarVec3[] mDiffuseDragView;
    private WidgetSwitch[] mShowSpecularViews;
    private WidgetTexture[] mSpecularViews;
    private WidgetDragBarVec3[] mSpecularDragViews;
    private WidgetSwitch[] mShowNormalViews;
    private WidgetTexture[] mNormalViews;
    // height map
    private WidgetSwitch[] mShowHeightViews;
    private WidgetSwitch[] mEnableHeightHalfRangeViews;
    private WidgetSwitch[] mEnableDiscardEdgeViews;
    private WidgetDragBar[] mHeightScaleViews;
    private WidgetTexture[] mHeightViews;

    private WidgetSwitch[] mShowAlbedoViews;
    private WidgetTexture[] mAlbedoViews;
    private WidgetDragBarVec3[] mAlbedoValViews;
    private WidgetSwitch[] mShowAOViews;
    private WidgetTexture[] mAOViews;
    private WidgetSwitch[] mShowMetallicViews;
    private WidgetTexture[] mMetallicViews;
    private WidgetDragBar[] mMetallicValViews;
    private WidgetSwitch[] mShowRoughnessViews;
    private WidgetTexture[] mRoughnessViews;
    private WidgetDragBar[] mRoughnessValViews;
    private final Int mSelectedMeshIndex = new Int(0);
    private final HashMap<Object, Integer> mDropDownListParentHashMap = new HashMap<>();

    public MeshPropertyPanel(Context context) {
        super(context);
        init();
    }

    private void init(){
        model = (Model) mGLCommunicator.getData("Model");
        int meshSize = model.getMeshes().size();
        mShowMeshViews = new WidgetSwitch[meshSize];
        mShowWireFrameViews = new WidgetSwitch[meshSize];
        mMeshPropertyGroupViews = new WidgetPropertyGroup[meshSize];
        mShowDiffuseViews = new WidgetSwitch[meshSize];
        mDiffuseViews = new WidgetTexture[meshSize];
        mDiffuseDragView = new WidgetDragBarVec3[meshSize];
        mShowSpecularViews = new WidgetSwitch[meshSize];
        mSpecularViews = new WidgetTexture[meshSize];
        mSpecularDragViews = new WidgetDragBarVec3[meshSize];
        mShowNormalViews = new WidgetSwitch[meshSize];
        mNormalViews = new WidgetTexture[meshSize];

        mShowHeightViews = new WidgetSwitch[meshSize];
        mEnableHeightHalfRangeViews = new WidgetSwitch[meshSize];
        mEnableDiscardEdgeViews = new WidgetSwitch[meshSize];
        mHeightScaleViews = new WidgetDragBar[meshSize];
        mHeightViews = new WidgetTexture[meshSize];

        mShowAlbedoViews = new WidgetSwitch[meshSize];
        mAlbedoViews = new WidgetTexture[meshSize];
        mAlbedoValViews = new WidgetDragBarVec3[meshSize];
        mShowAOViews = new WidgetSwitch[meshSize];
        mAOViews = new WidgetTexture[meshSize];
        mShowMetallicViews = new WidgetSwitch[meshSize];
        mShowMetallicViews = new WidgetSwitch[meshSize];
        mMetallicViews = new WidgetTexture[meshSize];
        mMetallicValViews = new WidgetDragBar[meshSize];
        mShowRoughnessViews = new WidgetSwitch[meshSize];
        mRoughnessViews = new WidgetTexture[meshSize];
        mRoughnessValViews = new WidgetDragBar[meshSize];
        List<String> shaderList = (List<String>) mGLCommunicator.getData("ShaderList");

        for (int i = 0; i < model.getMeshes().size(); i++) {
            Mesh mesh = model.getMeshes().get(i);
            mMeshPropertyGroupViews[i] = new WidgetPropertyGroup(getContext(), new PropertyGroup(mesh.getName()));
            final WidgetPropertyGroup currentGroupViews = mMeshPropertyGroupViews[i];

            // shader
            String meshCurrentSelectedShader = (String) mGLCommunicator.getData(mesh.getName() + "_currentSelectedShader");
            WidgetDropDownList.OnItemSelectedListener listener = new WidgetDropDownList.OnItemSelectedListener() {
                @Override
                public void OnItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Integer index = mDropDownListParentHashMap.get(this);
                    mSelectedMeshIndex.setVal(index);
                    String selectedItem = (String) parent.getItemAtPosition(position);
                    mGLCommunicator.putData(model.getMeshes().get(index).getName() + "_currentSelectedShader", selectedItem);
                    mGLCommunicator.setCurrentGLStatus(GLRequest.SWITCH_SHADER);
                    GLRenderer.isReLoad = true;
                    mMeshPropertyGroupViews[index].removeAllSubViews(5);
                    refreshTexture();
                }
            };
            mDropDownListParentHashMap.put(listener, i);
            WidgetDropDownList shaderListView = new WidgetDropDownList(getContext(), new PropertyDefinition("着色方式", shaderList, listener, ListUtil.getIndex(shaderList, meshCurrentSelectedShader)));
            mMeshPropertyGroupViews[i].addSubView(shaderListView);
            mMeshPropertyGroupViews[i].addSubView(new WidgetDivider(getContext()));

            mShowMeshViews[i] = new WidgetSwitch(getContext(), new PropertyDefinition("显示网格", mesh.getIsVisible()));
            mShowWireFrameViews[i] = new WidgetSwitch(getContext(), new PropertyDefinition("显示线框", mesh.getIsShowWireFrame()));
            mMeshPropertyGroupViews[i].addSubView(mShowMeshViews[i]);
            mMeshPropertyGroupViews[i].addSubView(mShowWireFrameViews[i]);
            mMeshPropertyGroupViews[i].addSubView(new WidgetDivider(getContext()));

            // texture
            TraditionalMaterial traditionalMaterial = (TraditionalMaterial) mGLCommunicator.getData(mesh.getName() + "_shader_traditional");
            PBRMaterial pbrMaterial = (PBRMaterial) mGLCommunicator.getData(mesh.getName() + "_shader_physicalBased");
            switch (meshCurrentSelectedShader) {
                case "传统着色":
                    initTraditional(currentGroupViews, traditionalMaterial, i);
                    break;
                case "PBR着色":
                    initPBR(currentGroupViews, pbrMaterial, i);
                    break;
            }

            addGroupView(mMeshPropertyGroupViews[i]);
        }
    }
    private void refreshTexture(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mGLCommunicator.getCurrentGLRequest().equals(GLRequest.RUNNING)){
//                if(mGLCommunicator.isFree()){
                    int i = mSelectedMeshIndex.getVal();
                    TraditionalMaterial currentMeshTraditionalMaterial = (TraditionalMaterial) mGLCommunicator.getData(model.getMeshes().get(i).getName() + "_shader_traditional");
                    PBRMaterial currentMeshPBRMaterial = (PBRMaterial) mGLCommunicator.getData(model.getMeshes().get(i).getName() + "_shader_physicalBased");
                    String meshCurrentSelectedShader = (String) mGLCommunicator.getData(model.getMeshes().get(i).getName() + "_currentSelectedShader");
                    switch (meshCurrentSelectedShader) {
                        case "传统着色":
                            initTraditional(mMeshPropertyGroupViews[i], currentMeshTraditionalMaterial, i);
                            break;
                        case "PBR着色":
                            initPBR(mMeshPropertyGroupViews[i], currentMeshPBRMaterial, i);
                            break;
                    }
                    mMeshPropertyGroupViews[i].requestLayout();
                }else {
                    refreshTexture();
                }
            }
        }, 10);
    }

    private void initTraditional(final WidgetPropertyGroup currentGroupViews, final Material currentMeshMaterial, int i){
        mDiffuseViews[i] = new WidgetTexture(getContext(), new PropertyDefinition("漫反射贴图", ((TraditionalMaterial) currentMeshMaterial).getDiffuseMap()));
        mDiffuseDragView[i] = new WidgetDragBarVec3(getContext(), new PropertyDefinition("漫反射颜色", new Range(0.0f, 1.0f, 0.01f), ((TraditionalMaterial) currentMeshMaterial).getObjectColor()));
        final int finalI = i;
        PropertyDefinition showDiffuse = new PropertyDefinition("使用漫反射贴图", ((TraditionalMaterial) currentMeshMaterial).getIsUseDiffuseMap());
        showDiffuse.setOnSwitchChangeListener(new WidgetSwitch.OnSwitchChangeListener() {
            @Override
            public void onSwitchChanged(boolean val) {
                if (val){
                    currentGroupViews.addSubView(mDiffuseViews[finalI], 6);
                    if (currentGroupViews.isHaveSubView(mDiffuseDragView[finalI])){
                        currentGroupViews.removeSubView(mDiffuseDragView[finalI]);
                    }
                }else {
                    currentGroupViews.addSubView(mDiffuseDragView[finalI], 6);
                    if (currentGroupViews.isHaveSubView(mDiffuseViews[finalI])){
                        currentGroupViews.removeSubView(mDiffuseViews[finalI]);
                    }
                }
                requestLayout();
            }
        });
        mShowDiffuseViews[i] = new WidgetSwitch(getContext(), showDiffuse);
        mMeshPropertyGroupViews[i].addSubView(mShowDiffuseViews[i]);
        if (((TraditionalMaterial) currentMeshMaterial).getIsUseDiffuseMap().getVal()) {
            mMeshPropertyGroupViews[i].addSubView(mDiffuseViews[i]);
        }else {
            mMeshPropertyGroupViews[i].addSubView(mDiffuseDragView[i]);
        }

        mSpecularViews[i] = new WidgetTexture(getContext(), new PropertyDefinition("高光贴图", ((TraditionalMaterial) currentMeshMaterial).getSpecularMap()));
        mSpecularDragViews[i] = new WidgetDragBarVec3(getContext(), new PropertyDefinition("高光颜色", new Range(0.0f, 1.0f, 0.01f), ((TraditionalMaterial) currentMeshMaterial).getObjectSpecular()));
        PropertyDefinition showSpecular = new PropertyDefinition("使用高光贴图", ((TraditionalMaterial) currentMeshMaterial).getIsUseSpecularMap());
        showSpecular.setOnSwitchChangeListener(new WidgetSwitch.OnSwitchChangeListener() {
            @Override
            public void onSwitchChanged(boolean val) {
                if (val){
                    currentGroupViews.addSubView(mSpecularViews[finalI], 9);
                    if (currentGroupViews.isHaveSubView(mSpecularDragViews[finalI])){
                        currentGroupViews.removeSubView(mSpecularDragViews[finalI]);
                    }
                }else {
                    currentGroupViews.addSubView(mSpecularDragViews[finalI], 9);
                    if (currentGroupViews.isHaveSubView(mSpecularViews[finalI])){
                        currentGroupViews.removeSubView(mSpecularViews[finalI]);
                    }
                }
                requestLayout();
            }
        });
        mShowSpecularViews[i] = new WidgetSwitch(getContext(), showSpecular);
        mMeshPropertyGroupViews[i].addSubView(mShowSpecularViews[i]);
        if (((TraditionalMaterial) currentMeshMaterial).getIsUseSpecularMap().getVal()) {
            mMeshPropertyGroupViews[i].addSubView(mSpecularViews[i]);
        }else {
            mMeshPropertyGroupViews[i].addSubView(mSpecularDragViews[i]);
        }


        mNormalViews[i] = new WidgetTexture(getContext(), new PropertyDefinition("法线贴图", ((TraditionalMaterial) currentMeshMaterial).getNormalMap()));
        PropertyDefinition showNormal = new PropertyDefinition("使用法线贴图", ((TraditionalMaterial) currentMeshMaterial).getIsUseTangentSpace());
        showNormal.setOnSwitchChangeListener(new WidgetSwitch.OnSwitchChangeListener() {
            @Override
            public void onSwitchChanged(boolean val) {
                if (val){
                    currentGroupViews.addSubView(mNormalViews[finalI], 10);
                }else {
                    currentGroupViews.removeSubView(mNormalViews[finalI]);
                }
                requestLayout();
            }
        });
        mShowNormalViews[i] = new WidgetSwitch(getContext(), showNormal);
        mMeshPropertyGroupViews[i].addSubView(mShowNormalViews[i]);
        if (((TraditionalMaterial) currentMeshMaterial).getIsUseTangentSpace().getVal()){
            mMeshPropertyGroupViews[i].addSubView(mNormalViews[i]);
        }


        mEnableHeightHalfRangeViews[i] = new WidgetSwitch(getContext(), new PropertyDefinition("选用一半高度", ((TraditionalMaterial) currentMeshMaterial).getIsUseHeightMapHalfRange()));
        mEnableDiscardEdgeViews[i] = new WidgetSwitch(getContext(), new PropertyDefinition("边界剔除", ((TraditionalMaterial) currentMeshMaterial).getDiscardHeightMapEdge()));
        mHeightScaleViews[i] = new WidgetDragBar(getContext(), new PropertyDefinition("高度大小", new Range(0.01f, 0.1f, 0.01f), ((TraditionalMaterial) currentMeshMaterial).getHeightScale()));
        mHeightViews[i] = new WidgetTexture(getContext(), new PropertyDefinition("高度贴图", ((TraditionalMaterial) currentMeshMaterial).getHeightMap()));
        PropertyDefinition showHeightMap = new PropertyDefinition("使用高度贴图", ((TraditionalMaterial) currentMeshMaterial).getIsUseDisplacementMap());
        showHeightMap.setOnSwitchChangeListener(new WidgetSwitch.OnSwitchChangeListener() {
            @Override
            public void onSwitchChanged(boolean val) {
                if (val){
                    currentGroupViews.addSubView(mEnableHeightHalfRangeViews[finalI]);
                    currentGroupViews.addSubView(mEnableDiscardEdgeViews[finalI]);
                    currentGroupViews.addSubView(mHeightScaleViews[finalI]);
                    currentGroupViews.addSubView(mHeightViews[finalI]);
                }else {
                    currentGroupViews.removeSubView(mEnableHeightHalfRangeViews[finalI]);
                    currentGroupViews.removeSubView(mEnableDiscardEdgeViews[finalI]);
                    currentGroupViews.removeSubView(mHeightScaleViews[finalI]);
                    currentGroupViews.removeSubView(mHeightViews[finalI]);
                }
                requestLayout();
            }
        });
        mShowHeightViews[i] = new WidgetSwitch(getContext(), showHeightMap);
        mMeshPropertyGroupViews[i].addSubView(mShowHeightViews[i]);
        if (((TraditionalMaterial) currentMeshMaterial).getIsUseDisplacementMap().getVal()){
            mMeshPropertyGroupViews[i].addSubView(mEnableHeightHalfRangeViews[finalI]);
            mMeshPropertyGroupViews[i].addSubView(mEnableDiscardEdgeViews[finalI]);
            mMeshPropertyGroupViews[i].addSubView(mHeightScaleViews[finalI]);
            mMeshPropertyGroupViews[i].addSubView(mHeightViews[finalI]);
        }
    }

    private void initPBR(final WidgetPropertyGroup currentGroupViews, Material currentMeshMaterial, int i){
        final int finalI = i;
        mAlbedoViews[i] = new WidgetTexture(getContext(), new PropertyDefinition("反照率贴图", ((PBRMaterial) currentMeshMaterial).getAlbedoMap()));
        mAlbedoValViews[i] = new WidgetDragBarVec3(getContext(), new PropertyDefinition("反照率", new Range(0.0f, 1.0f, 0.01f), ((PBRMaterial) currentMeshMaterial).getAlbedoVal()));
        PropertyDefinition showAlbedo = new PropertyDefinition("使用反照率贴图", ((PBRMaterial) currentMeshMaterial).getIsUseAlbedoMap());
        showAlbedo.setOnSwitchChangeListener(new WidgetSwitch.OnSwitchChangeListener() {
            @Override
            public void onSwitchChanged(boolean val) {
                if (val){
                    currentGroupViews.addSubView(mAlbedoViews[finalI], 6);
                    if (currentGroupViews.isHaveSubView(mAlbedoValViews[finalI])){
                        currentGroupViews.removeSubView(mAlbedoValViews[finalI]);
                    }
                }else {
                    currentGroupViews.addSubView(mAlbedoValViews[finalI], 6);
                    if (currentGroupViews.isHaveSubView(mAlbedoViews[finalI])){
                        currentGroupViews.removeSubView(mAlbedoViews[finalI]);
                    }
                }
                requestLayout();
            }
        });
        mShowAlbedoViews[i] = new WidgetSwitch(getContext(), showAlbedo);
        mMeshPropertyGroupViews[i].addSubView(mShowAlbedoViews[i]);
        if (((PBRMaterial) currentMeshMaterial).getIsUseAlbedoMap().getVal()){
            mMeshPropertyGroupViews[i].addSubView(mAlbedoViews[i]);
        }else {
            mMeshPropertyGroupViews[i].addSubView(mAlbedoValViews[i]);
        }


        mAOViews[i] = new WidgetTexture(getContext(), new PropertyDefinition("环境光遮蔽贴图", ((PBRMaterial) currentMeshMaterial).getAoMap()));
        PropertyDefinition showAOMap = new PropertyDefinition("使用环境光遮蔽贴图", ((PBRMaterial) currentMeshMaterial).getIsUseAOMap());
        showAOMap.setOnSwitchChangeListener(new WidgetSwitch.OnSwitchChangeListener() {
            @Override
            public void onSwitchChanged(boolean val) {
                if (val){
                    currentGroupViews.addSubView(mAOViews[finalI], 8);
                }else {
                    currentGroupViews.removeSubView(mAOViews[finalI]);
                }
                requestLayout();
            }
        });
        mShowAOViews[i] = new WidgetSwitch(getContext(), showAOMap);
        mMeshPropertyGroupViews[i].addSubView(mShowAOViews[i]);
        if (((PBRMaterial) currentMeshMaterial).getIsUseAOMap().getVal()) {
            mMeshPropertyGroupViews[i].addSubView(mAOViews[i]);
        }


        mMetallicViews[i] = new WidgetTexture(getContext(), new PropertyDefinition("金属度贴图", ((PBRMaterial) currentMeshMaterial).getMetallicMap()));
        mMetallicValViews[i] = new WidgetDragBar(getContext(), new PropertyDefinition("金属度", new Range(0.0f, 1.0f, 0.01f), ((PBRMaterial) currentMeshMaterial).getMetallicVal()));
        PropertyDefinition showMetallic = new PropertyDefinition("使用金属度贴图", ((PBRMaterial) currentMeshMaterial).getIsUseMetallicMap());
        showMetallic.setOnSwitchChangeListener(new WidgetSwitch.OnSwitchChangeListener() {
            @Override
            public void onSwitchChanged(boolean val) {
                if (val){
                    currentGroupViews.addSubView(mMetallicViews[finalI], 10);
                    if (currentGroupViews.isHaveSubView(mMetallicValViews[finalI])){
                        currentGroupViews.removeSubView(mMetallicValViews[finalI]);
                    }
                }else {
                    currentGroupViews.addSubView(mMetallicValViews[finalI], 10);
                    if (currentGroupViews.isHaveSubView(mMetallicViews[finalI])){
                        currentGroupViews.removeSubView(mMetallicViews[finalI]);
                    }
                }
                requestLayout();
            }
        });
        mShowMetallicViews[i] = new WidgetSwitch(getContext(), showMetallic);
        mMeshPropertyGroupViews[i].addSubView(mShowMetallicViews[i]);
        if (((PBRMaterial) currentMeshMaterial).getIsUseMetallicMap().getVal()) {
            mMeshPropertyGroupViews[i].addSubView(mMetallicViews[i]);
        }else {
            mMeshPropertyGroupViews[i].addSubView(mMetallicValViews[i]);
        }

        mRoughnessViews[i] = new WidgetTexture(getContext(), new PropertyDefinition("粗糙度贴图", ((PBRMaterial) currentMeshMaterial).getRoughnessMap()));
        mRoughnessValViews[i] = new WidgetDragBar(getContext(), new PropertyDefinition("粗糙度", new Range(0.0f, 1.0f, 0.01f), ((PBRMaterial) currentMeshMaterial).getRoughnessVal()));
        PropertyDefinition showRoughness = new PropertyDefinition("使用粗糙度贴图", ((PBRMaterial) currentMeshMaterial).getIsUseRoughnessMap());
        showRoughness.setOnSwitchChangeListener(new WidgetSwitch.OnSwitchChangeListener() {
            @Override
            public void onSwitchChanged(boolean val) {
                if (val){
                    currentGroupViews.addSubView(mRoughnessViews[finalI], 12);
                    if (currentGroupViews.isHaveSubView(mRoughnessValViews[finalI])){
                        currentGroupViews.removeSubView(mRoughnessValViews[finalI]);
                    }
                }else {
                    currentGroupViews.addSubView(mRoughnessValViews[finalI], 12);
                    if (currentGroupViews.isHaveSubView(mRoughnessViews[finalI])){
                        currentGroupViews.removeSubView(mRoughnessViews[finalI]);
                    }
                }
                requestLayout();
            }
        });
        mShowRoughnessViews[i] = new WidgetSwitch(getContext(), showRoughness);
        mMeshPropertyGroupViews[i].addSubView(mShowRoughnessViews[i]);
        if (((PBRMaterial) currentMeshMaterial).getIsUseMetallicMap().getVal()) {
            mMeshPropertyGroupViews[i].addSubView(mRoughnessViews[i]);
        }else {
            mMeshPropertyGroupViews[i].addSubView(mRoughnessValViews[i]);
        }

        mNormalViews[i] = new WidgetTexture(getContext(), new PropertyDefinition("法线贴图", ((PBRMaterial) currentMeshMaterial).getNormalMap()));
        PropertyDefinition showNormal = new PropertyDefinition("使用法线贴图", ((PBRMaterial) currentMeshMaterial).getIsHasNormal());
        showNormal.setOnSwitchChangeListener(new WidgetSwitch.OnSwitchChangeListener() {
            @Override
            public void onSwitchChanged(boolean val) {
                if (val){
                    currentGroupViews.addSubView(mNormalViews[finalI], 13);
                }else {
                    currentGroupViews.removeSubView(mNormalViews[finalI]);
                }
                requestLayout();
            }
        });
        mShowNormalViews[i] = new WidgetSwitch(getContext(), showNormal);
        mMeshPropertyGroupViews[i].addSubView(mShowNormalViews[i]);
        if (((PBRMaterial) currentMeshMaterial).getIsHasNormal().getVal()){
            mMeshPropertyGroupViews[i].addSubView(mNormalViews[i]);
        }

        mEnableHeightHalfRangeViews[i] = new WidgetSwitch(getContext(), new PropertyDefinition("选用一半高度", ((PBRMaterial) currentMeshMaterial).getUseHalfHeightRange()));
        mHeightScaleViews[i] = new WidgetDragBar(getContext(), new PropertyDefinition("高度大小", new Range(0.01f, 0.1f, 0.01f), ((PBRMaterial) currentMeshMaterial).getHeightScale()));
        mEnableDiscardEdgeViews[i] = new WidgetSwitch(getContext(), new PropertyDefinition("边界剔除", ((PBRMaterial) currentMeshMaterial).getDiscardEdge()));
        mHeightViews[i] = new WidgetTexture(getContext(), new PropertyDefinition("高度贴图", ((PBRMaterial) currentMeshMaterial).getHeightMap()));
        PropertyDefinition showHeightMap = new PropertyDefinition("使用高度贴图", ((PBRMaterial) currentMeshMaterial).getIsHasHeightMap());
        showHeightMap.setOnSwitchChangeListener(new WidgetSwitch.OnSwitchChangeListener() {
            @Override
            public void onSwitchChanged(boolean val) {
                if (val){
                    currentGroupViews.addSubView(mEnableHeightHalfRangeViews[finalI]);
                    currentGroupViews.addSubView(mEnableDiscardEdgeViews[finalI]);
                    currentGroupViews.addSubView(mHeightScaleViews[finalI]);
                    currentGroupViews.addSubView(mHeightViews[finalI]);
                }else {
                    currentGroupViews.removeSubView(mEnableHeightHalfRangeViews[finalI]);
                    currentGroupViews.removeSubView(mEnableDiscardEdgeViews[finalI]);
                    currentGroupViews.removeSubView(mHeightScaleViews[finalI]);
                    currentGroupViews.removeSubView(mHeightViews[finalI]);
                }
                requestLayout();
            }
        });
        mShowHeightViews[i] = new WidgetSwitch(getContext(), showHeightMap);
        mMeshPropertyGroupViews[i].addSubView(mShowHeightViews[i]);
        if (((PBRMaterial) currentMeshMaterial).getIsHasHeightMap().getVal()){
            mMeshPropertyGroupViews[i].addSubView(mEnableHeightHalfRangeViews[finalI]);
            mMeshPropertyGroupViews[i].addSubView(mEnableDiscardEdgeViews[finalI]);
            mMeshPropertyGroupViews[i].addSubView(mHeightScaleViews[finalI]);
            mMeshPropertyGroupViews[i].addSubView(mHeightViews[finalI]);
        }

    }
}
