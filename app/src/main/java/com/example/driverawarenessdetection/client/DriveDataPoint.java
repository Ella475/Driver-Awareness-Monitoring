package com.example.driverawarenessdetection.client;

public class DriveDataPoint {
    private int awarenessPercentage;
    private boolean asleep;
    private boolean inattentive;
    private String id;
    private String driveId;

    public DriveDataPoint(int awarenessPercentage, boolean asleep, boolean inattentive, String id, String driveId) {
        this.awarenessPercentage = awarenessPercentage;
        this.asleep = asleep;
        this.inattentive = inattentive;
        this.id = id;
        this.driveId = driveId;
    }

    public int getAwarenessPercentage() {
        return awarenessPercentage;
    }

    public boolean isAsleep() {
        return asleep;
    }

    public boolean isInattentive() {
        return inattentive;
    }

    public String getId() {
        return id;
    }

    public String getDriveId() {
        return driveId;
    }
}
