package com.example.android_3d_loader.view.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.android_3d_loader.R;
import com.example.android_3d_loader.view.property.PropertyDefinition;

public class WidgetDescription extends ConstraintLayout {

    private static final String TAG = "WidgetDescription";
    private final TextView mName;
    private final TextView mContent;
    private final PropertyDefinition mPropertyDefinition;

    public WidgetDescription(@NonNull Context context, PropertyDefinition propertyDefinition) {
        super(context);

        LayoutInflater.from(getContext()).inflate(R.layout.widget_description, this);
        mPropertyDefinition = propertyDefinition;
        mName = findViewById(R.id.description_name);
        mContent = findViewById(R.id.description_content);

        init();
    }

    private void init(){
        mName.setText(mPropertyDefinition.getName());
        mContent.setText(mPropertyDefinition.getDescription());
    }
}
