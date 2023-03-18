package com.example.driverawarenessdetection;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.example.driverawarenessdetection.utils.BaseActivity;

public class MainActivity extends BaseActivity {
    final int CAMERA_REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        grantCameraAndStoragePermission();

        Intent switchWelcomeScreenIntent = new Intent(MainActivity.this,
                WelcomeScreenActivity.class);
        startActivity(switchWelcomeScreenIntent);
    }

    public void grantCameraAndStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onBackPressed() {
    }

}
