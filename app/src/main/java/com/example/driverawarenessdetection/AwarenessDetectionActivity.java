package com.example.driverawarenessdetection;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;


public class AwarenessDetectionActivity extends AppCompatActivity {
    int awarenessPercentage = 0;
    CircularProgressBar circularProgressBar;
    TextView awarenessPercentageText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_awareness_detection);
        this.circularProgressBar = findViewById(R.id.awarenessCircularProgressbar);
        this.awarenessPercentageText = findViewById(R.id.awarenessPercentage);
        this.onPercentageChanged();
    }

    private void onPercentageChanged() {
        this.circularProgressBar.setProgressWithAnimation((float) this.awarenessPercentage, 10000L);
        this.awarenessPercentageText.setText(this.awarenessPercentage + "%");
    }

}
