package com.example.driverawarenessdetection.video_processing.awareness_detection;

import android.os.Build;

import com.example.driverawarenessdetection.video_processing.awareness_detection.utils.HistoryObject;
import com.google.mlkit.vision.face.Face;

import java.util.ArrayDeque;
import java.util.Arrays;

public abstract class AwarenessDetector implements AwarenessDetectorInterface{
    private final ArrayDeque<HistoryObject> history = new ArrayDeque<>();
    protected int MAX_HISTORY;

    public AwarenessDetector(int max_history) {
        MAX_HISTORY = max_history;
    }

    abstract protected boolean isNotAwareInFrame(Face face);

    abstract protected float getAwareProbabilityInFrame(Face face);

    @Override
    public void processFace(Face face) {
        boolean notAware = isNotAwareInFrame(face);
        float awarenessPercentage = getAwareProbabilityInFrame(face);
        history.addLast(new HistoryObject(notAware, awarenessPercentage));

        if (history.size() > MAX_HISTORY) {
            history.removeFirst();
        }
    }

    @Override
    public boolean isNotAware() {
        if (history.size() < MAX_HISTORY) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return !Arrays.asList(history.stream()
                    .map(HistoryObject::getNotAware).toArray()).contains(false);
        }
        return false;
    }

    @Override
    public float getAwareProbability() {
        if (history.size() < MAX_HISTORY) {
            return 1;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return (float) history.stream()
                    .mapToDouble(HistoryObject::getAwarenessPercentage).average().orElse(Double.NaN);
        }
        return 1;
    }


}
