package com.example.driverawarenessdetection.video_processing.awareness_detection;

import com.google.mlkit.vision.face.Face;

public class SleepDetector extends AwarenessDetector {
    public float SLEEP_THRESHOLD;

    SleepDetector(int max_history, float sleep_threshold) {
        super(max_history);
        SLEEP_THRESHOLD = sleep_threshold;
    }

    @Override
    protected boolean isNotAwareInFrame(Face face) {
        return getAwareProbabilityInFrame(face) < SLEEP_THRESHOLD;
    }

    @Override
    protected float getAwareProbabilityInFrame(Face face) {
        if (face.getLeftEyeOpenProbability() == null
                || face.getRightEyeOpenProbability() == null) {
            return 1;
        }

        float eyesOpenProbability = (face.getLeftEyeOpenProbability() +
                face.getRightEyeOpenProbability()) / 2.0f;

        return (eyesOpenProbability > SLEEP_THRESHOLD) ? 1 : eyesOpenProbability;
    }
}
