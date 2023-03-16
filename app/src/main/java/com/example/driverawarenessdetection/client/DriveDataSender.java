package com.example.driverawarenessdetection.client;

import com.example.driverawarenessdetection.login.data.LoginRepository;

public class DriveDataSender {
    private final Client client = Client.getInstance();
    private final String driveId;

    public DriveDataSender() {
        String user_id = LoginRepository.getInstance(null).getLoggedInUser().getUserId();
        driveId = client.startDrive(user_id);
    }

    public void sendDriveData(int awarenessPercentage, boolean asleep, boolean inattentive) {
        client.sendDriveData(driveId, awarenessPercentage, asleep, inattentive);

    }
}



