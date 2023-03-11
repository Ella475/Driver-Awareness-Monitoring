package com.example.driverawarenessdetection.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Client {

    private static Client instance = null;
    private final String serverUrl = "http://192.168.48.1:5000/";

    private Client() {
        // Private constructor to prevent instantiation from outside
    }

    public static Client getInstance() {
        if (instance == null) {
            instance = new Client();
        }
        return instance;
    }

    public HashMap<String, String> sendPostRequest(String endpoint, HashMap<String, String> payload) throws Exception {
        URL url = new URL(serverUrl + endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        // Convert the request body to JSON
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = mapper.writeValueAsString(payload);
        // Send the request payload
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuilder response = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        // Convert response to HashMap
        String responseString = response.toString();
        HashMap<String, String> responseMap = new HashMap<>();
        String[] keyValuePairs = responseString.split(",");
        for (String pair : keyValuePairs) {
            String[] entry = pair.split(":");
            // remove quotes from response
            entry[0] = entry[0].replace("\"", "");
            entry[1] = entry[1].replace("\"", "");
            responseMap.put(entry[0].trim(), entry[1].trim());
        }
        return responseMap;
    }

    public HashMap<String, String> sendGetRequest(String endpoint, HashMap<String, String> payload) throws Exception {
        URL url = new URL(serverUrl + endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Convert the request body to JSON
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = mapper.writeValueAsString(payload);
        // Send the request payload
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuilder response = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        // Convert response to HashMap
        String responseString = response.toString();
        HashMap<String, String> responseMap = new HashMap<>();
        String[] keyValuePairs = responseString.split(",");
        for (String pair : keyValuePairs) {
            String[] entry = pair.split(":");
            // remove quotes from response
            entry[0] = entry[0].replace("\"", "");
            entry[1] = entry[1].replace("\"", "");
            responseMap.put(entry[0].trim(), entry[1].trim());
        }
        return responseMap;
    }

    private String getPayloadString(HashMap<String, String> payload) {
        StringBuilder payloadString = new StringBuilder();
        for (String key : payload.keySet()) {
            payloadString.append(key).append("=").append(payload.get(key)).append("&");
        }
        return payloadString.toString();
    }
}
