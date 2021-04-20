package com.example.android_3d_loader.view.widget.notice;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

public class InfoNotice extends WidgetNotice{

    public InfoNotice(Context context) {
        super(context);
        init();
    }

    public InfoNotice(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Drawable backgroundDrawable = mIcon.getBackground();
            backgroundDrawable.setTintList(ColorStateList.valueOf(Color.parseColor("#ffffff")));
            mIcon.setBackground(backgroundDrawable);
            mMsg.setTextColor(ColorStateList.valueOf(Color.parseColor("#9c9c9c")));
            mIcon.setImageTintList(ColorStateList.valueOf(Color.parseColor("#9c9c9c")));
        }
    }
}
