package com.example.driverawarenessdetection.client;

import com.example.driverawarenessdetection.login.data.LoginRepository;

import org.json.JSONException;

public class DriveDataReceiver {
    private final Client client = Client.getInstance();
    private final String userId = LoginRepository.getInstance(null).getLoggedInUser().getUserId();
    private DriveData driveData;

    public void receiveDriveData() throws JSONException {
        driveData = new DriveData(client.getLastDriveData(userId));
    }

    public DriveData getDriveData(){
        return this.driveData;
    }

//    public string getLastDriveId(){
//        // TODO: get the last drive id from the database
//
//    }


}
