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
        attention_detector = new AttentionDetector(10, 20, 20, 50);
    }

    @Override
    public boolean isNotAware(Face face) {
        return sleep_detector.isNotAware(face) || attention_detector.isNotAware(face);
    }

    @Override
    public float getAwareProbability(Face face) {
        float sleepProbability = sleep_detector.getAwareProbability(face);
        float attentionProbability = attention_detector.getAwareProbability(face);
        return sleepProbability * attentionProbability;
    }
}
