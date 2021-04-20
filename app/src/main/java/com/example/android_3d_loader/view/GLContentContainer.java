package com.example.android_3d_loader.view;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.android_3d_loader.controller.communicator.GLCommunicator;
import com.example.android_3d_loader.core.GLRenderer;
import com.example.android_3d_loader.R;
import com.example.android_3d_loader.core.camera.OrbitCamera;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_POINTER_UP;
import static android.view.MotionEvent.ACTION_UP;

public class GLContentContainer extends FrameLayout implements View.OnTouchListener {

    private static final String TAG = "GLContentContainer";
    private GLSurfaceView glSurfaceView;
    private GLRenderer GLRenderer;
    private boolean isRendererSetUp;

    private ScaleGestureDetector mScaleGestureDetector;
    private GestureDetector mGestureDetector;

    private enum CameraDragMode{
        DRAG, ZOOM, NONE
    }
    public enum CameraControlMode{
        ROTATE, DRAG
    }
    private CameraDragMode mCameraDragMode;
    private CameraControlMode mCameraControlMode;

    private boolean mIsSidePanelOpen = false;

    private GLCommunicator glCommunicator;

    public GLContentContainer(@NonNull Context context) {
        super(context);
        init();
    }

    public GLContentContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.gl_content_container, this);

        ActivityManager activityManager = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        if(configurationInfo.reqGlEsVersion >= 0x30000){
            glSurfaceView = new GLSurfaceView(getContext());
            glSurfaceView.setEGLContextClientVersion(3);
            //在Android中默认没有配置模板测试，需要手动配置
            glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 8);
            glSurfaceView.setPreserveEGLContextOnPause(true);
            GLRenderer = new GLRenderer(getContext());
            glSurfaceView.setRenderer(GLRenderer);
            isRendererSetUp = true;
        }else{
            Toast.makeText(getContext(), "Current device not support OpenGL ES 3.0!", Toast.LENGTH_SHORT).show();
            return;
        }
        addView(glSurfaceView);

        mScaleGestureDetector = new ScaleGestureDetector(getContext(), new CustomScaleGestureListener());
        mGestureDetector = new GestureDetector(getContext(), new CustomGestureListener());
        mCameraDragMode = CameraDragMode.DRAG;
        mCameraControlMode = CameraControlMode.ROTATE;

        setOnTouchListener(this);

        glCommunicator = GLCommunicator.getInstance();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (!mIsSidePanelOpen) {
            switch (event.getActionMasked()) {
                case ACTION_DOWN:
                    mCameraDragMode = CameraDragMode.DRAG;
                    break;
                case ACTION_MOVE:
                    break;
                case ACTION_POINTER_UP:
                case ACTION_UP:
                    mCameraDragMode = CameraDragMode.NONE;
                    break;
            }
            mScaleGestureDetector.onTouchEvent(event);
            mGestureDetector.onTouchEvent(event);
        }
        return true;
    }

    private class CustomScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        float tempScaleFactor;
        @Override
        public boolean onScale(final ScaleGestureDetector detector) {
            if (mainCamera == null){
                mainCamera = (OrbitCamera) glCommunicator.getData("MainCamera");
            }
            mCameraDragMode = CameraDragMode.ZOOM;
            if (mCameraDragMode == CameraDragMode.ZOOM){
                tempScaleFactor = detector.getScaleFactor();
                mainCamera.handleZoom(tempScaleFactor);
            }
            return true;
        }
    }

    private OrbitCamera mainCamera;
    private OrbitCamera axisCamera;
    private class CustomGestureListener extends GestureDetector.SimpleOnGestureListener {

        float previousX, previousY;

        @Override
        public boolean onDown(MotionEvent e) {
            previousX = e.getX(0);
            previousY = e.getY(0);
            return super.onDown(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (mainCamera == null){
                mainCamera = (OrbitCamera) glCommunicator.getData("MainCamera");
            }
            if (axisCamera == null){
                axisCamera = (OrbitCamera) glCommunicator.getData("AxisCamera");
            }
            if (mCameraDragMode == CameraDragMode.DRAG) {
                if (e2.getPointerCount() > 1) {

                } else {
                    final float deltaX = e2.getX() - previousX;
                    final float deltaY = e2.getY() - previousY;

                    previousX = e2.getX();
                    previousY = e2.getY();

                    if (mCameraControlMode.equals(CameraControlMode.ROTATE)){
                        mainCamera.handleRotate(-deltaX, deltaY);
                        axisCamera.handleRotate(-deltaX, deltaY);
                    }else {
                        mainCamera.handleDragHorizontal(deltaX);
                        mainCamera.handleDragVertical(deltaY);
                    }
                }

                }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

    public CameraDragMode getCameraDragMode() {
        return mCameraDragMode;
    }

    public void setCameraDragMode(CameraDragMode mCameraDragMode) {
        this.mCameraDragMode = mCameraDragMode;
    }

    public CameraControlMode getCameraControlMode() {
        return mCameraControlMode;
    }

    public void setCameraControlMode(CameraControlMode mCameraControlMode) {
        this.mCameraControlMode = mCameraControlMode;
    }

    public GLSurfaceView getGlSurfaceView() {
        return glSurfaceView;
    }

    public GLRenderer getGLRenderer() {
        return GLRenderer;
    }

    public boolean isRendererSetUp() {
        return isRendererSetUp;
    }

    public void setSidePanelOpen(boolean isSidePanelOpen){
        this.mIsSidePanelOpen = isSidePanelOpen;
    }
}
