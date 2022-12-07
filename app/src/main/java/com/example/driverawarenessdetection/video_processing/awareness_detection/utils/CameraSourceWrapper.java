package com.example.driverawarenessdetection.video_processing.awareness_detection.utils;

import android.annotation.SuppressLint;
import android.app.Activity;

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
            cameraSource.setStartingDegrees(180);
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