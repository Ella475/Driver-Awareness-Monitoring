package com.example.driverawarenessdetection;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.driverawarenessdetection.client.DriveData;
import com.example.driverawarenessdetection.client.DriveDataReceiver;
import com.example.driverawarenessdetection.statistics.BarChartCreator;
import com.example.driverawarenessdetection.statistics.ChartCreator;
import com.example.driverawarenessdetection.statistics.MultiLineChartCreator;
import com.example.driverawarenessdetection.statistics.PieChartCreator;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.BarChart;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Objects;

public class StatisticsActivity extends AppCompatActivity {
    private ArrayList<DriveData> driveDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();

        DriveDataReceiver driveDataReceiver = new DriveDataReceiver();
        driveDataList = driveDataReceiver.getDriveDataList();

        presentAllDrives();
    }

    private void presentSingleDrive() {
        setContentView(R.layout.activity_statistics_1);
        DriveData driveData = driveDataList.get(0);

        BarChart barChart = findViewById(R.id.bar_chart_graph);
        String barChartTitle = "Awareness Percentage vs. Time";

        ChartCreator barChartCreator = new BarChartCreator(driveData, barChartTitle);
        barChartCreator.initChart(barChart);

        PieChart pieChart = findViewById(R.id.pie_chart_graph);
        String pieChartTitle = "Awareness \n Analysis";

        ChartCreator pieChartCreator = new PieChartCreator(driveData, pieChartTitle);
        pieChartCreator.initChart(pieChart);
    }

    private void presentAllDrives() {
        setContentView(R.layout.activity_statistics_2);

        BarChart barChart = findViewById(R.id.bar_chart_graph);
        String barChartTitle = "Awareness Percentage mean vs. Drive time";

        ChartCreator barChartCreator = new BarChartCreator(driveDataList, barChartTitle);
        barChartCreator.initChart(barChart);

        LineChart lineChart = findViewById(R.id.line_chart_graph);
        String lineChartTitle = "Awareness Percentage vs. Time";

        ChartCreator lineChartCreator = new MultiLineChartCreator(driveDataList, lineChartTitle);
        lineChartCreator.initChart(lineChart);
    }

//    private void presentBarChart() {
//        List<DriveDataPoint> driveDataList = driveDataReceiver.getLasDriveData().getDriveDataList();
//        BarChart barChart = findViewById(R.id.bar_chart_graph);
//        String title = "Awareness Percentage";
//
//        BarChartCreator barChartCreator = new BarChartCreator(driveDataList, title);
//        barChartCreator.initBarChart(barChart);
//
//    }

//    private void presentLineChart() {
//        List<DriveDataPoint> driveDataList = driveDataReceiver.getDriveData().getDriveDataList();
//        LineChart mChart = findViewById(R.id.line_chart_graph);
//        String title = "Awareness Percentage";
//
//        LineChartCreator lineChartCreator = new LineChartCreator(driveDataList, title);
//        lineChartCreator.initLineChart(mChart);
//
//    }

//    private void presentPieChart() {
//        List<DriveDataPoint> driveDataList = driveDataReceiver.getLasDriveData().getDriveDataList();
//        PieChart pieChart = findViewById(R.id.pie_chart_graph);
//        String title = "Awareness Percentage";
//
//        PieChartCreator pieChartCreator = new PieChartCreator(driveDataList, title);
//        pieChartCreator.initPieChart(pieChart);
//
//    }
}