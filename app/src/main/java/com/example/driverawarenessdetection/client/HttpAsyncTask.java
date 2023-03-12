package com.example.driverawarenessdetection.client;

import android.os.AsyncTask;

import com.example.driverawarenessdetection.login.data.Result;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class HttpAsyncTask extends AsyncTask<Void, Void, HashMap<String, String>> {
    private String url;
    private HashMap<String, String> payload;
    private String method;
    private OnResponseListener onResponseListener;

    public HttpAsyncTask(String url, HashMap<String, String> payload, String method, OnResponseListener onResponseListener) {
        this.url = url;
        this.payload = payload;
        this.method = method;
        this.onResponseListener = onResponseListener;
    }

    @Override
    protected HashMap<String, String> doInBackground(Void... voids) {
        HttpURLConnection urlConnection = null;
        HashMap<String, String> responseMap = new HashMap<>();

        try {
            // Create URL object
            URL requestUrl = new URL(url);

            // Open HTTP connection
            urlConnection = (HttpURLConnection) requestUrl.openConnection();
            urlConnection.setRequestMethod(method);
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);
            urlConnection.setDoOutput(true);

            // Set request body
            if (payload != null && !payload.isEmpty()) {
                OutputStream outputStream = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                writer.write(getPostDataString(payload));
                writer.flush();
                writer.close();
                outputStream.close();
            }

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

                responseMap.put("response", response.toString());
                responseMap.put("success", "true");
            } else {
                responseMap.put("success", "false");
                responseMap.put("error", "Response code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
            responseMap.put("success", "false");
            responseMap.put("error", "Exception: " + e.getMessage());
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

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first) {
                first = false;
            } else {
                result.append("&");
            }

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    public interface OnResponseListener {
        Result onResponse(HashMap<String, String> responseMap);
    }
}
