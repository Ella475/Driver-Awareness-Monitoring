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
    private static final String TAG = "CameraActivity";

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

    /**
     * Starts or restarts the camera source, if it exists. If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     */
    private void startCameraSource() {
        if (cameraSource != null) {
            try {
                if (preview == null) {
                    Log.d(TAG, "resume: Preview is null");
                }
                if (graphicOverlay == null) {
                    Log.d(TAG, "resume: graphOverlay is null");
                }
                preview.start(cameraSource, graphicOverlay);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                cameraSource.release();
                cameraSource = null;
            }
        }
    }

    public void stopCameraSource() {
        preview.stop();
        preview.release();
        cameraSource = null;
    }

    public void finishActivity() {
        stopCameraSource();
        finish();
    }
}
