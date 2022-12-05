package com.example.driverawarenessdetection;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.driverawarenessdetection.video_processing.awareness_detection.AwarenessDetectorProcessor;
import com.example.driverawarenessdetection.video_processing.awareness_detection.utils.MsgReader;
import com.example.driverawarenessdetection.video_processing.camera.CameraSource;
import com.example.driverawarenessdetection.video_processing.camera.CameraSourcePreview;
import com.example.driverawarenessdetection.video_processing.camera.GraphicOverlay;

import java.io.IOException;


public class CameraActivity extends AppCompatActivity {

    private CameraSourcePreview preview;
    private GraphicOverlay graphicOverlay;
    private AwarenessDetectorProcessor awarenessProcessor;
    protected CameraSource cameraSource;
    public MsgReader reader;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        preview = findViewById(R.id.camera_source_preview);
        graphicOverlay = findViewById(R.id.graphic_overlay);

        awarenessProcessor = new AwarenessDetectorProcessor(this);
        initCalibration();
        startCamera();
    }

    @SuppressLint("QueryPermissionsNeeded")
    private void startCamera() {
        initSource();
        startCameraSource();
    }

    private void initSource() {
        if (cameraSource == null) {
            cameraSource = new CameraSource(this, graphicOverlay);
            cameraSource.setFacing(CameraSource.CAMERA_FACING_FRONT);
            cameraSource.setStartingDegrees(180);
        }
        setProcessor();
    }

    protected void setProcessor() {
        cameraSource.setMachineLearningFrameProcessor(awarenessProcessor);
    }

    private void startCameraSource() {
        if (cameraSource != null && preview != null && graphicOverlay != null) {
            try {
                preview.start(cameraSource, graphicOverlay);
            } catch (IOException e) {
                cameraSource.release();
                cameraSource = null;
                finish();
            }
        }
    }

    public void stopCamera() {
        preview.stop();
        preview.release();
        preview = null;
        cameraSource = null;
    }

    public void initCalibration() {
        Button calibrationBtn = findViewById(R.id.calibrationBtn);
        reader = new MsgReader(CameraActivity.this);
        calibrationBtn.setOnClickListener(v -> {
            reader.speak("Starting calibration!");
            reader.speak("Please look at the road for 5 seconds!");
            // TODO: save calibration data
        });
    }

    public void finishActivity() {
        reader.stop();
        stopCamera();
        finish();
    }

    public void onBackPressed() {
        finishActivity();
    }
}
