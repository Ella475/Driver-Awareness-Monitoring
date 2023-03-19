package com.example.driverawarenessdetection.client;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// This class is used to store the drive data received from the server
public class DriveData {
    private final ArrayList<Double> awarenessPercentages = new ArrayList<>();
    private final ArrayList<Boolean> asleeps = new ArrayList<>();
    private final ArrayList<Boolean> inattentives = new ArrayList<>();
    private final ArrayList<String> index = new ArrayList<>();
    private float meanAwarenessPercentage;
    private int len;
    private String driveId;
    private String driveTime;


    public DriveData(String jsonString, String driveId, String driveTime) throws JSONException {
        parseDriveData(jsonString);
        this.driveId = driveId;
        this.driveTime = driveTime;
    }

    private void parseDriveData(String jsonString) throws JSONException {
        if (jsonString == null) {
            return;
        }
        JSONArray jsonArray = new JSONArray(jsonString);
        int sum = 0;
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int awarenessPercentage = jsonObject.getInt("awareness_percentage");
            boolean asleep = jsonObject.getInt("asleep") != 0;
            boolean inattentive = jsonObject.getInt("inattentive") != 0;

            awarenessPercentages.add((double)awarenessPercentage);
            asleeps.add(asleep);
            inattentives.add(inattentive);
            index.add(Integer.toString(i));
            sum += awarenessPercentage;
        }

        len = jsonArray.length();
        meanAwarenessPercentage = (float) sum / len;
    }

    public ArrayList<Double> getAwarenessPercentageList() {
        return awarenessPercentages;
    }

    public ArrayList<Boolean> getAsleepList() {
        return asleeps;
    }

    public ArrayList<Boolean> getInattentiveList() {
        return inattentives;
    }

    public ArrayList<String> getIndexList() {
        return index;
    }

    public float getMeanAwarenessPercentage() {
        return meanAwarenessPercentage;
    }

    public int getLen() {
        return len;
    }

    public String getDriveId() {
        return driveId;
    }

    public String getDriveTime() {
        return driveTime;
    }
}

