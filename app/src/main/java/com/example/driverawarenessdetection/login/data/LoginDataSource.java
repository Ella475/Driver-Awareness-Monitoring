package com.example.driverawarenessdetection.login.data;

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

    private boolean checkUserExists(String username) {
        Client client = Client.getInstance();
        try {
            HashMap<String, String> response =
                    client.sendGetRequest("users", new HashMap<String, String>() {{
                        put("username", username);
                    }});
            if (Objects.equals(response.get("response"), "false")) {
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
        return true;
    }

    public Result<LoggedInUser> register(String username, String password) {
        Client client = Client.getInstance();
        try {
            HashMap<String, String> response =
                    client.sendPostRequest("users", new HashMap<String, String>() {{
                        put("username", username);
                        put("password", password);
                    }});
            if (Objects.equals(response.get("response"), "false")) {
                return new Result.Error(new IOException("Error registering user"));
            } else {
                return new Result.Success(new LoggedInUser(
                        response.get("response"),
                        username));
            }
        } catch (Exception e) {
            return new Result.Error(new IOException("Error registering user", e));
        }
    }

    public Result<LoggedInUser> login(String username, String password) {
        Client client = Client.getInstance();
        try {
            HashMap<String, String> response =
                    client.sendGetRequest("users", new HashMap<String, String>() {{
                        put("username", username);
                        put("password", password);
                    }});
            if (Objects.equals(response.get("response"), "false")) {
                return new Result.Error(new IOException("Username already exists. Password incorrect!"));
            } else {
                return new Result.Success<>(new LoggedInUser(
                        response.get("response"),
                        username));
            }
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

}