package com.example.driverawarenessdetection;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.driverawarenessdetection.video_processing.awareness_detection.AwarenessDetectorProcessor;
import com.example.driverawarenessdetection.video_processing.awareness_detection.utils.CameraSourceWrapper;
import com.example.driverawarenessdetection.video_processing.awareness_detection.utils.MsgReader;
import com.example.driverawarenessdetection.video_processing.camera.CameraSource;
import com.example.driverawarenessdetection.video_processing.camera.CameraSourcePreview;
import com.example.driverawarenessdetection.video_processing.camera.GraphicOverlay;

import java.io.IOException;
import java.util.Objects;


public class CameraActivity extends AppCompatActivity {

    private MsgReader reader;
    private CameraSourceWrapper csw;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        CameraSourcePreview preview = findViewById(R.id.camera_source_preview);
        GraphicOverlay graphicOverlay = findViewById(R.id.graphic_overlay);

        csw = new CameraSourceWrapper(preview, graphicOverlay, this);
        csw.start();
        initCalibration();
    }

    public void initCalibration() {
        Button calibrationBtn = findViewById(R.id.calibrationBtn);
        reader = new MsgReader(CameraActivity.this);
        calibrationBtn.setOnClickListener(v -> {
            reader.speak("Starting calibration!");
            reader.speak("Please look at the road for a few seconds!");
            reader.speak("5, 4, 3, 2, 1");
            Objects.requireNonNull(csw.awarenessProcessor.onSuccessDetector.awarenessHashMap.get(0)).onCalibration();
            reader.speak("Calibration finished!");
        });
    }

    public void finishActivity() {
        reader.stop();
        csw.stop();
        finish();
    }

    public void onBackPressed() {
        finishActivity();
    }
}
