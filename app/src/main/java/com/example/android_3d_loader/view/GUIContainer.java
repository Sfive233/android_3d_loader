package com.example.android_3d_loader.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.android_3d_loader.R;
import com.example.android_3d_loader.controller.communicator.GLCommunicator;
import com.example.android_3d_loader.view.widget.WidgetFPSCounter;
import com.example.android_3d_loader.view.widget.notice.WidgetNoticePanel;

public class GUIContainer extends ConstraintLayout {
    private static final String TAG = "GUIContainer";
    private final GLContentContainer mGLContentContainer;
    private final DrawerLayout mDrawerRoot;
    private final ImageView mIconCameraRotate;
    private final ImageView mIconCameraDrag;
    private final ImageView mIconList;
    private final WidgetFPSCounter mFPSCounter;
//    private final ImageView mNoticeIcon;
//    private final WidgetNoticePanel mNoticePanel;
    private boolean mIsGUIBottomShow = true;
    private boolean mIsGUITopShow = true;

    public GUIContainer(Context context, GLContentContainer glContentContainer, DrawerLayout drawerRoot) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.gui_container, this);

        mGLContentContainer = glContentContainer;
        mDrawerRoot = drawerRoot;
        mIconCameraRotate = findViewById(R.id.ic_camera_rotate);
        mIconCameraDrag = findViewById(R.id.ic_camera_drag);
        mIconList = findViewById(R.id.ic_list);
        mFPSCounter = findViewById(R.id.fps_counter);
//        mNoticeIcon = findViewById(R.id.notice_icon);
//        mNoticePanel = findViewById(R.id.notice_panel);

        init();
    }

    private void init(){
        mIconCameraRotate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mGLContentContainer.getCameraControlMode().equals(GLContentContainer.CameraControlMode.ROTATE)){
                    mGLContentContainer.setCameraControlMode(GLContentContainer.CameraControlMode.ROTATE);
                }
                mIconCameraRotate.setSelected(!mIconCameraRotate.isSelected());
                mIconCameraDrag.setSelected(!mIconCameraDrag.isSelected());
            }
        });
        mIconCameraRotate.setSelected(true);
        mIconCameraDrag.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mGLContentContainer.getCameraControlMode().equals(GLContentContainer.CameraControlMode.DRAG)) {
                    mGLContentContainer.setCameraControlMode(GLContentContainer.CameraControlMode.DRAG);
                }
                mIconCameraRotate.setSelected(!mIconCameraRotate.isSelected());
                mIconCameraDrag.setSelected(!mIconCameraDrag.isSelected());
            }
        });
        mIconCameraDrag.setSelected(false);

        mIconList.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeGUITopVisibility();
                changeGUIBottomVisibility();
                mDrawerRoot.openDrawer(Gravity.RIGHT);
            }
        });
        mDrawerRoot.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                changeGUITopVisibility();
                changeGUIBottomVisibility();
            }
        });

//        mNoticeIcon.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mNoticePanel.setVisibility(mNoticePanel.getVisibility() == VISIBLE ? INVISIBLE: VISIBLE);
//                mNoticeIcon.setSelected(!mNoticeIcon.isSelected());
//            }
//        });
//        mNoticeIcon.setSelected(false);
//        mNoticePanel.setVisibility(INVISIBLE);

        GLCommunicator.getInstance().addTemp("FPSCounter", mFPSCounter);
        mFPSCounter.beginCount();
    }

    public void changeGUITopVisibility(){
        mIconList.setVisibility(mIsGUITopShow ? GONE : VISIBLE);
        mIsGUITopShow = !mIsGUITopShow;
    }

    public void changeGUIBottomVisibility(){
        mIconCameraDrag.setVisibility(mIsGUIBottomShow ? GONE : VISIBLE);
        mIconCameraRotate.setVisibility(mIsGUIBottomShow ? GONE : VISIBLE);
        mIsGUIBottomShow = !mIsGUIBottomShow;
    }
}
