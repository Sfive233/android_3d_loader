package com.example.android_3d_loader.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.android_3d_loader.controller.GLRequest;
import com.example.android_3d_loader.view.util.FileUtil;
import com.example.android_3d_loader.core.GLRenderer;
import com.example.android_3d_loader.R;
import com.example.android_3d_loader.controller.communicator.GLCommunicator;
import com.example.android_3d_loader.core.texture.texture2D.Texture2D;
import com.example.android_3d_loader.core.model.Model;
import com.example.android_3d_loader.view.widget.WidgetRightSidePanel;
import com.example.android_3d_loader.view.widget.WidgetTexture;

import static android.app.Activity.RESULT_OK;

public class GLESWindow extends DrawerLayout {
    private static final String TAG = "MyGLESWindow";
    private GLContentContainer mGLContentContainer;
    private GLSurfaceView mGLSurfaceView;
    private WidgetRightSidePanel mRightSidePanel;
    private GLCommunicator mGLCommunicator;

    public GLESWindow(@NonNull Context context) {
        super(context);
        init();
    }

    public GLESWindow(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.my_gles_window, this);
        DrawerLayout mRoot = findViewById(R.id.my_gles_window_drawer);
        FrameLayout mWindow = findViewById(R.id.window);
        mGLContentContainer = new GLContentContainer(getContext());
        mGLSurfaceView = mGLContentContainer.getGlSurfaceView();
        mWindow.addView(mGLContentContainer, 0);

        mRightSidePanel = findViewById(R.id.right_side_panel);

        GUIContainer mGUIContainer = new GUIContainer(getContext(), mGLContentContainer, mRoot);
        mWindow.addView(mGUIContainer);

        mRoot.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mRoot.setScrimColor(Color.TRANSPARENT);
        mRoot.addDrawerListener(new SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                mGLContentContainer.setSidePanelOpen(true);
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                mGLContentContainer.setSidePanelOpen(false);
                super.onDrawerClosed(drawerView);
            }
        });

        mGLCommunicator = GLCommunicator.getInstance();
        mGLCommunicator.setGLSurfaceView(mGLSurfaceView);

        mGLCommunicator.loadSavedData(getContext());



        initOptions();
    }

    public void initOptions(){
        mGLSurfaceView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mGLCommunicator.getCurrentGLRequest().equals(GLRequest.RUNNING)) {
//                if(mGLCommunicator.isFree()){
                    mRightSidePanel.createProperties();
                }else {
                    initOptions();
                }
            }
        }, 1000);
    }

    public boolean isRendererSetUp() {
        return mGLContentContainer.isRendererSetUp();
    }

    public void handleOnPause(){
        if (GLCommunicator.getInstance().getCurrentGLRequest() == GLRequest.RUNNING) {
//        if(mGLCommunicator.isFree()){
            GLCommunicator.getInstance().save(getContext());
        }
        if (isRendererSetUp()) {
            mGLSurfaceView.onPause();
        }
    }

    public void handleOnResume(){
        if (isRendererSetUp()) {
            mGLSurfaceView.onResume();
        }

        if (GLCommunicator.getInstance().getCurrentGLRequest() != GLRequest.BOOT &&
                GLCommunicator.getInstance().getCurrentGLRequest() != GLRequest.BACK_FROM_SWAPPING_TEXTURE &&
                GLCommunicator.getInstance().getCurrentGLRequest() != GLRequest.BACK_FROM_SWAPPING_MODEL &&
                GLCommunicator.getInstance().getCurrentGLRequest() != GLRequest.BACK_FROM_SWAPPING_HDRI) {

            GLCommunicator.getInstance().setCurrentGLStatus(GLRequest.BACK_FROM_BACKGROUND);
        }
    }

    public static final int OPEN_TEXTURE = 10;
    public static final int OPEN_MODEL = 11;
    public static final int OPEN_HDRI = 12;
    public void handleOnResult(int requestCode, int resultCode, @Nullable Intent data){
        if (resultCode == RESULT_OK && null != data) {

            Uri uri = data.getData();
            Log.d(TAG, "onActivityResult Scheme: " + uri.getScheme());
            Log.d(TAG, "onActivityResult Authority: " + uri.getAuthority());
            Log.d(TAG, "onActivityResult Path: " + uri.getPath());
            String filePath = FileUtil.getFilePath(getContext(), uri);
            Log.d(TAG, "onActivityResult filePath: " + filePath);
            String format = filePath.substring(filePath.indexOf(".")).toLowerCase();

            switch (requestCode) {
                case OPEN_TEXTURE:
                    switch (format) {
                        case ".png":
                        case ".jpeg":
                        case ".jpg":
                            break;
                        default:
                            Toast.makeText(getContext(), "Format \"" + format + "\" not supported!", Toast.LENGTH_LONG).show();
                            return;
                    }
                    WidgetTexture lastWidgetTexture = WidgetTexture.getLastClickedWidgetTexture();
                    Texture2D texture2D = lastWidgetTexture.getPropertyDefinition().getBindTexture();
                    texture2D.setPath(filePath);
                    lastWidgetTexture.initTexturePreview();
                    GLCommunicator.getInstance().setCurrentGLStatus(GLRequest.BACK_FROM_SWAPPING_TEXTURE);
                    break;
                case OPEN_MODEL:
                    switch (format) {
                        case ".obj":
                        case ".stl":
                            break;
                        default:
                            Toast.makeText(getContext(), "Format \"" + format + "\" not supported!", Toast.LENGTH_LONG).show();
                            return;
                    }
                    Model model = (Model) GLCommunicator.getInstance().getData("Model");
                    model.setModelPath(filePath);
                    GLCommunicator.getInstance().setCurrentGLStatus(GLRequest.BACK_FROM_SWAPPING_MODEL);
                    this.initOptions();
                    break;
                case OPEN_HDRI:
                    if (format.equals(".hdr")) {
                        GLCommunicator.getInstance().putData("SkyboxPath", filePath);
                        GLCommunicator.getInstance().setCurrentGLStatus(GLRequest.BACK_FROM_SWAPPING_HDRI);
                        this.initOptions();
                    } else {
                        Toast.makeText(getContext(), "Format \"" + format + "\" not supported!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    break;
            }
            Log.d(TAG, "onActivityResult: Complete");
        }
    }
}
