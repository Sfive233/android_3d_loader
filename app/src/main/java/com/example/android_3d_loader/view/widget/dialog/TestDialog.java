package com.example.android_3d_loader.view.widget.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.android_3d_loader.controller.communicator.GLCommunicator;

public class TestDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        GLCommunicator communicator = GLCommunicator.getInstance();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("System Info")
                .setMessage(
                        communicator.getGLSurfaceViewWidth() + "x" + communicator.getGLSurfaceViewHeight()+"\n"+
                                communicator.getRenderHardwareName()+"\n"+
                                communicator.getRenderHardwareVendor()+"\n"+
                                communicator.getOpenglVersion()+"\n"+
                                communicator.getGLSLVersion()
                );
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
}
