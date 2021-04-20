package com.example.android_3d_loader.view.widget.notice;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.android_3d_loader.R;

public class WidgetNotice extends LinearLayout {

    protected ImageView mIcon;
    protected TextView mMsg;

    public WidgetNotice(Context context) {
        super(context);
        init();
    }

    public WidgetNotice(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.widget_notice, this);
        mIcon = findViewById(R.id.notice_icon);
        mMsg = findViewById(R.id.notice_msg);
    }

    public void setMsg(String msg){
        mMsg.setText(msg);
    }
}
