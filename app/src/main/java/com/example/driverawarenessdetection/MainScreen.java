package com.example.driverawarenessdetection;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleObserver;

import com.example.driverawarenessdetection.login.data.LoginDataSource;
import com.example.driverawarenessdetection.login.data.LoginRepository;
import com.example.driverawarenessdetection.login.ui.LoginType;
import com.ncorti.slidetoact.SlideToActView;

import java.util.Objects;

public class MainScreen extends AppCompatActivity implements LifecycleObserver {
    private SlideToActView slide;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_screen);
        findViewById(R.id.main_id).getRootView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Objects.requireNonNull(getSupportActionBar()).hide();

        initView();
    }

    private void initView() {
        ImageView cameraButton = findViewById(R.id.camera);
        cameraButton.setOnClickListener(view -> {
            Intent switchCameraActivityIntent = new Intent(MainScreen.this,
                    CameraActivity.class);
            startActivity(switchCameraActivityIntent);
        });

        ImageView statisticsButton = findViewById(R.id.statistics);
        statisticsButton.setOnClickListener(view -> {
            Intent switchStatisticsActivityIntent = new Intent(MainScreen.this,
                    StatisticsActivity.class);
            startActivity(switchStatisticsActivityIntent);
        });

        ImageView settingsButton = findViewById(R.id.settings);
        settingsButton.setOnClickListener(view -> {
            Intent switchSettingsActivityIntent = new Intent(MainScreen.this,
                    SettingsActivity.class);
            startActivity(switchSettingsActivityIntent);
        });


        slide = findViewById(R.id.slider);
        slide.setOnSlideCompleteListener(view -> {
            Intent switchDetectionActivityIntent = new Intent(MainScreen.this,
                    AwarenessDetectionActivity.class);
            startActivity(switchDetectionActivityIntent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        slide.resetSlider();
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

}
