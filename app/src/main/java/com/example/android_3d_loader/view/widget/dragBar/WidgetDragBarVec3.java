package com.example.android_3d_loader.view.widget.dragBar;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.android_3d_loader.R;
import com.example.android_3d_loader.view.util.DensityUtil;
import com.example.android_3d_loader.view.property.PropertyDefinition;
import com.example.android_3d_loader.view.property.Range;

public class WidgetDragBarVec3 extends LinearLayout {

    private final TextView propertyName;
    private final SeekBar mSeekBarX;
    private final TextView valX;
    private final SeekBar mSeekBarY;
    private final TextView valY;
    private final SeekBar mSeekBarZ;
    private final TextView valZ;
    private final PropertyDefinition mPropertyDefinition;

    public WidgetDragBarVec3(Context context, final PropertyDefinition propertyDefinition) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.widget_drag_bar_vec3, this);

        propertyName = findViewById(R.id.drag_bar_Vec3_name);

        mSeekBarX = findViewById(R.id.drag_bar_vec3_x);
        valX = findViewById(R.id.drag_bar_vec3_val_x);
        mSeekBarY = findViewById(R.id.drag_bar_vec3_y);
        valY = findViewById(R.id.drag_bar_vec3_val_y);
        mSeekBarZ = findViewById(R.id.drag_bar_vec3_z);
        valZ = findViewById(R.id.drag_bar_vec3_val_z);

        mPropertyDefinition = propertyDefinition;

        init();
    }

    private void init(){
        propertyName.setText(mPropertyDefinition.getName());
        final Range range = mPropertyDefinition.getRange();
        int max = (int)((range.getMaxFloatVal() - range.getMinFloatVal()) / range.getFloatStep());
        mSeekBarX.setMax(max);
        mSeekBarX.setProgress((int)(mPropertyDefinition.getBindVector3().x.getVal() / range.getFloatStep()) - (int)range.getMinFloatVal());
        mSeekBarX.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float finalVal = progress * range.getFloatStep() + range.getMinFloatVal();
                mPropertyDefinition.setBindVector3X(finalVal);
                int width = mSeekBarX.getWidth();
                int seekBarWidth = width - mSeekBarX.getPaddingStart() - mSeekBarX.getPaddingEnd();
                float widthPerProgress = (float)seekBarWidth / (float)mSeekBarX.getMax();
                int xOffset = (int)(widthPerProgress * progress);
                valX.setText(finalVal + "");
                valX.setX(propertyName.getWidth() + xOffset - DensityUtil.dp2px(getContext(), 20));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                valX.setVisibility(VISIBLE);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                valX.setVisibility(INVISIBLE);
            }
        });
        mSeekBarY.setMax(max);
        mSeekBarY.setProgress((int)(mPropertyDefinition.getBindVector3().y.getVal() / range.getFloatStep()) - (int)range.getMinFloatVal());
        mSeekBarY.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float finalVal = progress * range.getFloatStep() + range.getMinFloatVal();
                mPropertyDefinition.setBindVector3Y(finalVal);
                int width = mSeekBarY.getWidth();
                int seekBarWidth = width - mSeekBarY.getPaddingStart() - mSeekBarY.getPaddingEnd();
                float widthPerProgress = (float)seekBarWidth / (float)mSeekBarY.getMax();
                int xOffset = (int)(widthPerProgress * progress);
                valY.setText(finalVal + "");
                valY.setX(propertyName.getWidth() + xOffset - DensityUtil.dp2px(getContext(), 20));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                valY.setVisibility(VISIBLE);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                valY.setVisibility(INVISIBLE);
            }
        });
        mSeekBarZ.setMax(max);
        mSeekBarZ.setProgress((int)(mPropertyDefinition.getBindVector3().z.getVal() / range.getFloatStep()) - (int)range.getMinFloatVal());
        mSeekBarZ.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float finalVal = progress * range.getFloatStep() + range.getMinFloatVal();
                mPropertyDefinition.setBindVector3Z(finalVal);
                int width = mSeekBarZ.getWidth();
                int seekBarWidth = width - mSeekBarZ.getPaddingStart() - mSeekBarZ.getPaddingEnd();
                float widthPerProgress = (float)seekBarWidth / (float)mSeekBarZ.getMax();
                int xOffset = (int)(widthPerProgress * progress);
                valZ.setText(finalVal + "");
                valZ.setX(propertyName.getWidth() + xOffset - DensityUtil.dp2px(getContext(), 20));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                valZ.setVisibility(VISIBLE);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                valZ.setVisibility(INVISIBLE);
            }
        });
    }
}
