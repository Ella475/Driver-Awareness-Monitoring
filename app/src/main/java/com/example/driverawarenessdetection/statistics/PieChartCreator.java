package com.example.driverawarenessdetection.statistics;

import com.example.driverawarenessdetection.client.DriveData;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;


import java.util.ArrayList;

public class PieChartCreator extends ChartCreator{

    public PieChartCreator(DriveData driveData, String title) {
        super(driveData, title);
    }

    @Override
    public void initChart(View viewChart) {
        PieChart chart = (PieChart) viewChart;

        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);

        chart.setDragDecelerationFrictionCoef(0.95f);


        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(65f);
        chart.setTransparentCircleRadius(70f);

        chart.setCenterText(title);
        chart.setDrawCenterText(true);
        chart.setCenterTextSize(18);
        // make bold
        chart.setCenterTextTypeface(Typeface.DEFAULT_BOLD);

        chart.setRotationAngle(45);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);

        chart.animateY(1400, Easing.EaseInOutQuad);

        chart.getLegend().setEnabled(false);

        // entry label styling
        chart.setEntryLabelColor(Color.BLACK);
        chart.setEntryLabelTextSize(12f);

        loadPieChartData(chart);
    }

    private void loadPieChartData(PieChart chart) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        // loop through the driveDataList and count the number of times each category appears:
        // asleep and inattentive, asleep and attentive, awake and inattentive, awake and attentive
        float asleepAndInattentive = 0;
        float asleepAndAttentive = 0;
        float awakeAndInattentive = 0;
        float awakeAndAttentive = 0;

        ArrayList<Boolean> asleepList = driveData.getAsleepList();
        ArrayList<Boolean> inattentiveList = driveData.getInattentiveList();
        for (int i = 0; i < asleepList.size(); i++) {
            if (asleepList.get(i)){
                if (inattentiveList.get(i)){
                    asleepAndInattentive++;
                } else {
                    asleepAndAttentive++;
                }
            } else {
                if (inattentiveList.get(i)){
                    awakeAndInattentive++;
                } else {
                    awakeAndAttentive++;
                }
            }
        }

        // create new values for each category
        float total = asleepAndInattentive + asleepAndAttentive + awakeAndInattentive + awakeAndAttentive;
        asleepAndInattentive = asleepAndInattentive / total;
        asleepAndAttentive =  asleepAndAttentive / total;
        awakeAndInattentive = awakeAndInattentive / total;
        awakeAndAttentive = awakeAndAttentive / total;


        // add the entries to the pie chart
        if (asleepAndInattentive != 0)
            entries.add(new PieEntry(asleepAndInattentive, "Asleep & Distracted"));
        if (asleepAndAttentive != 0)
            entries.add(new PieEntry(asleepAndAttentive, "Asleep & Attentive"));
        if (awakeAndInattentive != 0)
            entries.add(new PieEntry(awakeAndInattentive, "Awake & Distracted"));
        if (awakeAndAttentive != 0)
            entries.add(new PieEntry(awakeAndAttentive, "Awake & Attentive"));


        ArrayList<Integer> colors = new ArrayList<>();
        for (int color : ColorTemplate.MATERIAL_COLORS) {
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, title);
        dataSet.setValueTextSize(12f);

        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        chart.setData(data);

        // undo all highlights
        chart.highlightValues(null);
        chart.invalidate();
    }
}
