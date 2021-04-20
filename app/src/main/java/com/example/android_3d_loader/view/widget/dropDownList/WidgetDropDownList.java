package com.example.android_3d_loader.view.widget.dropDownList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.android_3d_loader.R;
import com.example.android_3d_loader.controller.communicator.GLCommunicator;
import com.example.android_3d_loader.view.property.PropertyDefinition;

public class WidgetDropDownList extends ConstraintLayout {

    private static final String TAG = "WidgetDropDownList";
    private final TextView mName;
    private final Spinner mList;
    private final PropertyDefinition propertyDefinition;

    public WidgetDropDownList(Context context, final PropertyDefinition propertyDefinition) {
        super(context);
        LayoutInflater.from(getContext()).inflate(R.layout.widget_drop_down_list, this);
        mName = findViewById(R.id.drop_down_list_name);
        mList = findViewById(R.id.drop_down_list);
        this.propertyDefinition = propertyDefinition;

        init();
    }

    private void init(){
        // name
        mName.setText(propertyDefinition.getName());

        // items
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_spinner_item, propertyDefinition.getObjectList()
        );
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mList.setAdapter(arrayAdapter);
        mList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            boolean isFirst = true;
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!isFirst) {
                    propertyDefinition.getOnItemSelectedListener().OnItemSelected(parent, view, position, id);
                }else {
                    isFirst = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mList.setSelection(propertyDefinition.getDefaultItemPosition());
    }

    public interface OnItemSelectedListener{
        void OnItemSelected(AdapterView<?> parent, View view, int position, long id);
    }
}
