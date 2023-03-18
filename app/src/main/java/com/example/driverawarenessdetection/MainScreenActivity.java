package com.example.driverawarenessdetection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.lifecycle.LifecycleObserver;

import com.example.driverawarenessdetection.utils.BaseActivity;
import com.example.driverawarenessdetection.utils.ConfirmationDialog;
import com.ncorti.slidetoact.SlideToActView;

import java.util.Objects;

public class MainScreenActivity extends BaseActivity implements LifecycleObserver {
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
            Intent switchCameraActivityIntent = new Intent(MainScreenActivity.this,
                    CameraActivity.class);
            startActivity(switchCameraActivityIntent);
        });

        ImageView statisticsButton = findViewById(R.id.statistics);
        statisticsButton.setOnClickListener(view -> {
            Intent switchStatisticsActivityIntent = new Intent(MainScreenActivity.this,
                    StatisticsActivity.class);
            startActivity(switchStatisticsActivityIntent);
        });

        ImageView settingsButton = findViewById(R.id.settings);
        settingsButton.setOnClickListener(view -> {
            Intent switchSettingsActivityIntent = new Intent(MainScreenActivity.this,
                    SettingsActivity.class);
            startActivity(switchSettingsActivityIntent);
        });


        slide = findViewById(R.id.slider);
        slide.setOnSlideCompleteListener(view -> {
            Intent switchDetectionActivityIntent = new Intent(MainScreenActivity.this,
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
        ConfirmationDialog dialog = new ConfirmationDialog();
        dialog.setMessage("Are you sure you want to exit?");
        dialog.setListener(this::finishAffinity);
        dialog.show(getSupportFragmentManager(), "ConfirmationDialog");
    }

}
