package com.example.android_3d_loader.view.widget.notice;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

public class ErrorNotice extends WidgetNotice{

    public ErrorNotice(Context context) {
        super(context);
        init();
    }

    public ErrorNotice(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Drawable backgroundDrawable = mIcon.getBackground();
            backgroundDrawable.setTintList(ColorStateList.valueOf(Color.parseColor("#F44336")));
            mIcon.setBackground(backgroundDrawable);
        }
        mMsg.setTextColor(Color.parseColor("#F44336"));
    }
}
