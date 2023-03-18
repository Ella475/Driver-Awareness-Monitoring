package com.example.driverawarenessdetection.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class ConfirmationDialog extends DialogFragment implements IConfirmationDialog {

    private String message;
    private ConfirmationDialogListener listener;

    public void setMessage(String message) {
        this.message = message;
    }

    public void setListener(ConfirmationDialogListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message)
                .setPositiveButton("Confirm", (dialogInterface, i) -> {
                    if (listener != null) {
                        listener.onConfirm();
                    }
                    dismiss();
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> dismiss());
        return builder.create();
    }
}

