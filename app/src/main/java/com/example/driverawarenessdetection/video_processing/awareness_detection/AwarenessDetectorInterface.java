package com.example.driverawarenessdetection.video_processing.awareness_detection;

import com.google.mlkit.vision.face.Face;

public interface AwarenessDetectorInterface {
    void processFace(Face face);
    boolean isNotAware();
    float getAwareProbability();
}
