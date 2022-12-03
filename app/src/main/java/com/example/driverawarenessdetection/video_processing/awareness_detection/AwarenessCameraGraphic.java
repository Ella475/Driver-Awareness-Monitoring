package com.example.driverawarenessdetection.video_processing.awareness_detection;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.driverawarenessdetection.video_processing.camera.GraphicOverlay;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceLandmark;
import com.google.mlkit.vision.face.FaceLandmark.LandmarkType;


public class AwarenessCameraGraphic extends GraphicOverlay.Graphic {
    private static final float FACE_POSITION_RADIUS = 8.0f;
    private static final float BOX_STROKE_WIDTH = 5.0f;

    private static final float TEXT_SIZE = 30.0f;
    private static final float TEXT_SIZE_BIG = 100f;

    private final Paint smallPaint;
    private final Paint bigPaint;

    private Boolean isAware;
    private volatile Face face;

    AwarenessCameraGraphic(GraphicOverlay overlay, Face face, Boolean isAware) {
        super(overlay);

        this.isAware = isAware;
        this.face = face;

        smallPaint = new Paint();
        smallPaint.setColor(Color.WHITE);
        smallPaint.setTextSize(TEXT_SIZE);
        smallPaint.setStrokeWidth(BOX_STROKE_WIDTH);
        smallPaint.setStyle(Paint.Style.STROKE);

        bigPaint = new Paint();
        bigPaint.setColor(Color.BLACK);
        bigPaint.setTextSize(TEXT_SIZE_BIG);
        bigPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    public void draw(Canvas canvas) {
        float offset = 0;
        Face face = this.face;
        if (face == null) {
            drawText(canvas, "Face Not Found.", offset++);
            drawText(canvas, "Readjust Your Device!", offset++);
            return;
        }
        drawText(canvas, "Face Found. Ready To Go!", offset++);

        if (isAware)
            drawText(canvas, "Road Awareness Detected!", offset + 0.5f);
        else
            drawText(canvas, "Road Awareness Not Detected!", offset + 0.5f);

        // Draws a circle at the position of the detected face, with the face's track id below.
        float center_x = translateX(face.getBoundingBox().centerX());
        float center_y = translateY(face.getBoundingBox().centerY());

        // Calculate positions.
        float left = center_x - scale(face.getBoundingBox().width() / 2.0f);
        float top = center_y - scale(face.getBoundingBox().height() / 2.0f);
        float right = center_x + scale(face.getBoundingBox().width() / 2.0f);
        float bottom = center_y + scale(face.getBoundingBox().height() / 2.0f);

        canvas.drawRect(left, top, right, bottom, smallPaint);

        // Draw facial landmarks
        drawFaceLandmark(canvas, FaceLandmark.LEFT_EYE);
        drawFaceLandmark(canvas, FaceLandmark.RIGHT_EYE);
        drawFaceLandmark(canvas, FaceLandmark.LEFT_CHEEK);
        drawFaceLandmark(canvas, FaceLandmark.RIGHT_CHEEK);
    }

    private void drawFaceLandmark(Canvas canvas, @LandmarkType int landmarkType) {
        FaceLandmark faceLandmark = face.getLandmark(landmarkType);
        if (faceLandmark != null) {
            canvas.drawCircle(
                    translateX(faceLandmark.getPosition().x),
                    translateY(faceLandmark.getPosition().y),
                    FACE_POSITION_RADIUS,
                    this.smallPaint);
        }
    }

    private void drawText(Canvas canvas, String text, float offset) {
        int height = canvas.getHeight() / 2;
        int width = canvas.getWidth() / 2;
        canvas.drawText(text, 0, text.length(), width, height - 700 + offset * 100, bigPaint);
    }
}
