package com.example.driverawarenessdetection.login.data;

import com.example.driverawarenessdetection.client.Client;
import com.example.driverawarenessdetection.login.data.model.LoggedInUser;
import com.example.driverawarenessdetection.login.ui.LoginType;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    public boolean checkUserExists(String username) {
        boolean isUser = (LoginType.getLoginType()).equals("user");
        Client client = Client.getInstance();
        return client.checkUserExists(username, isUser);
    }

    public Result<LoggedInUser> handleUser(String username, String password, boolean userExists) {
        Client client = Client.getInstance();
        boolean isUser = (LoginType.getLoginType()).equals("user");

        if (userExists) {
            Result result = client.login(username, password, isUser);
            if (result instanceof Result.Success) {
                return result;
            } else {
                return new Result.Error(new IOException("Incorrect password"));
            }
        } else {
            return client.register(username, password, isUser);
        }
    }
}