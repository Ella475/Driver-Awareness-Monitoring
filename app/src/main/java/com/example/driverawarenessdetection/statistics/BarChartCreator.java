package com.example.driverawarenessdetection.statistics;

import android.view.View;

import com.example.driverawarenessdetection.client.DriveData;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

public class BarChartCreator extends ChartCreator{

    public BarChartCreator(DriveData driveData, String title) {
        super(driveData, title);
    }

    public BarChartCreator(ArrayList<DriveData> driveDataList, String title) {
        super(driveDataList, title);
    }

    @Override
    public void initChart(View viewChart) {
        BarChart chart = (BarChart) viewChart;
        if (driveDataList != null) {
            multiBarChart(chart);
        } else {
            singleBarChart(chart);
        }
    }

    private void singleBarChart(BarChart chart) {
        assert driveData != null;
        ArrayList<Double> valueList = driveData.getAwarenessPercentageList();
        ArrayList<String> xAxisValues = driveData.getIndexList();

        buildBarChart(chart, valueList, xAxisValues);

    }

    private void multiBarChart(BarChart chart) {
        ArrayList<Double> valueList = new ArrayList<>();
        ArrayList<String> xAxisValues = new ArrayList<>();
        for (DriveData driveData : driveDataList) {
            valueList.add((double)driveData.getMeanAwarenessPercentage());
            xAxisValues.add(Integer.toString(driveData.getLen()));
        }
        buildBarChart(chart, valueList, xAxisValues);

    }

    private void buildBarChart(BarChart chart, ArrayList<Double> valueList, ArrayList<String> xAxisValues) {
        chart.getXAxis().setValueFormatter(
                new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(xAxisValues));


        //fit the data into a bar
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < valueList.size(); i++) {
            BarEntry barEntry = new BarEntry(i, valueList.get(i).floatValue());
            entries.add(barEntry);

        }
        BarDataSet barDataSet = new BarDataSet(entries, title);
        BarData data = new BarData(barDataSet);
        chart.setData(data);
        chart.getDescription().setEnabled(false);

        chart.invalidate();
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.animateY(1400, Easing.EaseInOutSine);
    }
}
