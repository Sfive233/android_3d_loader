package com.example.android_3d_loader.view.widget.popupNotice;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

public class PopupErrorNotice extends WidgetPopupNotice{
    public PopupErrorNotice(Context context) {
        super(context);
        init();
    }

    public PopupErrorNotice(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Drawable backgroundDrawable = mIcon.getBackground();
            backgroundDrawable.setTintList(ColorStateList.valueOf(Color.parseColor("#F44336")));
            mIcon.setBackground(backgroundDrawable);
        }
        mNotice.setTextColor(Color.parseColor("#F44336"));
    }
}
