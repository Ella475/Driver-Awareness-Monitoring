package com.example.driverawarenessdetection.video_processing.awareness_detection.alerts;

public class AlertsData {

    private DrivingAlertTiming durationTimeAlert = DrivingAlertTiming.ONE_HOUR;
    private boolean sleepAlert = true;
    private boolean attentionAlert = true;


    public void setTimeData(DrivingAlertTiming time) {
        durationTimeAlert = time;
    }

    public void setSleepAlert( boolean sleepAlert) {
        this.sleepAlert = sleepAlert;
    }

    public void setAttentionAlert(boolean attentionAlert) {
        this.attentionAlert = attentionAlert;
    }

    public DrivingAlertTiming getDurationTimeAlert() {
        return durationTimeAlert;
    }

    public boolean getSleepAlert() {
        return sleepAlert;
    }

    public boolean getAttentionAlert() {
        return attentionAlert;
    }

}

