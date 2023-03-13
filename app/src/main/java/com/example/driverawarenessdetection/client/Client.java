package com.example.driverawarenessdetection.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import com.example.driverawarenessdetection.login.data.Result;
import com.example.driverawarenessdetection.login.data.model.LoggedInUser;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    public boolean checkUserExists(String username){
        String endpoint = "users";
        HashMap<String, String> payload = new HashMap<String, String>() {{
            put("username", username);
        }};

        String method = "GET";

        // Create a new HttpAsyncTask with the specified parameters
        HttpAsyncTask httpAsyncTask = new HttpAsyncTask(endpoint, payload, method, responseMap -> {});

        // Execute the HttpAsyncTask
        httpAsyncTask.execute();

        HashMap<String, String> response = new HashMap<>();
        // Wait for the response
        try {
            response = httpAsyncTask.get();
            // Do something with the response
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

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

        // Create a new HttpAsyncTask with the specified parameters
        HttpAsyncTask httpAsyncTask = new HttpAsyncTask(endpoint, payload, method, responseMap -> {});

        // Execute the HttpAsyncTask
        httpAsyncTask.execute();

        HashMap<String, String> response = new HashMap<>();

        // Wait for the response
        try {
            response = httpAsyncTask.get();
            // Do something with the response
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        // if  we succeeded and response is not false, user was created
        if ((Objects.equals(response.get("success"), "true")) &&
                !Objects.equals(response.get("response"), "false")) {
            System.out.println("User " + username + " was created successfully!");
            return new Result.Success(new LoggedInUser(
                    response.get("response"),
                    username));
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

        // Create a new HttpAsyncTask with the specified parameters
        HttpAsyncTask httpAsyncTask = new HttpAsyncTask(endpoint, payload, method, responseMap -> {});

        // Execute the HttpAsyncTask
        httpAsyncTask.execute();

        HashMap<String, String> response = new HashMap<>();

        // Wait for the response
        try {
            response = httpAsyncTask.get();
            // Do something with the response
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        // if  we succeeded and response is not false, allow login
        if ((Objects.equals(response.get("success"), "true")) &&
                !Objects.equals(response.get("response"), "false")) {
            System.out.println("User " + username + " logged in successfully!");
            return new Result.Success(new LoggedInUser(
                    response.get("response"),
                    username));
        }
        System.out.println("User " + username + " failed to login");
        return new Result.Error(new IOException("Username already exists. Password incorrect!"));

    }
}
