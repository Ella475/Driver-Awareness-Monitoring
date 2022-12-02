package com.example.driverawarenessdetection;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.driverawarenessdetection.video_processing.awareness_detection.FaceDetectorProcessor;
import com.example.driverawarenessdetection.video_processing.camera.CameraSource;
import com.example.driverawarenessdetection.video_processing.camera.CameraSourcePreview;
import com.example.driverawarenessdetection.video_processing.camera.GraphicOverlay;

import java.io.IOException;


public class CameraActivity extends AppCompatActivity {

    private CameraSourcePreview preview;
    private GraphicOverlay graphicOverlay;
    protected CameraSource cameraSource;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        preview = findViewById(R.id.camera_source_preview);
        graphicOverlay = findViewById(R.id.graphic_overlay);

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
        cameraSource.setMachineLearningFrameProcessor(new FaceDetectorProcessor(this));
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

    public void finishActivity() {
        stopCamera();
        finish();
    }

    public void onBackPressed() {
        finishActivity();
    }
}
