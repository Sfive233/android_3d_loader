package com.example.android_3d_loader.view.widget.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.android_3d_loader.R;

public class AlertDialog extends DialogFragment {

    private TextView mAlertDetail;
    private Button mOk;
    private Button mCancel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.widget_alert_dialog, container);
        mAlertDetail = view.findViewById(R.id.alert_detail);
        mOk = view.findViewById(R.id.alert_ok);
        mCancel = view.findViewById(R.id.alert_cancel);
        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
}
