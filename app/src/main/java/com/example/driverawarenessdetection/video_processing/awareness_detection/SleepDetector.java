package com.example.driverawarenessdetection.video_processing.awareness_detection;

import com.google.mlkit.vision.face.Face;

public class SleepDetector extends AwarenessDetector {
    private float SLEEP_THRESHOLD;

    SleepDetector(int max_history, float sleep_threshold) {
        super(max_history);
        SLEEP_THRESHOLD = sleep_threshold;
    }

    @Override
    protected boolean isNotAwareInFrame(Face face) {
        if (face.getLeftEyeOpenProbability() == null
                || face.getRightEyeOpenProbability() == null) {
            return false;
        }

        return face.getLeftEyeOpenProbability() < SLEEP_THRESHOLD
                && face.getRightEyeOpenProbability() < SLEEP_THRESHOLD;
    }
}
