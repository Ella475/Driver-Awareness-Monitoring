package com.example.driverawarenessdetection.video_processing.awareness_detection.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;

import com.example.driverawarenessdetection.video_processing.awareness_detection.AwarenessDetectorProcessor;
import com.example.driverawarenessdetection.video_processing.camera.CameraSource;
import com.example.driverawarenessdetection.video_processing.camera.CameraSourcePreview;
import com.example.driverawarenessdetection.video_processing.camera.GraphicOverlay;

import java.io.IOException;

public class CameraSourceWrapper {

    private CameraSourcePreview preview;
    private final GraphicOverlay graphicOverlay;
    public final AwarenessDetectorProcessor awarenessProcessor;
    private final Activity activity;
    protected CameraSource cameraSource;

    public CameraSourceWrapper(CameraSourcePreview preview, GraphicOverlay graphicOverlay, Activity activity) {
        this.preview = preview;
        this.graphicOverlay = graphicOverlay;
        this.activity = activity;
        this. awarenessProcessor = new AwarenessDetectorProcessor(this.activity);
    }

    @SuppressLint("QueryPermissionsNeeded")
    public void start() {
        initSource();
        startCameraSource();
    }

    private void initSource() {
        if (cameraSource == null) {
            cameraSource = new CameraSource(activity, graphicOverlay);
            cameraSource.setFacing(CameraSource.CAMERA_FACING_FRONT);
            if (Build.FINGERPRINT.contains("generic") || Build.FINGERPRINT.contains("emulator")) {
                // running on an emulator
                cameraSource.setStartingDegrees(180);
                System.out.println("Running on an emulator." + " Starting degrees: 180");
            } else {
                // running on a real device
                cameraSource.setStartingDegrees(270);
                System.out.println("Running on real device." + " Starting degrees: 270");
            }
        }
        setProcessor();
    }

    private void setProcessor() {
        cameraSource.setMachineLearningFrameProcessor(awarenessProcessor);
    }

    private void startCameraSource() {
        if (cameraSource != null && preview != null && graphicOverlay != null) {
            try {
                preview.start(cameraSource, graphicOverlay);
            } catch (IOException e) {
                cameraSource.release();
                cameraSource = null;
            }
        }
    }

    public void stop() {
        preview.stop();
        preview.release();
        preview = null;
        cameraSource = null;
    }
}