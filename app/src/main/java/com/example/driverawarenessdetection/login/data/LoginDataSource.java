package com.example.driverawarenessdetection.login.data;

import com.example.driverawarenessdetection.client.HttpAsyncTask;
import com.example.driverawarenessdetection.login.data.model.LoggedInUser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    private volatile HashMap<String, String> response;

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

    private boolean checkUserExists(String username) {
        String endpoint = "users/";
        HashMap<String, String> payload = new HashMap<String, String>() {{
            put("username", username);
        }};

        String method = "GET";

        // Create a new HttpAsyncTask with the specified parameters
        HttpAsyncTask httpAsyncTask = new HttpAsyncTask(endpoint, payload, method,
                responseMap -> {
                    if (Objects.equals(responseMap.get("success"), "true")) {
                        this.response = responseMap;
                    } else {
                        this.response = null;
                    }
                });

        // Execute the HttpAsyncTask
        httpAsyncTask.execute();

        // Wait for the response
        while (this.response == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // if response is success and response is false, user does not exist
        if ((Objects.equals(this.response.get("success"), "true")) &&
                Objects.equals(this.response.get("response"), "false")) {
            System.out.println("User " + username + " does not exist");
            return false;
        }

        System.out.println("User " + username + " exists");
        return true;
    }

    public Result register(String username, String password) {
//        String url = "http://192.168.48.1:5000/users";
//        HashMap<String, String> payload = new HashMap<String, String>() {{
//            put("username", username);
//            put("password", password);
//        }};
//
//        String method = "POST";

        // Create a new HttpAsyncTask with the specified parameters
//        HttpAsyncTask httpAsyncTask = new HttpAsyncTask(url, payload, method, new HttpAsyncTask.OnResponseListener() {
//            @Override
//            public Result onResponse(HashMap<String, String> responseMap) {
//                // Handle the response from the server
//                if (Objects.equals(responseMap.get("response"), "false")) {
//                    return new Result.Error(new IOException("Error registering user"));
//                } else {
//                    return new Result.Success(new LoggedInUser(
//                            responseMap.get("response"),
//                            username));
//                }
//
//            }
//
//        });

        // Execute the HttpAsyncTask
//        return httpAsyncTask.execute();
        return new Result.Error<LoggedInUser>(new IOException("Error registering user"));
    }

    public Result login(String username, String password) {

//        String url = "http://192.168.48.1:5000/users";
//        HashMap<String, String> payload = new HashMap<String, String>() {{
//            put("username", username);
//            put("password", password);
//        }};
//
//        String method = "POST";
//
//        // Create a new HttpAsyncTask with the specified parameters
//        return new HttpAsyncTask(url, payload, method, new HttpAsyncTask.OnResponseListener() {
//            @Override
//            public Result onResponse(HashMap<String, String> responseMap) {
//                // Handle the response from the server
//                if (Objects.equals(response.get("response"), "false")) {
//                    return new Result.Error(new IOException("Username already exists. Password incorrect!"));
//                } else {
//                    return new Result.Success<>(new LoggedInUser(
//                            response.get("response"),
//                            username));
//
//                }
//
//            }).
//
//            execute();
//        }
        return new Result.Error<LoggedInUser>(new IOException("Error registering user"));
    }
}