package com.example.driverawarenessdetection.utils;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;

public interface IConfirmationDialog {
    @NonNull
    Dialog onCreateDialog(Bundle savedInstanceState);
}
