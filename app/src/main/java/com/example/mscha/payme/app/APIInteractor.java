package com.example.mscha.payme.app;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.example.mscha.payme.helper.HttpPost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class APIInteractor {

    public final static String TAG = "APIInteractor";
    private static String sessionCookie;

    public void register(String username, String email, String hashedPassword, OnResponseListener listener) {
        HttpPost request = new HttpPost();
        request.put(API.HeaderFields.ACTION, API.ActionCodes.REGISTER);
        request.put(API.HeaderFields.USERNAME, username);
        request.put(API.HeaderFields.EMAIL, email);
        request.put(API.HeaderFields.PASSWORD, hashedPassword);
        sendAsyncRequest(API.URLs.REGISTER, request, listener);
    }

    public void login(String email, String hashedPassword, OnResponseListener listener) {
        HttpPost request = new HttpPost();
        request.put(API.HeaderFields.ACTION, API.ActionCodes.LOGIN);
        request.put(API.HeaderFields.EMAIL, email);
        request.put(API.HeaderFields.PASSWORD, hashedPassword);
        sendAsyncRequest(API.URLs.LOGIN, request, listener);
    }

    public void createPm(String name, String description, String[] debtors, double price, OnResponseListener onResponseListener) {
        JSONObject json = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            for(String debtor : debtors)
                jsonArray.put(debtor);
            json.put("name", name)
                    .put("description", description)
                    .put("debtors", jsonArray)
                    .put("price", price);

        } catch (JSONException e) {
            Log.d(TAG, "JSONException");
            e.printStackTrace();
        }
        HttpPost request = new HttpPost();
        request.put(API.HeaderFields.ACTION, API.ActionCodes.CREATEPM);
        Log.d(TAG, "Json PM-request: " + json.toString());
        request.put(API.HeaderFields.CREATEPM_JSON, json.toString());
        sendAsyncRequest(API.URLs.CREATEPM, request, onResponseListener);
    }

    public void getMyPMs(OnResponseListener responseListener){
        HttpPost request = new HttpPost();
        request.put(API.HeaderFields.ACTION,API.ActionCodes.GET_MY_PMS);
        sendAsyncRequest(API.URLs.GET_MY_PMS, request, responseListener);
    }

    private void sendAsyncRequest(String url, HttpPost request, OnResponseListener onResponseListener) {
        new SendAsyncRequest(url, request, onResponseListener).execute();
    }

    public byte[] hash(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(password.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String byteToString(byte[] bytes) {
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private class SendAsyncRequest extends AsyncTask<Void, Void, String[]> {
        private final String url;
        private final HttpPost postRequest;
        private final OnResponseListener onResponse;

        public SendAsyncRequest(String url, HttpPost postRequest, OnResponseListener onResponse) {
            this.url = url;
            this.postRequest = postRequest;
            this.onResponse = onResponse;
        }

        @Override
        //TODO debug code entfernen
        protected String[] doInBackground(Void... params) {
            try {
                HttpsURLConnection con = (HttpsURLConnection) new URL(url).openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                if(sessionCookie != null)
                    con.setRequestProperty("Cookie", sessionCookie);
                con.getOutputStream().write(postRequest.toString().getBytes());

                //read answer
                BufferedReader buff = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line;
                StringBuilder answerBuilder = new StringBuilder();
                while ((line = buff.readLine()) != null)
                    answerBuilder.append(line).append("\n");
                String answer = answerBuilder.toString();
                Log.d(TAG, "Answer: " + answer);

                Log.d(TAG, "Statuscode: " + con.getHeaderField(API.HeaderFields.ERROR));

                this.getSessionCookie(con.getHeaderFields().get("Set-Cookie"));
                return new String[]{con.getHeaderField(API.HeaderFields.ERROR), con.getHeaderField(API.HeaderFields.ACTION), answer};
            } catch (IOException e) {
                e.printStackTrace();
                return new String[]{API.ErrorCodes.UNKNOWN_ERROR};
            }
        }

        private void getSessionCookie(List<String> cookies) {
            if(cookies != null)
                for(String cookie : cookies)
                    if(cookie.startsWith("PHPSESSID="))
                        sessionCookie = cookie;
        }

        @Override
        protected void onPostExecute(String[] string) {
            if (string.length < 3) {
                onResponse.onResponse(API.ErrorCodes.UNKNOWN_ERROR, null, null);
                return;
            }
            onResponse.onResponse(string[0],string[1],string[2]);
        }
    }

}
