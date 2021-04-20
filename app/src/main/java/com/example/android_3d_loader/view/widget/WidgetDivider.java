package com.example.android_3d_loader.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.android_3d_loader.R;

public class WidgetDivider extends FrameLayout {

    public WidgetDivider(@NonNull Context context) {
        super(context);
        init();
    }

    public WidgetDivider(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.widget_divider, this);
    }
}
