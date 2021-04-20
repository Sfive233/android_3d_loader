package com.example.android_3d_loader.view.widget.popupNotice;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.example.android_3d_loader.R;

public class PopupWarningNotice extends WidgetPopupNotice{
    public PopupWarningNotice(Context context) {
        super(context);
        init();
    }

    public PopupWarningNotice(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Drawable triangle = getResources().getDrawable(R.drawable.shape_triangle);
            triangle.setTintList(ColorStateList.valueOf(Color.parseColor("#ff9800")));
            mIcon.setBackground(triangle);
            mIcon.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffffff")));
        }
        mNotice.setTextColor(Color.parseColor("#ff9800"));
    }
}
