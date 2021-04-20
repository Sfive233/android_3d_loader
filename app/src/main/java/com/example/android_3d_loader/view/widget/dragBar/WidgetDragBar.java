package com.example.android_3d_loader.view.widget.dragBar;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.android_3d_loader.R;
import com.example.android_3d_loader.view.util.DensityUtil;
import com.example.android_3d_loader.view.property.PropertyDefinition;
import com.example.android_3d_loader.view.property.Range;

public class WidgetDragBar extends FrameLayout {

    private static final String TAG = "WidgetDragBar";
    private final TextView mTextView;
    private final SeekBar mSeekBar;
    private final TextView dragBarVal;
    private final PropertyDefinition propertyDefinition;

    public WidgetDragBar(Context context, final PropertyDefinition propertyDefinition) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.widget_drag_bar, this);
        FrameLayout mRoot = findViewById(R.id.root);

        mTextView = mRoot.findViewById(R.id.drag_bar_name);
        mSeekBar = mRoot.findViewById(R.id.drag_bar_seek_bar);
        dragBarVal = findViewById(R.id.drag_bar_val);

        this.propertyDefinition = propertyDefinition;

        init();
    }

    private void init(){
        mTextView.setText(propertyDefinition.getName());
        final Range range = propertyDefinition.getRange();

        switch (propertyDefinition.getDataType()){
            case Int:
                mSeekBar.setMax((range.getMaxIntVal() - range.getMinIntVal()) / range.getIntStep());
                mSeekBar.setProgress((propertyDefinition.getBindIntVal().getVal() / range.getIntStep()) - (range.getMinIntVal() / range.getIntStep()));
                mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        int finalVal = progress * range.getIntStep() + range.getMinIntVal();
                        propertyDefinition.setBindIntVal(finalVal);
                        int width = mSeekBar.getWidth();
                        int seekBarWidth = width - mSeekBar.getPaddingStart() - mSeekBar.getPaddingEnd();
                        float widthPerProgress = (float)seekBarWidth / (float)mSeekBar.getMax();
                        int xOffset = (int)(widthPerProgress * progress);
                        dragBarVal.setText(finalVal+"");
                        dragBarVal.setX(mTextView.getWidth() + xOffset - DensityUtil.dp2px(getContext(), 20));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        dragBarVal.setVisibility(VISIBLE);
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        dragBarVal.setVisibility(INVISIBLE);
                    }
                });
                break;
            case Float:
                mSeekBar.setMax(
                        (int)((range.getMaxFloatVal() - range.getMinFloatVal()) / range.getFloatStep())
                );
                mSeekBar.setProgress(
                        (int)(propertyDefinition.getBindFloatVal().getVal() / range.getFloatStep()) - (int)(range.getMinFloatVal() / range.getFloatStep())
                );
                mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        float finalVal = progress * range.getFloatStep() + range.getMinFloatVal();
                        propertyDefinition.setBindFloatVal(finalVal);
                        int width = mSeekBar.getWidth();
                        int seekBarWidth = width - mSeekBar.getPaddingStart() - mSeekBar.getPaddingEnd();
                        float widthPerProgress = (float)seekBarWidth / (float)mSeekBar.getMax();
                        int xOffset = (int)(widthPerProgress * progress);
                        dragBarVal.setText(finalVal + "");
                        dragBarVal.setX(mTextView.getWidth() + xOffset - DensityUtil.dp2px(getContext(), 20));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        dragBarVal.setVisibility(VISIBLE);
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        dragBarVal.setVisibility(INVISIBLE);
                    }
                });
                break;
        }
    }
}
