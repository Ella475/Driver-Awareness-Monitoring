package com.example.driverawarenessdetection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;

import com.example.driverawarenessdetection.login.data.LoginDataSource;
import com.example.driverawarenessdetection.login.data.LoginRepository;
import com.example.driverawarenessdetection.login.ui.LoginType;

import java.util.Objects;

public class WelcomeScreen extends AppCompatActivity {

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
                Intent switchLoginActivityIntent = new Intent(WelcomeScreen.this, LoginActivity.class);
                startActivity(switchLoginActivityIntent);
            }
        });
        Button admin = findViewById(R.id.button_top);
        admin.setOnClickListener(v -> {
            LoginType.setLoginType("admin");
            LoginRepository loginRepository = LoginRepository.getInstance(new LoginDataSource());
            if (!loginRepository.isLoggedIn()) {
                Intent switchLoginActivityIntent = new Intent(WelcomeScreen.this, LoginActivity.class);
                startActivity(switchLoginActivityIntent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}