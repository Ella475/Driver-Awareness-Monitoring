package com.example.driverawarenessdetection.video_processing.awareness_detection;

import androidx.annotation.NonNull;

import com.example.driverawarenessdetection.video_processing.camera.GraphicOverlay;
import com.google.mlkit.vision.face.Face;

import java.util.HashMap;
import java.util.List;

public class DetectorOnSuccess {
    public final HashMap<Integer, AwarenessManager> awarenessHashMap = new HashMap<>();

    protected void apply(@NonNull List<Face> faces, GraphicOverlay graphicOverlay) {
        if (faces.isEmpty()) {
            if (graphicOverlay != null)
                graphicOverlay.add(new AwarenessCameraGraphic(graphicOverlay, null, null));
            return;
        }
        Face face = faces.get(0);
        AwarenessManager manager = awarenessHashMap.get(face.getTrackingId());
        if (manager == null) {
            manager = new AwarenessManager();
            awarenessHashMap.put(face.getTrackingId(), manager);
        }
        manager.processFace(face);
        if (graphicOverlay != null) {
            boolean isAware = !manager.isNotAware();
            graphicOverlay.add(new AwarenessCameraGraphic(graphicOverlay, face, isAware));
        }
    }
}
