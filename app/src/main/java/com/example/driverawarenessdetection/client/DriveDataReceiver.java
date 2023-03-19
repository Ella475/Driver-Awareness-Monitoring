package com.example.driverawarenessdetection.client;

import com.example.driverawarenessdetection.login.data.LoginRepository;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DriveDataReceiver {
    private final Client client;
    private final String userId;
    private ArrayList<DriveData> driveDataList;

    public DriveDataReceiver() {
        client = Client.getInstance();
        userId = LoginRepository.getInstance(null).getLoggedInUser().getUserId();
        initDriveDataList();
    }

    private void initDriveDataList() {
        try {
            driveDataList = new ArrayList<>();
            List<HashMap<String, String>> drives = client.getDrives(userId);
            for (HashMap<String, String> drive : drives) {
                String driveId = drive.get("driveId");
                String driveTime = drive.get("driveTime");
                driveDataList.add(new DriveData(client.getDriveData(driveId), driveId, driveTime));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<DriveData> getDriveDataList() {
        return driveDataList;
    }

    public DriveData getDriveData(int index) {
        return driveDataList.get(index);
    }

}
