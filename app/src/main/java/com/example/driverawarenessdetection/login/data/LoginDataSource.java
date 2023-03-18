package com.example.driverawarenessdetection.login.data;

import com.example.driverawarenessdetection.client.Client;
import com.example.driverawarenessdetection.client.HttpAsyncTask;
import com.example.driverawarenessdetection.login.data.model.LoggedInUser;
import com.example.driverawarenessdetection.login.ui.LoginType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> handleUser(String username, String password) {
        Client client = Client.getInstance();
        // check if username exists
        boolean userExists = client.checkUserExists(username);
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