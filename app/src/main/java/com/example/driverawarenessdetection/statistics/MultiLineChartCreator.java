package com.example.driverawarenessdetection.statistics;

import android.graphics.Color;
import android.view.View;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.concurrent.atomic.DoubleAccumulator;

import com.example.driverawarenessdetection.client.DriveData;
import com.github.mikephil.charting.charts.LineChart;

public class MultiLineChartCreator extends ChartCreator{

    public MultiLineChartCreator(ArrayList<DriveData> driveDataList, String title) {
        super(driveDataList, title);
    }

    @Override
    public void initChart(View viewChart) {
        LineChart chart = (LineChart) viewChart;

        ArrayList<Entry> entries;
        ArrayList<ILineDataSet> datasets = new ArrayList<>();
        for (DriveData driveData : driveDataList) {
            entries = new ArrayList<>();
            ArrayList<Double> awarenessList = driveData.getAwarenessPercentageList();
            for (int i = 0; i < awarenessList.size(); i++) {
                String label = "";
                if (i == 0) {
                    label = "Start";
                } else if (i == awarenessList.size() - 1) {
                    label = "End";
                } else if (i % 10 == 0) {
                    label = String.valueOf(i);
                }
                Entry entry = new Entry(i, awarenessList.get(i).floatValue());
                entries.add(entry);
            }

            LineDataSet dataset = new LineDataSet(entries, "Drive " + driveData.getDriveId());
            dataset.setDrawFilled(false);
            int red = (int) (Math.random() * 255);
            int green = (int) (Math.random() * 255);
            int blue = (int) (Math.random() * 255);
            dataset.setColor(Color.rgb(red, green, blue));
            dataset.setFillColor(Color.rgb(red, green, blue));
            dataset.setDrawCircles(false);
            dataset.setDrawValues(false);

            dataset.setCircleColor(Color.DKGRAY);
            datasets.add(dataset);
        }

        chart.setData(new LineData(datasets));

        chart.getDescription().setText(title);

        chart.getDescription().setTextColor(Color.RED);


        chart.animateY(1400, Easing.EaseInOutBounce);
    }

}
