package com.example.driverawarenessdetection.statistics;

import android.os.Build;
import android.view.View;

import com.example.driverawarenessdetection.client.DriveData;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.Comparator;

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
        // sort the driveDataList by length
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            driveDataList.sort(Comparator.comparingInt(DriveData::getLen));
        }


        for (DriveData driveData : driveDataList) {
            valueList.add((double)driveData.getMeanAwarenessPercentage());
            float minutes = Math.round(100 * driveData.getLen() / 30f) / 100f;
            xAxisValues.add(Float.toString(minutes));
        }
        buildBarChart(chart, valueList, xAxisValues);

    }

    private void buildBarChart(BarChart chart, ArrayList<Double> valueList, ArrayList<String> xAxisValues) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < valueList.size(); i++) {
            BarEntry barEntry = new BarEntry(i, valueList.get(i).floatValue());
            entries.add(barEntry);

        }
        BarDataSet barDataSet = new BarDataSet(entries, title);
        barDataSet.setDrawValues(false);
        BarData data = new BarData(barDataSet);
        chart.setData(data);
        chart.getDescription().setEnabled(false);
        chart.invalidate();
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.animateY(1400, Easing.EaseInOutSine);

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setAxisMinimum(0);
        yAxis.setAxisMaximum(100);
        yAxis.setGranularity(10);
        yAxis.setGranularityEnabled(true);
        yAxis.setLabelCount(11);
        yAxis.setDrawGridLines(true);
        yAxis.setDrawAxisLine(true);
        yAxis.setDrawLabels(true);
        chart.getAxisRight().setEnabled(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawLabels(true);
        xAxis.setGranularity(1);

        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisValues));
    }
}
