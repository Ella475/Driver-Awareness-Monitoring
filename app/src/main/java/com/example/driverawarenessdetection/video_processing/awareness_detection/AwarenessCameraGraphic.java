package com.example.driverawarenessdetection.video_processing.awareness_detection;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.driverawarenessdetection.video_processing.camera.GraphicOverlay;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceLandmark;
import com.google.mlkit.vision.face.FaceLandmark.LandmarkType;

/**
 * Graphic instance for rendering face position, contour, and landmarks within the associated
 * graphic overlay view.
 */
public class AwarenessCameraGraphic extends GraphicOverlay.Graphic {
    private static final float FACE_POSITION_RADIUS = 8.0f;
    private static final float ID_TEXT_SIZE = 30.0f;
    private static final float ID_Y_OFFSET = 40.0f;
    private static final float BOX_STROKE_WIDTH = 5.0f;
    private static final int NUM_COLORS = 10;
    private static final int[][] COLORS =
            new int[][]{
                    // {Text color, background color}
                    {Color.BLACK, Color.WHITE},
                    {Color.WHITE, Color.MAGENTA},
                    {Color.BLACK, Color.LTGRAY},
                    {Color.WHITE, Color.RED},
                    {Color.WHITE, Color.BLUE},
                    {Color.WHITE, Color.DKGRAY},
                    {Color.BLACK, Color.CYAN},
                    {Color.BLACK, Color.YELLOW},
                    {Color.WHITE, Color.BLACK},
                    {Color.BLACK, Color.GREEN}
            };
    private static final float ID_TEXT_SIZE_BIG = 100f;

    private final Paint facePositionPaint;
    private final Paint myPaint;
    private final Paint[] idPaints;
    private final Paint[] boxPaints;
    private final Paint[] labelPaints;

    private Boolean isAware;

    private volatile Face face;

    AwarenessCameraGraphic(GraphicOverlay overlay, Face face, Boolean isAware) {
        super(overlay);

        this.isAware = isAware;
        this.face = face;

        facePositionPaint = new Paint();
        facePositionPaint.setColor(Color.WHITE);

        myPaint = new Paint();
        myPaint.setColor(COLORS[7][0] /* text color */);
        myPaint.setTextSize(ID_TEXT_SIZE_BIG);
        myPaint.setTextAlign(Paint.Align.CENTER);

        int numColors = COLORS.length;
        idPaints = new Paint[numColors];
        boxPaints = new Paint[numColors];
        labelPaints = new Paint[numColors];
        for (int i = 0; i < numColors; i++) {
            idPaints[i] = new Paint();
            idPaints[i].setColor(COLORS[i][0] /* text color */);
            idPaints[i].setTextSize(ID_TEXT_SIZE);

            boxPaints[i] = new Paint();
            boxPaints[i].setColor(COLORS[i][1] /* background color */);
            boxPaints[i].setStyle(Paint.Style.STROKE);
            boxPaints[i].setStrokeWidth(BOX_STROKE_WIDTH);

            labelPaints[i] = new Paint();
            labelPaints[i].setColor(COLORS[i][1] /* background color */);
            labelPaints[i].setStyle(Paint.Style.FILL);
        }
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
//    canvas.drawCircle(x, y, FACE_POSITION_RADIUS, facePositionPaint);

        // Calculate positions.
        float left = center_x - scale(face.getBoundingBox().width() / 2.0f);
        float top = center_y - scale(face.getBoundingBox().height() / 2.0f);
        float right = center_x + scale(face.getBoundingBox().width() / 2.0f);
        float bottom = center_y + scale(face.getBoundingBox().height() / 2.0f);

        // Decide color based on face ID
        int colorID = (face.getTrackingId() == null) ? 0 : Math.abs(face.getTrackingId() % NUM_COLORS);
        canvas.drawRect(left, top, right, bottom, boxPaints[colorID]);


        FaceLandmark leftEye = face.getLandmark(FaceLandmark.LEFT_EYE);
        if (leftEye != null) {
            float leftEyeLeft =
                    translateX(leftEye.getPosition().x) - idPaints[colorID].measureText("Left Eye") / 2.0f;
            canvas.drawRect(
                    leftEyeLeft - BOX_STROKE_WIDTH,
                    translateY(leftEye.getPosition().y) + ID_Y_OFFSET - ID_TEXT_SIZE,
                    leftEyeLeft + idPaints[colorID].measureText("Left Eye") + BOX_STROKE_WIDTH,
                    translateY(leftEye.getPosition().y) + ID_Y_OFFSET + BOX_STROKE_WIDTH,
                    labelPaints[colorID]);
            canvas.drawText(
                    "Left Eye",
                    leftEyeLeft,
                    translateY(leftEye.getPosition().y) + ID_Y_OFFSET,
                    idPaints[colorID]);
        }

        FaceLandmark rightEye = face.getLandmark(FaceLandmark.RIGHT_EYE);
        if (rightEye != null) {
            float rightEyeLeft =
                    translateX(rightEye.getPosition().x) - idPaints[colorID].measureText("Right Eye") / 2.0f;
            canvas.drawRect(
                    rightEyeLeft - BOX_STROKE_WIDTH,
                    translateY(rightEye.getPosition().y) + ID_Y_OFFSET - ID_TEXT_SIZE,
                    rightEyeLeft + idPaints[colorID].measureText("Right Eye") + BOX_STROKE_WIDTH,
                    translateY(rightEye.getPosition().y) + ID_Y_OFFSET + BOX_STROKE_WIDTH,
                    labelPaints[colorID]);
            canvas.drawText(
                    "Right Eye",
                    rightEyeLeft,
                    translateY(rightEye.getPosition().y) + ID_Y_OFFSET,
                    idPaints[colorID]);
        }

        float l =
                translateX(canvas.getWidth() / 2.0f) - idPaints[colorID].measureText("Left Eye") / 2.0f;
        canvas.drawRect(
                l - BOX_STROKE_WIDTH,
                translateY(canvas.getHeight() / 2.0f) + ID_Y_OFFSET - ID_TEXT_SIZE,
                l + idPaints[colorID].measureText("Left Eye") + BOX_STROKE_WIDTH,
                translateY(canvas.getHeight() / 2.0f) + ID_Y_OFFSET + BOX_STROKE_WIDTH,
                labelPaints[colorID]);
        canvas.drawText(
                "Left Eye",
                l,
                translateY(canvas.getHeight() / 2.0f) + ID_Y_OFFSET,
                idPaints[colorID]);


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
                    facePositionPaint);
        }
    }

    private void drawText(Canvas canvas, String text, float offset) {
        int height = canvas.getHeight() / 2;
        int width = canvas.getWidth() / 2;
        canvas.drawText(text, 0, text.length(), width, height - 700 + offset * 100, myPaint);
    }
}
