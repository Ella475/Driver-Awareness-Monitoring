package com.example.driverawarenessdetection.login.data;

import android.util.Log;

import com.example.driverawarenessdetection.client.HttpAsyncTask;
import com.example.driverawarenessdetection.login.data.model.LoggedInUser;
import com.example.driverawarenessdetection.client.Client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> handleUser(String username, String password) {
        // check if username exists
        boolean userExists = checkUserExists(username);
        if (userExists) {
            return login(username, password);
        } else {
            return register(username, password);
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }

    private Result checkUserExists(String username) {
        String url = "http://127.0.0.1/users";
        HashMap<String, String> payload = new HashMap<String, String>() {{
            put("username", username);
        }};

        String method = "POST";

        // Create a new HttpAsyncTask with the specified parameters
        HttpAsyncTask httpAsyncTask = new HttpAsyncTask(url, payload, method, new HttpAsyncTask.OnResponseListener() {
            @Override
            public Result onResponse(HashMap<String, String> responseMap) {
                // Handle the response from the server
                if (Objects.equals(responseMap.get("response"), "false")) {
                    return new Result.Error(new IOException("Error registering user"));
                } else {
                    return new Result.Success(new LoggedInUser(
                            responseMap.get("response"),
                            username));
                }

            }

        });

        // Execute the HttpAsyncTask
      return httpAsyncTask.execute();

    }

    public Result<LoggedInUser> register(String username, String password) {
        String url = "http://192.168.48.1:5000/users";
        HashMap<String, String> payload = new HashMap<String, String>() {{
            put("username", username);
            put("password", password);
        }};

        String method = "POST";

        // Create a new HttpAsyncTask with the specified parameters
        HttpAsyncTask httpAsyncTask = new HttpAsyncTask(url, payload, method, new HttpAsyncTask.OnResponseListener() {
            @Override
            public Result onResponse(HashMap<String, String> responseMap) {
                // Handle the response from the server
                if (Objects.equals(responseMap.get("response"), "false")) {
                    return new Result.Error(new IOException("Error registering user"));
                } else {
                    return new Result.Success(new LoggedInUser(
                            responseMap.get("response"),
                            username));
                }

            }

        });

        // Execute the HttpAsyncTask
        return httpAsyncTask.execute();

    }

    public Result<LoggedInUser> login(String username, String password) {

        String url = "http://192.168.48.1:5000/users";
        HashMap<String, String> payload = new HashMap<String, String>() {{
            put("username", username);
            put("password", password);
        }};

        String method = "POST";

        // Create a new HttpAsyncTask with the specified parameters
        return new HttpAsyncTask(url, payload, method, new HttpAsyncTask.OnResponseListener() {
            @Override
            public Result onResponse(HashMap<String, String> responseMap) {
                // Handle the response from the server
                if (Objects.equals(response.get("response"), "false")) {
                    return new Result.Error(new IOException("Username already exists. Password incorrect!"));
                } else {
                    return new Result.Success<>(new LoggedInUser(
                            response.get("response"),
                            username));

            }

        }).execute();
        }

}