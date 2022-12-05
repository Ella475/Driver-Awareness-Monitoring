package com.example.driverawarenessdetection.video_processing.awareness_detection;

public class CalibrationData {
    private float calibratedXAngle = 0;
    private float calibratedYAngle = 0;

    public void setCalibrationData(float x, float y) {
        calibratedXAngle = x;
        calibratedYAngle = y;
    }

    public float getCalibratedXAngle() {
        return calibratedXAngle;
    }

    public float getCalibratedYAngle() {
        return calibratedYAngle;
    }
}
