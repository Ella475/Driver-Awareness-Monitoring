package com.example.driverawarenessdetection;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.driverawarenessdetection.video_processing.awareness_detection.utils.CameraSourceWrapper;
import com.example.driverawarenessdetection.video_processing.camera.CameraSourcePreview;
import com.example.driverawarenessdetection.video_processing.camera.GraphicOverlay;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.Objects;


public class AwarenessDetectionActivity extends AppCompatActivity {
    private int awarenessPercentage = 0;
    private CircularProgressBar circularProgressBar;
    private TextView awarenessPercentageText;
    private CameraSourceWrapper csw;
    private final Handler handler = new Handler();
    private final int update_every = 1000; // 1000 milliseconds == 1 second

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
//        awarenessPercentage = (int) (100 * Objects.requireNonNull(csw.awarenessProcessor.
//                onSuccessDetector.awarenessHashMap.get(0)).getAwareProbability());
        awarenessPercentage += 10;
        this.circularProgressBar.setProgressWithAnimation((float) awarenessPercentage, 300L);
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
                handler.postDelayed(this, update_every);
            }
        }, update_every);
    }

    public void finishActivity() {
//        csw.stop();
        finish();
    }

    public void onBackPressed() {
        finishActivity();
    }

}
