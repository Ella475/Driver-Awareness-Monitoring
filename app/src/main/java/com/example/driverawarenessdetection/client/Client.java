package com.example.driverawarenessdetection.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import com.example.driverawarenessdetection.login.data.Result;
import com.example.driverawarenessdetection.login.data.LoggedInUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


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
        HttpAsyncTask httpAsyncTask = new HttpAsyncTask(endpoint, payload, method, responseMap -> {
        });

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

    public boolean checkUserExists(String username, boolean isUser) {
        String endpoint = "users";
        HashMap<String, String> payload = new HashMap<String, String>() {{
            put("username", username);
        }};

        if (!isUser) {
            payload.put("supervisor", "true");
        }

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

    public Result register(String username, String password, boolean isUser) {
        String endpoint = "users";
        HashMap<String, String> payload = new HashMap<String, String>() {{
            put("username", username);
            put("password", password);
        }};

        if (!isUser) {
            payload.put("supervisor", "true");
        }

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

    public Result login(String username, String password, boolean isUser) {
        String endpoint = "users";
        HashMap<String, String> payload = new HashMap<String, String>() {{
            put("username", username);
            put("password", password);
        }};

        if (!isUser) {
            payload.put("supervisor", "true");
        }

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
        HashMap<String, String> payload = new HashMap<String, String>() {{
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

    public List<HashMap<String, String>> getDrives(String userId) throws JSONException {
        String endpoint = "drives";
        HashMap<String, String> payload = new HashMap<String, String>() {{
            put("user_id", userId);
        }};

        String method = "GET";

        HashMap<String, String> response = getResponse(sendAsyncTask(endpoint, payload, method));

        if (Objects.equals(response.get("success"), "true")) {
            System.out.println("Drives retrieved successfully!");
            return parseDrives(response.get("response"));

        } else {
            System.out.println("Drives failed to retrieve");
            return null;
        }
    }

    private List<HashMap<String, String>> parseDrives(String jsonString) throws JSONException {
        if (jsonString == null) {
            return null;
        }
        JSONArray jsonArray = new JSONArray(jsonString);
        List<HashMap<String, String>> drives = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int driveId = jsonObject.getInt("id");
            String driveTime = jsonObject.getString("time");
            HashMap<String, String> drive = new HashMap<>();
            drive.put("driveId", Integer.toString(driveId));
            drive.put("driveTime", driveTime);
            drives.add(drive);
        }
        return drives;
    }

    public void sendDriveData(String drive_id, int awarenessPercentage, boolean asleep,
                              boolean inattentive) {
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

    public String getDriveData(String driveId) {
        String endpoint = "drives_data";
        HashMap<String, String> payload = new HashMap<String, String>() {{
            put("drive_id", driveId);
        }};

        String method = "GET";

        HashMap<String, String> response = getResponse(sendAsyncTask(endpoint, payload, method));

        if (Objects.equals(response.get("success"), "true")) {
            System.out.println("Drive data retrieved successfully!");
            return response.get("response");

        } else {
            System.out.println("Drive data failed to retrieve");
            return null;
        }
    }

    public List<List<String>> getSupervisedUsers(String supervisorId) {
        String endpoint = "supervisors";
        HashMap<String, String> payload = new HashMap<String, String>() {{
            put("supervisor_id", supervisorId);
        }};

        String method = "GET";

        HashMap<String, String> response = getResponse(sendAsyncTask(endpoint, payload, method));

        if (Objects.equals(response.get("success"), "true")) {
            System.out.println("Supervised users retrieved successfully!");
            if (response.get("response") == null) {
                return new ArrayList<>();
            }
            return parseSupervisedUsers(response.get("response"));

        } else {
            System.out.println("Supervised users failed to retrieve");
            return null;
        }

    }

    private List<List<String>> parseSupervisedUsers(String response) {
        if (response == null) {
            return null;
        }
        response = response.replace("[", "");
        response = response.replace("]", "");
        response = response.replace("'", "");
        String[] supervisedUsers = response.split(", ");

        ArrayList<String> userIds = new ArrayList<>();
        ArrayList<String> usernames = new ArrayList<>();
        for (String supervisedUser : supervisedUsers) {
            String[] user = supervisedUser.split(":");
            userIds.add(user[0]);
            usernames.add(user[1]);
        }

        return new ArrayList<>(Arrays.asList(userIds, usernames));
    }

    public void setSupervisedUser(String supervisorId, String username) {
        String endpoint = "supervisors";
        HashMap<String, String> payload = new HashMap<String, String>() {{
            put("supervisor_id", supervisorId);
            put("username", username);
        }};

        String method = "POST";

        sendAsyncTask(endpoint, payload, method);
    }
}
