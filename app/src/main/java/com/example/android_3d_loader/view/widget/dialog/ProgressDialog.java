package com.example.android_3d_loader.view.widget.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.android_3d_loader.R;

public class ProgressDialog extends DialogFragment {

//    private TextView mTextView;
    private static ProgressDialog sInstance;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFullScreen);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.widget_progress_dialog, container);
//        mTextView = view.findViewById(R.id.progress_detail);
        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    public static ProgressDialog getInstance(){
        if (sInstance == null){
            sInstance = new ProgressDialog();
        }
        return sInstance;
    }

    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }
}
