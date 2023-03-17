package com.example.driverawarenessdetection.statistics;

import android.view.View;

import com.example.driverawarenessdetection.client.DriveData;

import java.util.ArrayList;

public abstract class ChartCreator {

    protected final DriveData driveData;
    protected final ArrayList<DriveData> driveDataList;
    protected final String title;

    public ChartCreator(DriveData driveData, String title) {
        this.driveData = driveData;
        this.title = title;
        this.driveDataList = null;
    }

    public ChartCreator(ArrayList<DriveData> driveDataList, String title) {
        this.driveDataList = driveDataList;
        this.title = title;
        this.driveData = null;
    }

    public abstract void initChart(View chart);
}
