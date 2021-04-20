package com.example.android_3d_loader.view.widget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.android_3d_loader.R;

public class WidgetDialogInfo_ extends DialogFragment {

    private static WidgetDialogInfo_ widgetDialogInfo;

    public WidgetDialogInfo_(){

    }

    public static WidgetDialogInfo_ getInstance(String title, String message){
        if (widgetDialogInfo == null){
            widgetDialogInfo = new WidgetDialogInfo_();
        }
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("message", message);
        widgetDialogInfo.setArguments(bundle);
        return widgetDialogInfo;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.widget_dialog_info, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        String message = getArguments().getString("message");
        TextView titleView = view.findViewById(R.id.dialog_title);
        TextView messageView = view.findViewById(R.id.dialog_message);
        titleView.setText(title);
        messageView.setText(message);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
