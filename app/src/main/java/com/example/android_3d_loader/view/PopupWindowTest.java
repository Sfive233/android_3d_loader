package com.example.android_3d_loader.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.android_3d_loader.R;

public class PopupWindowTest extends PopupWindow {

    private Context mContext;
    private TextView mPopupText;

    public PopupWindowTest(Context context){
        super(context);
        View mRoot = LayoutInflater.from(context).inflate(R.layout.popup_window_test, null, false);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(mRoot);

        mPopupText = mRoot.findViewById(R.id.popup_text);
        mPopupText.setText("0");
    }
}
