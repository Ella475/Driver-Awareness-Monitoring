package com.example.driverawarenessdetection;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;

import com.example.driverawarenessdetection.login.data.LoginDataSource;
import com.example.driverawarenessdetection.login.data.LoginRepository;
import com.example.driverawarenessdetection.login.ui.LoginType;
import com.example.driverawarenessdetection.utils.BaseActivity;
import com.example.driverawarenessdetection.utils.ConfirmationDialog;

import java.util.Objects;

public class WelcomeScreenActivity extends BaseActivity {
    final int CAMERA_REQUEST_CODE = 1001;
    final int INTERNET_REQUEST_CODE = 1002;
    final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1003;
    final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 1004;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Objects.requireNonNull(getSupportActionBar()).hide();

        grantPermissions();

        Button user = findViewById(R.id.button_bottom);
        user.setOnClickListener(v -> {
            LoginType.setLoginType("user");
            LoginRepository loginRepository = LoginRepository.getInstance(new LoginDataSource());
            Intent switchLoginActivityIntent = new Intent(WelcomeScreenActivity.this, LoginActivity.class);
            startActivity(switchLoginActivityIntent);
        });
        Button admin = findViewById(R.id.button_top);
        admin.setOnClickListener(v -> {
            LoginType.setLoginType("admin");
            Intent switchLoginActivityIntent = new Intent(WelcomeScreenActivity.this, LoginActivity.class);
            startActivity(switchLoginActivityIntent);
        });
    }

    @Override
    public void onBackPressed() {
        ConfirmationDialog dialog = new ConfirmationDialog();
        dialog.setMessage("Are you sure you want to exit?");
        dialog.setListener(this::finishAffinity);
        dialog.show(getSupportFragmentManager(), "ConfirmationDialog");
    }


    public void grantPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
            }
            if (checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.INTERNET}, INTERNET_REQUEST_CODE);
            }
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
            }
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_REQUEST_CODE);
            }
        }
    }
}