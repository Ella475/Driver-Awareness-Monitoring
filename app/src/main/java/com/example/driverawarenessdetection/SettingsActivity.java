package com.example.driverawarenessdetection;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.driverawarenessdetection.settings.SettingsFragment;
import com.example.driverawarenessdetection.video_processing.awareness_detection.alerts.CommandManager;
import com.example.driverawarenessdetection.video_processing.awareness_detection.alerts.DrivingAlertTiming;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        findViewById(R.id.settings).getRootView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Objects.requireNonNull(getSupportActionBar()).hide();

        if (findViewById(R.id.idFrameLayout) != null) {
            if (savedInstanceState != null) {
                return;
            }
            getFragmentManager().beginTransaction().add(R.id.idFrameLayout, new SettingsFragment()).commit();
        }

        ImageView backButton = findViewById(R.id.back);
        backButton.setOnClickListener(view -> finishActivity());
    }

    private void setSharedPreferences() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean asleepAlert = prefs.getBoolean("asleep_alert", true);
        boolean distractedAlert = prefs.getBoolean("distracted_alert", true);
        String drivingAlertStr = prefs.getString("break_alert", "1");

        DrivingAlertTiming t;
        switch (drivingAlertStr) {
            case "0":
                t = DrivingAlertTiming.NEVER;
                break;
            case "1":
                t = DrivingAlertTiming.ONE_HOUR;
                break;
            case "2":
                t = DrivingAlertTiming.TWO_HOURS;
                break;
            default:
                t = DrivingAlertTiming.THREE_HOURS;
        }
        CommandManager.alertsData.setSleepAlert(asleepAlert);
        CommandManager.alertsData.setAttentionAlert(distractedAlert);
        CommandManager.alertsData.setTimeData(t);
    }

    public void finishActivity() {
        setSharedPreferences();
        finish();
    }

    public void onBackPressed() {
        finishActivity();
    }
}
