package com.example.android_3d_loader.view.widget.popupNotice;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.android_3d_loader.R;

public class WidgetPopupNotice extends LinearLayout {

    protected ImageView mIcon;
    protected TextView mNotice;

    public WidgetPopupNotice(Context context) {
        super(context);
        init();
    }

    public WidgetPopupNotice(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.widget_pupup_notice, this);
        mIcon = findViewById(R.id.popup_notice_icon);
        mNotice = findViewById(R.id.popup_notice_text);
    }

    public void setMsg(String msg){
        mNotice.setText(msg);
    }
}
