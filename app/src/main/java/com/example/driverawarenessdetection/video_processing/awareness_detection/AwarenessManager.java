package com.example.driverawarenessdetection.video_processing.awareness_detection;

import com.google.mlkit.vision.face.Face;

public class AwarenessManager implements AwarenessDetectorInterface{
    private final AwarenessDetectorInterface sleep_detector;
    private final AwarenessDetectorInterface attention_detector;

    AwarenessManager(int max_history, float sleep_threshold,
                      float x_max_dev, float y_max_dev, float z_max_dev) {
        sleep_detector = new SleepDetector(max_history, sleep_threshold);
        attention_detector = new AttentionDetector(max_history, x_max_dev, y_max_dev, z_max_dev);
    }

    AwarenessManager() {
        sleep_detector = new SleepDetector(10, 0.5f);
        attention_detector = new AttentionDetector(20, 20, 20, 50);
    }

    @Override
    public void processFace(Face face) {
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
