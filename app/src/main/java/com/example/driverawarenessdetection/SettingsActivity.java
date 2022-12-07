package com.example.driverawarenessdetection;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.driverawarenessdetection.settings.SettingsFragment;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setTitle("Settings");

        if (findViewById(R.id.idFrameLayout) != null) {
            if (savedInstanceState != null) {
                return;
            }
            getFragmentManager().beginTransaction().add(R.id.idFrameLayout, new SettingsFragment()).commit();
        }

        ImageView backButton = findViewById(R.id.back);
        backButton.setOnClickListener(view -> {
            finish();
        });
    }

}
