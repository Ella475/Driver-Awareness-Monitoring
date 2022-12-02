package com.example.driverawarenessdetection.video_processing.awareness_detection;

import com.google.mlkit.vision.face.Face;

import java.util.ArrayDeque;
import java.util.Arrays;

public abstract class AwarenessDetector implements AwarenessDetectorInterface{
    private final ArrayDeque<Boolean> history = new ArrayDeque<>();
    protected int MAX_HISTORY;

    public AwarenessDetector(int max_history) {
        MAX_HISTORY = max_history;
    }

    abstract protected boolean isNotAwareInFrame(Face face);

    public boolean isNotAware(Face face) {
        boolean aware = isNotAwareInFrame(face);
        history.addLast(aware);

        if (history.size() > MAX_HISTORY) {
            history.removeFirst();
        }
        if (history.size() < MAX_HISTORY) {
            return false;
        }
        return !Arrays.asList(history.toArray()).contains(false);
    }
}
