package com.example.driverawarenessdetection;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

public class StatisticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        BarChart barChart = findViewById(R.id.severityBarChart);
        ArrayList<Double> valueList = new ArrayList<Double>();
        ArrayList<BarEntry> entries = new ArrayList<>();
        String title = " Avg Obtain Marks";
        ArrayList<String> xAxisValues = new ArrayList<String>();
        xAxisValues.add("2001");
        xAxisValues.add("2002");
        xAxisValues.add("2003");
        xAxisValues.add("2004");
        xAxisValues.add("2005");
        xAxisValues.add("2006");
        xAxisValues.add("2007");
        xAxisValues.add("2008");
        xAxisValues.add("2009");
        xAxisValues.add("2010");
        xAxisValues.add("2011");
        xAxisValues.add("2012");
        xAxisValues.add("2013");
        xAxisValues.add("2014");
        xAxisValues.add("2015");

        barChart.getXAxis().setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(xAxisValues));

        //input data
        valueList.add(20.1);
        valueList.add(30.1);
        valueList.add(50.1);
        valueList.add(30.1);
        valueList.add(60.1);
        valueList.add(60.1);
        valueList.add(60.1);
        valueList.add(60.1);
        valueList.add(65.1);
        valueList.add(60.1);
        valueList.add(55.1);
        valueList.add(60.1);
        valueList.add(50.1);

        //fit the data into a bar
        for (int i = 0; i < valueList.size(); i++) {
            BarEntry barEntry = new BarEntry(i, valueList.get(i).floatValue());
            entries.add(barEntry);

        }
        BarDataSet barDataSet = new BarDataSet(entries, title);
        BarData data = new BarData(barDataSet);
        barChart.setData(data);
        barChart.invalidate();
//         barChart.animateXY(2000, 2000);
        // llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.animateY(1400, Easing.EaseInOutSine);
    }
}