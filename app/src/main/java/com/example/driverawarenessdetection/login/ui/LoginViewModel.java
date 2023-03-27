package com.example.driverawarenessdetection.login.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.example.driverawarenessdetection.login.data.LoginRepository;
import com.example.driverawarenessdetection.login.data.Result;
import com.example.driverawarenessdetection.login.data.LoggedInUser;
import com.example.driverawarenessdetection.R;

public class LoginViewModel extends ViewModel {

    private final MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private final MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private final LoginRepository loginRepository;

    public LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    public LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    public LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void handleUser(String username, String password, boolean userExists) {
        Result result = loginRepository.handleUser(username, password, userExists);

        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
        } else {
            loginResult.setValue(new LoginResult(result.toString()));
        }
    }

    public boolean userExists(String username) {
        return loginRepository.userExists(username);
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}