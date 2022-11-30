/*
 * Copyright 2020 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

public class FaceDetectorProcessor extends VisionProcessorBase<List<Face>> {

  private static final String TAG = "FaceDetectorProcessor";

  private final FaceDetector detector;

  private final HashMap<Integer, FaceAwareness> awarenessHashMap = new HashMap<>();

  public FaceDetectorProcessor(Context context) {
    super(context);
    FaceDetectorOptions faceDetectorOptions = new FaceDetectorOptions.Builder()
          .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
          .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
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
      FaceAwareness faceAwareness = awarenessHashMap.get(face.getTrackingId());
      if (faceAwareness == null) {
        faceAwareness = new FaceAwareness();
        awarenessHashMap.put(face.getTrackingId(), faceAwareness);
      }
      boolean isSleepy = faceAwareness.isSleepy(face);
      graphicOverlay.add(new FaceGraphic(graphicOverlay, face, isSleepy));
    }
  }

  @Override
  protected void onFailure(@NonNull Exception e) {
    stop();
  }
}
