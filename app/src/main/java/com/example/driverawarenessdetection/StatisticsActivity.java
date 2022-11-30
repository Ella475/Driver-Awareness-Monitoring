package com.example.driverawarenessdetection;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class StatisticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        initLineChart();
        initBarChart();


    }

    private void initBarChart() {
        BarChart barChart = findViewById(R.id.bar_chart_graph);
        ArrayList<Double> valueList = new ArrayList<>();
        ArrayList<BarEntry> entries = new ArrayList<>();
        String title = " Avg Obtain Marks";
        ArrayList<String> xAxisValues = new ArrayList<>();
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
        // barChart.animateXY(2000, 2000);
        // llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.animateY(1400, Easing.EaseInOutSine);

    }

    private void initLineChart() {
        LineChart mChart = findViewById(R.id.line_chart_graph);
        ArrayList<String> xAxisValues = new ArrayList<String>();
        xAxisValues.add("Term1");
        xAxisValues.add("Term2");
        xAxisValues.add("Term3");
        xAxisValues.add("Term4");
        xAxisValues.add("Term5");
        xAxisValues.add("Term6");

        mChart.getXAxis().setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(xAxisValues));


        ArrayList<Entry> entries = new ArrayList<>();

        entries.add(new Entry(0, 60));//20    2018f  (2018f, 20))
        entries.add(new Entry(1, 57));//40    2019f   (2019f, 40))
        entries.add(new Entry(2, 65));//60    2020f (2020f, 60))
        entries.add(new Entry(3, 70));//80    2021f (2021f, 80))
        entries.add(new Entry(4, 80));//80    2021f (2021f, 80))
        entries.add(new Entry(5, 70));//80    2021f (2021f, 80))


        ArrayList<Entry> entry = new ArrayList<>();
        entry.add(new Entry(0, 70));
        entry.add(new Entry(1, 50));
        entry.add(new Entry(2, 60));
        entry.add(new Entry(3, 65));
        entry.add(new Entry(4, 75));
        entry.add(new Entry(5, 80));

        ArrayList<Entry> marathi = new ArrayList<>();
        marathi.add(new Entry(0, 80));
        marathi.add(new Entry(1, 70));
        marathi.add(new Entry(2, 50));
        marathi.add(new Entry(3, 68));
        marathi.add(new Entry(4, 55));
        marathi.add(new Entry(5, 36));


        ArrayList<LineDataSet> lines = new ArrayList<LineDataSet>();

        LineDataSet set1 = new LineDataSet(entries, "English");
        set1.setDrawFilled(true);
        set1.setFillColor(Color.WHITE);
        set1.setColor(Color.RED);
        set1.setCircleColor(Color.DKGRAY);
        lines.add(set1);

        LineDataSet set2 = new LineDataSet(entry, "Hindi");
        set2.setDrawFilled(true);
        set2.setFillColor(Color.WHITE);
        set2.setColor(Color.BLUE);
        set2.setCircleColor(Color.RED);
        lines.add(set2);

        LineDataSet set3 = new LineDataSet(marathi, "Marathi");
        set3.setDrawFilled(true);
        set3.setFillColor(Color.WHITE);
        set3.setColor(Color.YELLOW);
        set3.setCircleColor(Color.parseColor("#8B0000"));
        lines.add(set3);


        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        dataSets.add(set2);
        dataSets.add(set3);

        mChart.setData(new LineData(dataSets));

        mChart.getDescription().setText("");

        mChart.getDescription().setTextColor(Color.RED);


        mChart.animateY(1400, Easing.EaseInOutBounce);
    }
}