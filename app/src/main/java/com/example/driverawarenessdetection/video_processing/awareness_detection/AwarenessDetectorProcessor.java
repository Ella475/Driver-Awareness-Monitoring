package com.example.driverawarenessdetection.video_processing.awareness_detection;

import android.content.Context;
import android.graphics.PointF;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.driverawarenessdetection.video_processing.camera.GraphicOverlay;
import com.example.driverawarenessdetection.video_processing.camera.VisionProcessorBase;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;
import com.google.mlkit.vision.face.FaceLandmark;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AwarenessDetectorProcessor extends VisionProcessorBase<List<Face>> {

    private final FaceDetector detector;
    private final HashMap<Integer, AwarenessManager> awarenessHashMap = new HashMap<>();

    public AwarenessDetectorProcessor(Context context) {
        super(context);
        FaceDetectorOptions faceDetectorOptions = new FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                .enableTracking()
                .build();
        detector = FaceDetection.getClient(faceDetectorOptions);
    }

    @Override
    public void stop() {
        super.stop();
        detector.close();
    }

    @Override
    protected Task<List<Face>> detectInImage(InputImage image) {
        return detector.process(image);
    }

    @Override
    protected void onSuccess(@NonNull List<Face> faces, @NonNull GraphicOverlay graphicOverlay) {
        for (Face face : faces) {
            AwarenessManager manager = awarenessHashMap.get(face.getTrackingId());
            if (manager == null) {
                manager = new AwarenessManager();
                awarenessHashMap.put(face.getTrackingId(), manager);
            }
            boolean isAware = !manager.isNotAware(face);
            graphicOverlay.add(new AwarenessCameraGraphic(graphicOverlay, face, isAware));
        }
        if (faces.isEmpty()) {
            graphicOverlay.add(new AwarenessCameraGraphic(graphicOverlay, null, null));
        }
    }

    @Override
    protected void onFailure(@NonNull Exception e) {
        stop();
    }
}
