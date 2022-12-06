package com.example.driverawarenessdetection.video_processing.awareness_detection;

import android.os.Build;
import android.util.Log;

import com.google.mlkit.vision.face.Face;

import java.time.Duration;
import java.time.Instant;

public class AwarenessManager implements AwarenessDetectorInterface{
    private final AwarenessDetectorInterface sleep_detector;
    private final AwarenessDetectorInterface attention_detector;
    private volatile boolean invokeCalibration = false;
    private Instant start_calibration_time;
    private long calibrationDelay = 5;

    AwarenessManager(int max_history, float sleep_threshold,
                      float x_max_dev, float y_max_dev, float z_max_dev) {
        sleep_detector = new SleepDetector(max_history, sleep_threshold);
        attention_detector = new AttentionDetector(max_history, x_max_dev, y_max_dev, z_max_dev);
    }

    AwarenessManager() {
        sleep_detector = new SleepDetector(10, 0.5f);
        attention_detector = new AttentionDetector(20, 20, 20, 50);
    }

    public void onCalibration() {
        invokeCalibration = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            start_calibration_time = Instant.now();
            Log.i("Calibration", String.valueOf(start_calibration_time));
        }
        Log.i("Calibration", "Started");
    }

    @Override
    public void processFace(Face face) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (invokeCalibration &&
                    Duration.between(start_calibration_time, Instant.now()).getSeconds() > calibrationDelay) {
                Log.i("Calibration", String.valueOf(Instant.now()));
                float x_angle = face.getHeadEulerAngleX();
                Log.i("Calibration_x", String.valueOf(x_angle));
                float y_angle = face.getHeadEulerAngleY();
                Log.i("Calibration_y", String.valueOf(y_angle));
                AttentionDetector.calibrationData.setCalibrationData(x_angle, y_angle);
                invokeCalibration = false;
            }
        }

        sleep_detector.processFace(face);
        attention_detector.processFace(face);
    }

    @Override
    public boolean isNotAware() {
        return sleep_detector.isNotAware() || attention_detector.isNotAware();
    }

    @Override
    public float getAwareProbability() {
        float sleepProbability = sleep_detector.getAwareProbability();
        float attentionProbability = attention_detector.getAwareProbability();
        return sleepProbability * attentionProbability;
    }
}
