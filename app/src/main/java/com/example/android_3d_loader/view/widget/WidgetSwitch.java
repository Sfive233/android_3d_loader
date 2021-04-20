package com.example.android_3d_loader.view.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.android_3d_loader.R;
import com.example.android_3d_loader.view.property.PropertyDefinition;

public class WidgetSwitch extends LinearLayout {

    private final TextView mSwitchName;
    private final Switch mSwitchVal;
    private final PropertyDefinition mPropertyDefinition;

    public WidgetSwitch(Context context, final PropertyDefinition propertyDefinition) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.widget_switch, this);

        mSwitchName = findViewById(R.id.switch_name);
        mSwitchName.setText(propertyDefinition.getName());
        mPropertyDefinition = propertyDefinition;
        mSwitchVal = findViewById(R.id.switch_val);

        init();
    }

    private void init(){
        mSwitchVal.setChecked(mPropertyDefinition.getBindBooleanVal().getVal());
        if (null != mPropertyDefinition.getOnSwitchChangeListener()){
            mSwitchVal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mPropertyDefinition.getBindBooleanVal().setVal(isChecked);
                    mPropertyDefinition.getOnSwitchChangeListener().onSwitchChanged(isChecked);
                }
            });
        }else {
            mSwitchVal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mPropertyDefinition.setBindBooleanVal(isChecked);
                }
            });
        }
    }

    public interface OnSwitchChangeListener{
        void onSwitchChanged(boolean val);
    }
}
