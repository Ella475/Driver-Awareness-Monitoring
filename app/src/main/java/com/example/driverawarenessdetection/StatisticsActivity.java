package com.example.driverawarenessdetection;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.driverawarenessdetection.client.DriveData;
import com.example.driverawarenessdetection.client.DriveDataReceiver;
import com.example.driverawarenessdetection.client.SupervisorSenderReceiver;
import com.example.driverawarenessdetection.login.data.LoginType;
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
    private DriveDataReceiver driveDataReceiver;
    private int driveIndex = 0;
    private final String loginType = LoginType.getLoginType();
    private Runnable supervisorBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();

        if (loginType.equals("user")) {
            present_user();
        } else {
            presentSupervisor();
        }
    }

    private void present_user() {
        driveDataReceiver = new DriveDataReceiver();
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

    private void updateDropDownMenu(SupervisorSenderReceiver ssr) {
        Spinner userSpinner = findViewById(R.id.user_spinner);
        ssr.fetchSupervisedUsers();
        List<String> usernameList = ssr.getSupervisedUsersUsernames();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, usernameList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userSpinner.setAdapter(adapter);
    }

    private void presentSupervisor() {
        SupervisorSenderReceiver ssr = new SupervisorSenderReceiver();

        setContentView(R.layout.activity_statistics_supervisor);

        Spinner userSpinner = findViewById(R.id.user_spinner);
        EditText newUserEditText = findViewById(R.id.new_user_edit_text);
        Button confirmButton = findViewById(R.id.confirm_button);
        confirmButton.setEnabled(false);
        confirmButton.setAlpha(0.5f);
        Button addNewUserButton = findViewById(R.id.add_new_user_button);
        addNewUserButton.setEnabled(false);
        addNewUserButton.setAlpha(0.5f);

        // Populate the Spinner with a list of users
        updateDropDownMenu(ssr);

        // Handle user selection from the Spinner
        userSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                confirmButton.setEnabled(true);
                confirmButton.setAlpha(1f);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                confirmButton.setEnabled(false);
                confirmButton.setAlpha(0.5f);
            }
        });

        confirmButton.setOnClickListener(v -> {
            String selectedUsername = userSpinner.getSelectedItem().toString();
            List<String> usernames = ssr.getSupervisedUsersUsernames();
            List<String> ids = ssr.getSupervisedUsersIds();
            int index = usernames.indexOf(selectedUsername);
            String selectedUser = ids.get(index);

            driveDataReceiver = new DriveDataReceiver(selectedUser);
            driveDataList = driveDataReceiver.getDriveDataList();
            driveIndex = driveDataList.size() - 1;
            supervisorBackPressed = this::presentSupervisor;
            presentAllDrives();
        });

        // Handle text change in the EditText
        newUserEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addNewUserButton.setEnabled(true);
                addNewUserButton.setAlpha(1f);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Handle button click to add new user data
        addNewUserButton.setOnClickListener(v -> {
            String newUser = newUserEditText.getText().toString();
            boolean userExists = ssr.supervisedUserExists(newUser);
            if (!userExists) {
                Toast.makeText(StatisticsActivity.this, "User does not exist!", Toast.LENGTH_SHORT).show();
            } else {
                ssr.addSupervisedUser(newUser);
                updateDropDownMenu(ssr);
                Toast.makeText(StatisticsActivity.this, "User added!", Toast.LENGTH_SHORT).show();
                newUserEditText.setText("");
                addNewUserButton.setEnabled(false);
                addNewUserButton.setAlpha(0.5f);
                updateDropDownMenu(ssr);
            }
        });

        supervisorBackPressed = this::exit_dialog;

    }

    public void exit_dialog() {
        ConfirmationDialog dialog = new ConfirmationDialog();
        dialog.setMessage("Are you sure you want to exit?");
        dialog.setListener(this::finishAffinity);
        dialog.show(getSupportFragmentManager(), "ConfirmationDialog");
    }

    public void onBackPressed() {
        String loginType = LoginType.getLoginType();
        if (!loginType.equals("user")) {
            supervisorBackPressed.run();
        } else {
            super.onBackPressed();
        }
    }


}