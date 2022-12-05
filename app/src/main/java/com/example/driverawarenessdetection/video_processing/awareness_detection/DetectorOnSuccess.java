package com.example.driverawarenessdetection.video_processing.awareness_detection;

import androidx.annotation.NonNull;

import com.example.driverawarenessdetection.video_processing.camera.GraphicOverlay;
import com.google.mlkit.vision.face.Face;

import java.util.HashMap;
import java.util.List;

public class DetectorOnSuccess {
    private final HashMap<Integer, AwarenessManager> awarenessHashMap = new HashMap<>();

    protected void apply(@NonNull List<Face> faces, @NonNull GraphicOverlay graphicOverlay) {
        for (Face face : faces) {
            AwarenessManager manager = awarenessHashMap.get(face.getTrackingId());
            if (manager == null) {
                manager = new AwarenessManager();
                awarenessHashMap.put(face.getTrackingId(), manager);
            }
            boolean isAware = !manager.isNotAware(face);
            graphicOverlay.add(new AwarenessCameraGraphic(graphicOverlay, face, isAware));
            face.getHeadEulerAngleX();
        }
        if (faces.isEmpty()) {
            graphicOverlay.add(new AwarenessCameraGraphic(graphicOverlay, null, null));
        }
    }
}
