package com.example.driverawarenessdetection.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import com.example.driverawarenessdetection.login.data.Result;
import com.example.driverawarenessdetection.login.data.model.LoggedInUser;


public class Client {

    private static Client instance = null;

    private Client() {
        // Private constructor to prevent instantiation from outside
    }

    public static Client getInstance() {
        if (instance == null) {
            instance = new Client();
        }
        return instance;
    }

    private HttpAsyncTask sendAsyncTask(String endpoint, HashMap<String, String> payload, String method) {
        // Create a new HttpAsyncTask with the specified parameters
        HttpAsyncTask httpAsyncTask = new HttpAsyncTask(endpoint, payload, method, responseMap -> {});

        // Execute the HttpAsyncTask
        httpAsyncTask.execute();
        return httpAsyncTask;
    }

    private HashMap<String, String> getResponse(HttpAsyncTask httpAsyncTask) {
        HashMap<String, String> response = new HashMap<>();
        // Wait for the response
        try {
            response = httpAsyncTask.get();
            // Do something with the response
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return response;
    }

    public boolean checkUserExists(String username){
        String endpoint = "users";
        HashMap<String, String> payload = new HashMap<String, String>() {{
            put("username", username);
        }};

        String method = "GET";

        HashMap<String, String> response = getResponse(sendAsyncTask(endpoint, payload, method));

        // if response is success and response is false, user does not exist
        if ((Objects.equals(response.get("success"), "true")) &&
                Objects.equals(response.get("response"), "false")) {
            System.out.println("User " + username + " does not exist");
            return false;
        }

        System.out.println("User " + username + " exists");
        return true;

    }

    public Result register(String username, String password){
        String endpoint = "users";
        HashMap<String, String> payload = new HashMap<String, String>() {{
            put("username", username);
            put("password", password);
        }};

        String method = "POST";

        HashMap<String, String> response = getResponse(sendAsyncTask(endpoint, payload, method));

        // if  we succeeded and response is not false, user was created
        if ((Objects.equals(response.get("success"), "true")) &&
                !Objects.equals(response.get("response"), "false")) {
            System.out.println("User " + username + " was created successfully!");
            LoggedInUser user = new LoggedInUser(
                    response.get("response"),
                    username);
            return new Result.Success(user);
        }
        System.out.println("User " + username + " failed to register");
        return new Result.Error(new IOException("Error registering user"));

    }

    public Result login (String username, String password){
        String endpoint = "users";
        HashMap<String, String> payload = new HashMap<String, String>() {{
            put("username", username);
            put("password", password);
        }};

        String method = "GET";

        HashMap<String, String> response = getResponse(sendAsyncTask(endpoint, payload, method));

        // if  we succeeded and response is not false, allow login
        if ((Objects.equals(response.get("success"), "true")) &&
                !Objects.equals(response.get("response"), "false")) {
            System.out.println("User " + username + " logged in successfully!");
            LoggedInUser user = new LoggedInUser(
                    response.get("response"),
                    username);
            return new Result.Success(user);
        }
        System.out.println("User " + username + " failed to login");
        return new Result.Error(new IOException("Username already exists. Password incorrect!"));

    }

    public String startDrive(String userId) {
    String endpoint = "drives";
        HashMap<String, String> payload =  new HashMap<String, String>() {{
            put("user_id", userId);
        }};

        String method = "POST";

        HashMap<String, String> response = getResponse(sendAsyncTask(endpoint, payload, method));

        // if we succeeded and response is not false, return drive id
        if ((Objects.equals(response.get("success"), "true")) &&
                !Objects.equals(response.get("response"), "false")) {
            String drive_id = response.get("response");
            System.out.println("Started drive " + drive_id + "for user " + userId);
            return drive_id;
        }
        System.out.println("Drive failed to start");
        return null;
    }


    public void sendDriveData(String drive_id, int awarenessPercentage, boolean asleep,
                              boolean inattentive){
        String endpoint = "drives_data";

        HashMap<String, String> payload = new HashMap<String, String>() {{
            put("drive_id", drive_id);
            put("awareness_percentage", Integer.toString(awarenessPercentage));
            put("asleep", Boolean.toString(asleep));
            put("inattentive", Boolean.toString(inattentive));
        }};

        String method = "POST";

        sendAsyncTask(endpoint, payload, method);
    }
}
