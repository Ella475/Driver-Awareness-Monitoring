package com.example.driverawarenessdetection.login.data;

import com.example.driverawarenessdetection.login.data.model.LoggedInUser;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {

        try {
            // TODO: handle loggedInUser authentication
            LoggedInUser newUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            username);
            // TODO: save credentials
            return new Result.Success<>(newUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}