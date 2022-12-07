package com.example.driverawarenessdetection;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

import com.example.driverawarenessdetection.video_processing.awareness_detection.AwarenessManager;
import com.example.driverawarenessdetection.video_processing.awareness_detection.utils.CameraSourceWrapper;
import com.example.driverawarenessdetection.video_processing.camera.CameraSourcePreview;
import com.example.driverawarenessdetection.video_processing.camera.GraphicOverlay;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.HashMap;
import java.util.Objects;


public class AwarenessDetectionActivity extends AppCompatActivity {
    private int awarenessPercentage = 100;
    private CircularProgressBar circularProgressBar;
    private TextView awarenessPercentageText;
    private CameraSourceWrapper csw;
    private final Handler handler = new Handler();
    private final int updateEvery = 5000; // 1000 milliseconds == 1 second
    private final long animationDuration = 3000;
    private int percentageCutOff = 20;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_awareness_detection);

        CameraSourcePreview preview = findViewById(R.id.camera_source_preview);
        GraphicOverlay graphicOverlay = findViewById(R.id.graphic_overlay);

        csw = new CameraSourceWrapper(preview, graphicOverlay, this);
        csw.start();

        initView();
    }

    @SuppressLint("SetTextI18n")
    private void onPercentageChanged() {
        HashMap<Integer, AwarenessManager> managerHashMap = csw.awarenessProcessor.onSuccessDetector.awarenessHashMap;
        if (!managerHashMap.isEmpty())
            awarenessPercentage = (int) (100 * Objects.requireNonNull(managerHashMap.get(0)).getAwareProbability());
        if (awarenessPercentage < percentageCutOff) {
            this.circularProgressBar.setBackgroundProgressBarColor(getColor(0xFF, 0xFF, 0x00, 0x00));
        }
        else {
            this.circularProgressBar.setBackgroundProgressBarColor(getColor(0xFF, 0xA4, 0xAF, 0xF4));
        }

        this.circularProgressBar.setProgressWithAnimation((float) awarenessPercentage, animationDuration);
        this.awarenessPercentageText.setText(awarenessPercentage + "%");
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
            finishActivity();
        });

        handler.postDelayed(new Runnable() {
            public void run() {
                onPercentageChanged();
                handler.postDelayed(this, updateEvery);
            }
        }, updateEvery);
    }

    public void finishActivity() {
        csw.stop();
        finish();
    }

    public void onBackPressed() {
        finishActivity();
    }

    @ColorInt
    private int getColor(int A, int R, int G, int B) {
        return  (A & 0xff) << 24 | (R & 0xff) << 16 | (G & 0xff) << 8 | (B & 0xff);
    }
}
