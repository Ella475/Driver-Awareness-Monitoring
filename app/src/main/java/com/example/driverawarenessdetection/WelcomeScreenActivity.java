package com.example.driverawarenessdetection;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Objects.requireNonNull(getSupportActionBar()).hide();


        Button user = findViewById(R.id.button_bottom);
        user.setOnClickListener(v -> {
            LoginType.setLoginType("user");
            LoginRepository loginRepository = LoginRepository.getInstance(new LoginDataSource());
            if (!loginRepository.isLoggedIn()) {
                Intent switchLoginActivityIntent = new Intent(WelcomeScreenActivity.this, LoginActivity.class);
                startActivity(switchLoginActivityIntent);
            }
        });
        Button admin = findViewById(R.id.button_top);
        admin.setOnClickListener(v -> {
            LoginType.setLoginType("admin");
            LoginRepository loginRepository = LoginRepository.getInstance(new LoginDataSource());
            if (!loginRepository.isLoggedIn()) {
                Intent switchLoginActivityIntent = new Intent(WelcomeScreenActivity.this, LoginActivity.class);
                startActivity(switchLoginActivityIntent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        ConfirmationDialog dialog = new ConfirmationDialog();
        dialog.setMessage("Are you sure you want to exit?");
        dialog.setListener(this::finishAffinity);
        dialog.show(getSupportFragmentManager(), "ConfirmationDialog");
    }
}