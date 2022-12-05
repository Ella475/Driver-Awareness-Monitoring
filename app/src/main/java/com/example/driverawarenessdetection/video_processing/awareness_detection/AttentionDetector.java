package com.example.driverawarenessdetection.video_processing.awareness_detection;

import com.google.mlkit.vision.face.Face;

public class AttentionDetector extends AwarenessDetector {
    public static CalibrationData calibrationData = new CalibrationData();
    float X_ANGLE_MAX_DEVIATION;
    float Y_ANGLE_MAX_DEVIATION;
    float Z_ANGLE_MAX_DEVIATION;

    AttentionDetector(int max_history, float x_max_dev, float y_max_dev, float z_max_dev) {
        super(max_history);
        X_ANGLE_MAX_DEVIATION = x_max_dev;
        Y_ANGLE_MAX_DEVIATION = y_max_dev;
        Z_ANGLE_MAX_DEVIATION = z_max_dev;
    }

    @Override
    protected boolean isNotAwareInFrame(Face face) {
        float x_angle = face.getHeadEulerAngleX() - calibrationData.getCalibratedXAngle();
        float y_angle = face.getHeadEulerAngleY() - calibrationData.getCalibratedYAngle();
        float z_angle = face.getHeadEulerAngleZ();

        return (Math.abs(x_angle) > X_ANGLE_MAX_DEVIATION) ||
                (Math.abs(y_angle) > Y_ANGLE_MAX_DEVIATION) ||
                (Math.abs(z_angle) > Z_ANGLE_MAX_DEVIATION);
    }

    @Override
    protected float getAwareProbabilityInFrame(Face face) {
        float x_angle = face.getHeadEulerAngleX() - calibrationData.getCalibratedXAngle();
        float y_angle = face.getHeadEulerAngleY() - calibrationData.getCalibratedYAngle();

        float xDeviation = Math.abs(Math.abs(x_angle) - X_ANGLE_MAX_DEVIATION) / X_ANGLE_MAX_DEVIATION;
        float yDeviation = Math.abs(Math.abs(y_angle) - Y_ANGLE_MAX_DEVIATION) / Y_ANGLE_MAX_DEVIATION;

        float xFactor = (Math.abs(x_angle) <= X_ANGLE_MAX_DEVIATION) ? 5 : 10;
        float yFactor = (Math.abs(y_angle) <= Y_ANGLE_MAX_DEVIATION) ? 5 : 10;

        int awarePercentage = (int) (100 - xFactor * Math.pow(xDeviation, 2) -
                yFactor * Math.pow(yDeviation, 2));

        return Math.max(awarePercentage / 100.0f, 0.0f);
    }
}
