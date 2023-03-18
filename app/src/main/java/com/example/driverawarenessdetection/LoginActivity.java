package com.example.driverawarenessdetection;

import android.annotation.SuppressLint;
import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.driverawarenessdetection.login.data.LoginDataSource;
import com.example.driverawarenessdetection.login.data.LoginRepository;
import com.example.driverawarenessdetection.login.ui.LoginType;
import com.example.driverawarenessdetection.login.ui.LoginViewModel;
import com.example.driverawarenessdetection.utils.BaseActivity;
import com.example.driverawarenessdetection.utils.ConfirmationDialog;

import java.util.Objects;

public class LoginActivity extends BaseActivity {

    private LoginViewModel loginViewModel;
    private final String loginType = LoginType.getLoginType();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        findViewById(R.id.container).getRootView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Objects.requireNonNull(getSupportActionBar()).hide();

        handleLoginType();

        loginViewModel = new LoginViewModel(LoginRepository.getInstance(new LoginDataSource()));

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);

        loginViewModel.getLoginFormState().observe(this, loginFormState -> {
            if (loginFormState == null) {
                return;
            }
            boolean isDataValid = loginFormState.isDataValid();
            if (isDataValid) {
                loginButton.setEnabled(true);
                loginButton.setBackgroundTintMode(null);
            }
            if (loginFormState.getUsernameError() != null) {
                usernameEditText.setError(getString(loginFormState.getUsernameError()));
            }
            if (loginFormState.getPasswordError() != null) {
                passwordEditText.setError(getString(loginFormState.getPasswordError()));
            }
        });

        loginViewModel.getLoginResult().observe(this, loginResult -> {
            if (loginResult == null) {
                return;
            }
            if (loginResult.getError() != null) {
                showLoginFailed(loginResult.getError());
                return;
            }
            if (loginResult.getSuccess() != null) {
                if (loginType == "user") {
                    updateUiWithUser(loginResult.getSuccess());
                } else {
                    updateUiWithUser("Supervisor");
                }
            }
            setResult(Activity.RESULT_OK);

            String loginType = LoginType.getLoginType();
            if (!Objects.equals(loginType, "user")) {
                Intent switchStatisticsActivityIntent = new Intent(LoginActivity.this,
                        StatisticsActivity.class);
                startActivity(switchStatisticsActivityIntent);
            } else {
                Intent switchMainScreenIntent = new Intent(LoginActivity.this,
                        MainScreenActivity.class);
                startActivity(switchMainScreenIntent);
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);

        loginButton.setOnClickListener(v -> {
            boolean userExist = loginViewModel.userExists(usernameEditText.getText().toString());
            if (userExist || !Objects.equals(loginType, "user")) {
                loginViewModel.handleUser(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString(), userExist);
            } else {
                ConfirmationDialog dialog = new ConfirmationDialog();
                dialog.setMessage("Are you sure you want to register?");
                dialog.setListener(() -> loginViewModel.handleUser(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString(), false));
                dialog.show(getSupportFragmentManager(), "ConfirmationDialog");
            }
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void handleLoginType() {
        ImageView hello = findViewById(R.id.hello);
        Button login = findViewById(R.id.login);

        if (loginType == "user") {
            hello.setBackground(getResources().getDrawable(R.drawable.user_logo));
            login.setText(R.string.action_sign_in);
        } else {
            hello.setBackground(getResources().getDrawable(R.drawable.superviser_logo));
            login.setText(R.string.action_sign_in_short);
        }
    }

    private void updateUiWithUser(String name) {
        String welcome = getString(R.string.welcome) + name + "!";
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(String errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    public void onBackPressed() {
        ConfirmationDialog dialog = new ConfirmationDialog();
        dialog.setMessage("Are you sure you want to exit?");
        dialog.setListener(this::finishAffinity);
        dialog.show(getSupportFragmentManager(), "ConfirmationDialog");
    }
}