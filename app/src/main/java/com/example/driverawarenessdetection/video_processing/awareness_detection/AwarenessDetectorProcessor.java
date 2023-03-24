package com.example.driverawarenessdetection.video_processing.awareness_detection;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.driverawarenessdetection.video_processing.camera.GraphicOverlay;
import com.example.driverawarenessdetection.video_processing.camera.VisionProcessorBase;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.util.List;

public class AwarenessDetectorProcessor extends VisionProcessorBase<List<Face>> {

    private final FaceDetector detector;
    public final DetectorOnSuccess onSuccessDetector = new DetectorOnSuccess();
    private boolean updateLayout = true;

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

    public void turnOffLayoutUpdate() {
        updateLayout = false;
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
            onSuccessDetector.apply(faces, updateLayout ? graphicOverlay : null);
    }

    @Override
    protected void onFailure(@NonNull Exception e) {
        stop();
    }
}
