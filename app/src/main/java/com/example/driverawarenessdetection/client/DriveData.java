package com.example.driverawarenessdetection.client;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// This class is used to store the drive data received from the server
public class DriveData {
    /*
    List of DriveDataPoint objects

    Each DriveDataPoint object has the following attributes:
    - awarenessPercentage (int) - the percentage of the drive that the driver was aware
    - asleep (boolean) - whether the driver was asleep
    - inattentive (boolean) - whether the driver was inattentive
    - createdAt (string) - the timestamp of the data point
    - id (string) - the id of the data point
    - driveId (string) - the id of the drive
     */
    List<DriveDataPoint> driveDataList;

    public DriveData(String jsonString) throws JSONException {
        if (jsonString == null) {
            return;
        }
        JSONArray jsonArray = new JSONArray(jsonString);
        List<DriveDataPoint> driveDataList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int awarenessPercentage = jsonObject.getInt("awareness_percentage");
            boolean asleep = jsonObject.getInt("asleep") != 0;
            boolean inattentive = jsonObject.getInt("inattentive") != 0;
            String id = Integer.toString(jsonObject.getInt("id"));
            String driveId = Integer.toString(jsonObject.getInt("drive_id"));
            DriveDataPoint driveDataPoint = new DriveDataPoint(awarenessPercentage, asleep, inattentive, id, driveId);
            driveDataList.add(driveDataPoint);
        }

        this.driveDataList = driveDataList;

    }

    public List<DriveDataPoint> getDriveDataList() {
        return driveDataList;
    }
}

