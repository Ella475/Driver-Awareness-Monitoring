package com.example.driverawarenessdetection;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.example.driverawarenessdetection.client.DriveData;
import com.example.driverawarenessdetection.client.DriveDataPoint;
import com.example.driverawarenessdetection.client.DriveDataReceiver;
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

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StatisticsActivity extends AppCompatActivity {
    private DriveDataReceiver driveDataReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        Objects.requireNonNull(getSupportActionBar()).hide();

        this.driveDataReceiver = new DriveDataReceiver();
        try {
            driveDataReceiver.receiveDriveData();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        initLineChart();
        initBarChart();
    }

    private void initBarChart() {
        List<DriveDataPoint> driveDataList = driveDataReceiver.getDriveData().getDriveDataList();
        BarChart barChart = findViewById(R.id.bar_chart_graph);
        ArrayList<Double> valueList = new ArrayList<>();
        ArrayList<BarEntry> entries = new ArrayList<>();
        String title = "Awareness Percentage";
        ArrayList<String> xAxisValues = new ArrayList<>();


        // loop through the driveDataList and add awarenessPercentage to valueList
        int i = 0;
        for (DriveDataPoint driveDataPoint : driveDataList) {
            valueList.add((double) driveDataPoint.getAwarenessPercentage());
            i++;
            xAxisValues.add(Integer.toString(i));
        }


        barChart.getXAxis().setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(xAxisValues));


        //fit the data into a bar
        for (i = 0; i < valueList.size(); i++) {
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