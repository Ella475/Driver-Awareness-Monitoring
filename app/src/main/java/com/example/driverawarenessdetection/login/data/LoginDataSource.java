package com.example.driverawarenessdetection.login.data;

import android.os.Bundle;

import androidx.fragment.app.FragmentManager;

import com.example.driverawarenessdetection.client.Client;
import com.example.driverawarenessdetection.login.data.model.LoggedInUser;
import com.example.driverawarenessdetection.login.ui.LoginType;
import com.example.driverawarenessdetection.utils.ConfirmationDialog;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public boolean checkUserExists(String username) {
        Client client = Client.getInstance();
        return client.checkUserExists(username);
    }

    public Result<LoggedInUser> handleUser(String username, String password, boolean userExists) {
        Client client = Client.getInstance();
        String loginType = LoginType.getLoginType();

        if (userExists) {
            Result result = client.login(username, password);
            if (loginType.equals("user") | result instanceof Result.Success) {
                return result;
            } else {
                return new Result.Error(new IOException("Incorrect password"));
            }
        } else {
            if (!loginType.equals("user")) {
                return new Result.Error(new IOException("User does not exist"));
            } else {
                return client.register(username, password);
            }
        }
    }
}