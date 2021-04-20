package com.example.android_3d_loader.view.widget.popupNotice;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

public class PopupInfoNotice extends WidgetPopupNotice{
    public PopupInfoNotice(Context context) {
        super(context);
        init();
    }

    public PopupInfoNotice(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Drawable backgroundDrawable = mIcon.getBackground();
            backgroundDrawable.setTintList(ColorStateList.valueOf(Color.parseColor("#ffffff")));
            mIcon.setBackground(backgroundDrawable);
            mNotice.setTextColor(ColorStateList.valueOf(Color.parseColor("#9c9c9c")));
            mIcon.setImageTintList(ColorStateList.valueOf(Color.parseColor("#9c9c9c")));
        }
    }
}
