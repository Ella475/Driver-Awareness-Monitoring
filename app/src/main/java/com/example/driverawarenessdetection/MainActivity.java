package com.example.driverawarenessdetection;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleObserver;

import com.example.driverawarenessdetection.login.ui.LoginActivity;
import com.ncorti.slidetoact.SlideToActView;

public class MainActivity extends AppCompatActivity implements LifecycleObserver {
    final int CAMERA_REQUEST_CODE = 1001;
    private SlideToActView slide;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Intent switchLoginActivityIntent = new Intent(MainActivity.this, LoginActivity.class);
//        startActivity(switchLoginActivityIntent);

        setContentView(R.layout.activity_main);

        grantCameraAndStoragePermission();
        initView();
    }

    private void initView() {
        ImageView cameraButton = findViewById(R.id.camera);
        cameraButton.setOnClickListener(view -> {
            Intent switchCameraActivityIntent = new Intent(MainActivity.this,
                    CameraActivity.class);
            startActivity(switchCameraActivityIntent);
        });

        ImageView settingsButton = findViewById(R.id.settings);
        settingsButton.setOnClickListener(view -> {
            Intent switchSettingsActivityIntent = new Intent(MainActivity.this,
                    SettingsActivity.class);
            startActivity(switchSettingsActivityIntent);
        });


        slide = findViewById(R.id.slider);
        slide.setOnSlideCompleteListener(view -> {
            Intent switchDetectionActivityIntent = new Intent(MainActivity.this,
                    AwarenessDetectionActivity.class);
            startActivity(switchDetectionActivityIntent);
        });
    }

    public void grantCameraAndStoragePermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        slide.resetSlider();
    }
}
