package com.example.android_3d_loader.view.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android_3d_loader.R;
import com.example.android_3d_loader.view.property.PropertyDefinition;

public class WidgetButton extends LinearLayout {

    private final RelativeLayout mButton;
    private final TextView mText;
    private final PropertyDefinition mPropertyDefinition;

    public WidgetButton(Context context, PropertyDefinition propertyDefinition) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.widget_button, this);
        mButton = findViewById(R.id.button_button);
        mText = findViewById(R.id.button_text);
        mPropertyDefinition = propertyDefinition;

        init();
    }

    private void init(){
        mText.setText(mPropertyDefinition.getHint());
        mButton.setOnClickListener(mPropertyDefinition.getOnClickListener());
    }
}
