package com.example.android_3d_loader.view.widget;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.android_3d_loader.R;
import com.example.android_3d_loader.view.widget.dragBar.WidgetDragBar;
import com.example.android_3d_loader.view.widget.dragBar.WidgetDragBarVec3;
import com.example.android_3d_loader.view.widget.dropDownList.WidgetDropDownList;
import com.example.android_3d_loader.view.property.PropertyDefinition;
import com.example.android_3d_loader.view.property.PropertyGroup;

import static com.example.android_3d_loader.view.GLESWindow.OPEN_TEXTURE;

public class WidgetPropertyGroup extends LinearLayout {
    private static final String TAG = "WidgetPropertyGroup";
    private final LinearLayout mPropertyGroupSubs;
    private final TextView mPropertyGroupName;
    private final ImageView mPropertyGroupIcon;
    private final RelativeLayout mPropertyGroupTitle;
    private final PropertyGroup mPropertyGroup;

    public WidgetPropertyGroup(Context context, PropertyGroup propertyGroup) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.widget_property_group, this);

        mPropertyGroup = propertyGroup;
        mPropertyGroupTitle = findViewById(R.id.property_group_title);
        mPropertyGroupName = findViewById(R.id.property_group_name);
        mPropertyGroupIcon = findViewById(R.id.property_group_icon);
        mPropertyGroupSubs = findViewById(R.id.property_group_subs);

        init();
    }

    private void init(){
        if (mPropertyGroup.isShowTitle()) {

            mPropertyGroupName.setText(mPropertyGroup.getName());

            if (mPropertyGroup.isClosable()) {
                mPropertyGroupTitle.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mPropertyGroup.isClosed()){
                            openSubs();
                        }else {
                            closeSubs();
                        }
                        mPropertyGroup.setClosed(!mPropertyGroup.isClosed());
                    }
                });

                if (mPropertyGroup.isClosed()) {
                    closeSubs();
                } else {
                    openSubs();
                }
            }else {
                mPropertyGroupIcon.setVisibility(GONE);
            }
        }else {
            mPropertyGroupTitle.setVisibility(GONE);
        }

        loadProperties();
    }

    private void closeSubs(){
        mPropertyGroupIcon.setRotation(0.0f);
        mPropertyGroupSubs.setVisibility(GONE);
        mPropertyGroupSubs.requestLayout();
    }

    private void openSubs(){
        mPropertyGroupIcon.setRotation(-90.0f);
        mPropertyGroupSubs.setVisibility(VISIBLE);
        mPropertyGroupSubs.requestLayout();
    }

    private void loadProperties() {
        for (PropertyDefinition propertyDefinition: mPropertyGroup.getProperties()){
            switch (propertyDefinition.getPropertyType()){
                case DragBar:
                    WidgetDragBar widgetDragBar = new WidgetDragBar(getContext(), propertyDefinition);
                    mPropertyGroupSubs.addView(widgetDragBar);
                    break;
                case DragBarVec3:
                    WidgetDragBarVec3 widgetDragBarVec3 = new WidgetDragBarVec3(getContext(), propertyDefinition);
                    mPropertyGroupSubs.addView(widgetDragBarVec3);
                    break;
                case Switch:
                    WidgetSwitch widgetSwitch = new WidgetSwitch(getContext(), propertyDefinition);
                    mPropertyGroupSubs.addView(widgetSwitch);
                    break;
                case Button:
                    WidgetButton widgetButton = new WidgetButton(getContext(), propertyDefinition);
                    mPropertyGroupSubs.addView(widgetButton);
                    break;
                case Texture:
                    final WidgetTexture widgetTexture = new WidgetTexture(getContext(), propertyDefinition);
                    widgetTexture.onTextureClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int permissionGrantedCode = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
                            if (permissionGrantedCode != PackageManager.PERMISSION_GRANTED) {
                                // 请求内部存储访问权限
                                ActivityCompat.requestPermissions((Activity)getContext(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                            }else {
                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                intent.setType("*/*");
                                intent.addCategory(Intent.CATEGORY_OPENABLE);
                                WidgetTexture.setLastClickedWidgetTexture(widgetTexture);
                                ((Activity)getContext()).startActivityForResult(intent, OPEN_TEXTURE);
                            }
                        }
                    });
                    mPropertyGroupSubs.addView(widgetTexture);
                    break;
                case DropDownList:
                    WidgetDropDownList widgetDropDownList = new WidgetDropDownList(getContext(), propertyDefinition);
                    mPropertyGroupSubs.addView(widgetDropDownList);
                    break;
                case Text:
                    WidgetDescription widgetDescription = new WidgetDescription(getContext(), propertyDefinition);
                    mPropertyGroupSubs.addView(widgetDescription);
                    break;
            }
        }
    }

    public void addSubView(final View view){
        addSubView(view, null);
    }

    public void addSubView(final View view, Integer index){
        if (view instanceof WidgetTexture){
            ((WidgetTexture)view).onTextureClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int permissionGrantedCode = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
                    if (permissionGrantedCode != PackageManager.PERMISSION_GRANTED) {
                        // 请求内部存储访问权限
                        ActivityCompat.requestPermissions((Activity)getContext(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    }else {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("*/*");
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        WidgetTexture.setLastClickedWidgetTexture(((WidgetTexture)view));
                        ((Activity)getContext()).startActivityForResult(intent, OPEN_TEXTURE);
                    }
                }
            });
        }
        if (index == null){
            mPropertyGroupSubs.addView(view);
        }else {
            mPropertyGroupSubs.addView(view, index);
        }

    }

    public void setClosable(boolean isClosable){
        mPropertyGroupIcon.setVisibility(isClosable? VISIBLE: GONE);
        requestLayout();
    }

    public void setShowTitle(boolean isShowTitle){
        mPropertyGroupTitle.setVisibility(isShowTitle? VISIBLE: GONE);
        requestLayout();
    }

    public void removeSubView(View view){
        mPropertyGroupSubs.removeView(view);
    }

    public void removeAllSubViews(int start){
        mPropertyGroupSubs.removeViews(start, mPropertyGroupSubs.getChildCount() - start);
    }

    public boolean isHaveSubView(View view){
        return !(getSubViewIndex(view) == -1);
    }

    public int getSubViewIndex(View view){
        for (int i = 1; i <= mPropertyGroupSubs.getChildCount(); i++){
            View subView = mPropertyGroupSubs.getChildAt(i);
            if (view != null && subView == view){
                return i;
            }
        }
        return -1;
    }
}
