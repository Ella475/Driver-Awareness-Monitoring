package com.example.driverawarenessdetection;

import android.os.Bundle;
import android.widget.ImageView;
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
        initView();
    }

    private void onPercentageChanged() {
        this.circularProgressBar.setProgressWithAnimation((float) this.awarenessPercentage, 10000L);
        this.awarenessPercentageText.setText(this.awarenessPercentage + "%");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {
        this.circularProgressBar = findViewById(R.id.awarenessCircularProgressbar);
        this.awarenessPercentageText = findViewById(R.id.awarenessPercentage);
        this.onPercentageChanged();

        ImageView backButton = findViewById(R.id.back);
        backButton.setOnClickListener(view -> {
            finish();
        });
    }

}
