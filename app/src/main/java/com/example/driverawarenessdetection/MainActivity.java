package com.example.driverawarenessdetection;

import android.content.Intent;
import android.os.Bundle;

import com.example.driverawarenessdetection.utils.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent switchWelcomeScreenIntent = new Intent(MainActivity.this,
                WelcomeScreenActivity.class);
        startActivity(switchWelcomeScreenIntent);
    }

    @Override
    public void onBackPressed() {
    }

}
