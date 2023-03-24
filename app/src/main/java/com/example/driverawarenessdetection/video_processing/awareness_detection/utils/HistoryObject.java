package com.example.driverawarenessdetection.video_processing.awareness_detection.utils;

public class HistoryObject {
    private final boolean isNotAware;
    private final float awarenessPercentage;

    public HistoryObject(boolean notAware, float awarenessPercentage) {
        this.isNotAware = notAware;
        this.awarenessPercentage = awarenessPercentage;
    }

    public boolean getNotAware() {
        return isNotAware;
    }

    public float getAwarenessPercentage() {
        return awarenessPercentage;
    }
}
