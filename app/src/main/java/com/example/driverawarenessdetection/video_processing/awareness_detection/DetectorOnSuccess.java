package com.example.driverawarenessdetection.video_processing.awareness_detection;

import androidx.annotation.NonNull;

import com.example.driverawarenessdetection.video_processing.camera.GraphicOverlay;
import com.google.mlkit.vision.face.Face;

import java.util.HashMap;
import java.util.List;

public class DetectorOnSuccess {
    private final AwarenessManager manager = new AwarenessManager();

    protected void apply(@NonNull List<Face> faces, GraphicOverlay graphicOverlay) {
        if (faces.isEmpty()) {
            if (graphicOverlay != null)
                graphicOverlay.add(new AwarenessCameraGraphic(graphicOverlay, null, null));
            return;
        }
        Face face = faces.get(0);
        manager.processFace(face);
        if (graphicOverlay != null) {
            boolean isAware = !manager.isNotAware();
            graphicOverlay.add(new AwarenessCameraGraphic(graphicOverlay, face, isAware));
        }
    }

    public AwarenessManager getManager() {
        return manager;
    }
}
