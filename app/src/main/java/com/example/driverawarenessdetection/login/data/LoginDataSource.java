package com.example.driverawarenessdetection.login.data;

import com.example.driverawarenessdetection.client.Client;
import com.example.driverawarenessdetection.client.HttpAsyncTask;
import com.example.driverawarenessdetection.login.data.model.LoggedInUser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    private HashMap<String, String> response;

    public Result<LoggedInUser> handleUser(String username, String password) {
        Client client = Client.getInstance();
        // check if username exists
        boolean userExists = client.checkUserExists(username);
        if (userExists) {
            return client.login(username, password);
        } else {
            return client.register(username, password);
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }


}