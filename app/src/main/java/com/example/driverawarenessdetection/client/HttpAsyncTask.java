package com.example.driverawarenessdetection.client;

import android.os.AsyncTask;

import com.example.driverawarenessdetection.login.data.Result;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpAsyncTask extends AsyncTask<Void, Void, HashMap<String, String>> {
    private String url = "http://10.0.2.2:5000/";
    private HashMap<String, String> payload;
    private String method;
    private OnResponseListener onResponseListener;

    public HttpAsyncTask(String endpoint, HashMap<String, String> payload, String method, OnResponseListener onResponseListener) {
        this.url = this.url + endpoint;
        this.payload = payload;
        this.method = method;
        this.onResponseListener = onResponseListener;
    }

    @Override
    protected HashMap<String, String> doInBackground(Void... voids) {
        HttpURLConnection urlConnection = null;
        HashMap<String, String> responseMap = new HashMap<>();

        try {
            // Sent GET request
            if (method.equals("GET")) {
                String encodedParams = getParamsString(payload);
                if (!encodedParams.isEmpty()) {
                    this.url += "?" + encodedParams;
                }
            }

            // Create URL object
            URL requestUrl = new URL(url);

            // Open HTTP connection
            urlConnection = (HttpURLConnection) requestUrl.openConnection();
            urlConnection.setRequestMethod(method);
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);
            urlConnection.addRequestProperty("Content-Type", "application/json; utf-8");


            // Send POST request
            if (method.equals("POST")) {
                urlConnection.setDoOutput(true);

                if (!payload.isEmpty()) {
                    JSONObject postData = new JSONObject();
                    for (Map.Entry<String, String> entry : payload.entrySet()) {
                        postData.put(entry.getKey(), entry.getValue());
                    }
                    String jsonInputString = postData.toString();
                    OutputStream os = urlConnection.getOutputStream();
                    byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                    os.close();
                }
            }

            urlConnection.connect();

            // Get response
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Create a new Gson instance
                Gson gson = new Gson();

                // Parse the JSON response string into a HashMap
                Type type = new TypeToken<HashMap<String, String>>() {}.getType();
                responseMap.putAll(gson.fromJson(response.toString(), type));
                responseMap.put("success", "true");
            } else {
                responseMap.put("success", "false");
                responseMap.put("error", "Response code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
            responseMap.put("success", "false");
            responseMap.put("error", "Exception: " + e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return responseMap;
    }

    @Override
    protected void onPostExecute(HashMap<String, String> responseMap) {
        onResponseListener.onResponse(responseMap);
    }

//    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
//        StringBuilder result = new StringBuilder();
//        result.append("{");
//        for (Map.Entry<String, String> entry : params.entrySet()) {
//            result.append("\"");
//            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));  // key
//            result.append("\"");
//            result.append(":");
//            result.append("\"");
//            result.append(URLEncoder.encode(entry.getValue(), "UTF-8")); // value
//            result.append("\"");
//            result.append(",");
//        }
//        result.deleteCharAt(result.length() - 1);
//        result.append("}");
//
//        return result.toString();
//    }

    private String getParamsString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));  // key
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8")); // value
            result.append("&");
        }
        result.deleteCharAt(result.length() - 1);

        return result.toString();
    }

    private HashMap<String, String> extractResponseMapFromJsonString(String jsonResponse) {
        HashMap<String, String> responseMap = new HashMap<>();
        String[] responseArray = jsonResponse.split(",");
        for (String response : responseArray) {
            String[] responsePair = response.split(":");
            responseMap.put(responsePair[0], responsePair[1]);
        }
        return responseMap;
    }

    public interface OnResponseListener {
        void onResponse(HashMap<String, String> responseMap);
    }
}
