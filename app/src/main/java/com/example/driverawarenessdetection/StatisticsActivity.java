package com.example.driverawarenessdetection;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.driverawarenessdetection.client.DriveData;
import com.example.driverawarenessdetection.client.DriveDataReceiver;
import com.example.driverawarenessdetection.login.ui.LoginType;
import com.example.driverawarenessdetection.statistics.BarChartCreator;
import com.example.driverawarenessdetection.statistics.ChartCreator;
import com.example.driverawarenessdetection.statistics.MultiLineChartCreator;
import com.example.driverawarenessdetection.statistics.PieChartCreator;
import com.example.driverawarenessdetection.utils.BaseActivity;
import com.example.driverawarenessdetection.utils.ConfirmationDialog;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.BarChart;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StatisticsActivity extends BaseActivity {
    private ArrayList<DriveData> driveDataList;
    private int driveIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();

        DriveDataReceiver driveDataReceiver = new DriveDataReceiver();
        driveDataList = driveDataReceiver.getDriveDataList();
        driveIndex = driveDataList.size() - 1;

        presentAllDrives();
    }

    private void presentSingleDrive() {
        setContentView(R.layout.activity_statistics_1);

        ImageView leftArrow = findViewById(R.id.left_button);
        ImageView rightArrow = findViewById(R.id.right_button);

        if (driveIndex == 0) {
            rightArrow.setImageResource(R.drawable.right_disabeled);
            rightArrow.setEnabled(false);
        } else {
            rightArrow.setImageResource(R.drawable.right_enabeled);
            rightArrow.setEnabled(true);
        }


        DriveData driveData = driveDataList.get(driveIndex);

        BarChart barChart = findViewById(R.id.bar_chart_graph);
        String barChartTitle = "Awareness Percentage vs. Time";

        ChartCreator barChartCreator = new BarChartCreator(driveData, barChartTitle);
        barChartCreator.initChart(barChart);

        PieChart pieChart = findViewById(R.id.pie_chart_graph);
        String pieChartTitle = "Awareness \n Analysis";

        ChartCreator pieChartCreator = new PieChartCreator(driveData, pieChartTitle);
        pieChartCreator.initChart(pieChart);

        TextView title = findViewById(R.id.title);
        String drive = getString(R.string.drive) + " " + (driveIndex + 1);
        title.setText(drive);

        TextView time = findViewById(R.id.time);
        String driveTime = driveData.getDriveTime();
        String[] driveTimeList = driveTime.split(" ");
        driveTime = driveTimeList[1] + " " + driveTimeList[0];
        time.setText(driveTime);

        leftArrow.setOnClickListener(v -> {
            if (driveIndex < driveDataList.size() - 1) {
                driveIndex++;
                presentSingleDrive();
            } else {
                presentAllDrives();
            }
        });

        rightArrow.setOnClickListener(v -> {
            if (driveIndex > 0) {
                driveIndex--;
                presentSingleDrive();
            }
        });
    }

    private void presentAllDrives() {
        setContentView(R.layout.activity_statistics_2);

        View chart = findViewById(R.id.chart_graph_mean);
        String chartTitleMean = "Awareness Percentage mean vs. Drive time [m]";

        ChartCreator chartMeanCreator = new BarChartCreator(driveDataList, chartTitleMean);
        chartMeanCreator.initChart(chart);

        LineChart lineChart = findViewById(R.id.line_chart_graph);
        String lineChartTitle = "Awareness Percentage vs. Time";

        ChartCreator lineChartCreator = new MultiLineChartCreator(driveDataList, lineChartTitle);
        lineChartCreator.initChart(lineChart);

        TextView title = findViewById(R.id.title);
        title.setText(R.string.all_drives);

        ImageView rightArrow = findViewById(R.id.right_button);
        rightArrow.setOnClickListener(v -> presentSingleDrive());
    }

    public void onBackPressed() {
        String loginType = LoginType.getLoginType();
        if (!loginType.equals("user")) {
            ConfirmationDialog dialog = new ConfirmationDialog();
            dialog.setMessage("Are you sure you want to exit?");
            dialog.setListener(this::finishAffinity);
            dialog.show(getSupportFragmentManager(), "ConfirmationDialog");
        } else {
            super.onBackPressed();
        }
    }
}