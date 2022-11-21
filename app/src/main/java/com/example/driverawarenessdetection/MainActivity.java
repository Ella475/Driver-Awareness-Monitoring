package com.example.driverawarenessdetection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ebanx.swipebtn.OnStateChangeListener;
import com.example.driverawarenessdetection.ui.login.LoginActivity;
import com.ncorti.slidetoact.SlideToActView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent switchLoginActivityIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(switchLoginActivityIntent);


        setContentView(R.layout.activity_main);


        SlideToActView slide = findViewById(R.id.slider);
        slide.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(@NonNull SlideToActView view) {
                Intent switchDetectionActivityIntent = new Intent(MainActivity.this,
                        AwarenessDetectionActivity.class);
                startActivity(switchDetectionActivityIntent);
            }
        });

    }
}
