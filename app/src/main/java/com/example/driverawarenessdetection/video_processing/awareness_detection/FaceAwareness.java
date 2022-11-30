package com.example.driverawarenessdetection.video_processing.awareness_detection;

import com.google.mlkit.vision.face.Face;

import java.util.ArrayDeque;
import java.util.Arrays;

public class FaceAwareness {
    private static final float SLEEP_THRESHOLD = 0.5f;
    private static final int MAX_HISTORY = 10;

    // TODO: change to private
    public long lastCheckedAt;
    private final ArrayDeque<Boolean> history = new ArrayDeque<>();
    private boolean asleep;

    public boolean isSleepy(Face face) {
        lastCheckedAt = System.currentTimeMillis();
        if (face.getLeftEyeOpenProbability() == null
            || face.getRightEyeOpenProbability() == null) {
            return false;
        }

        boolean closedEyes = face.getLeftEyeOpenProbability() < SLEEP_THRESHOLD
                && face.getRightEyeOpenProbability() < SLEEP_THRESHOLD;
        history.addLast(closedEyes);


        if (history.size() > MAX_HISTORY) {
            history.removeFirst();
        }
        if (history.size() < MAX_HISTORY) {
            return false;
        }
        asleep = !Arrays.asList(history.toArray()).contains(false);
        return asleep;
    }
}
