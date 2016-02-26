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
    //TODO alternative für statische variable suchen
    private static String sessionCookie;

    public boolean loggedIn() {
        return sessionCookie != null;
    }

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

    //apiInteractor.clearCookie() in callback aufrufen nicht vergessen (cookie muss bei logout noch mitgeschickt werden)
    public void logout(OnResponseListener listener) {
        HttpPost request = new HttpPost();
        request.put(API.HeaderFields.ACTION, API.ActionCodes.LOGOUT);
        sendAsyncRequest(API.URLs.LOGOUT, request, listener);
    }

    public void clearCookie() {
        sessionCookie = null;
    }

    public void createPm(String title, String description, String[] debtors, double price, OnResponseListener onResponseListener) {
        JSONObject json = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            for(String debtor : debtors)
                jsonArray.put(debtor);
            json.put("name", title)
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
        return Base64.encodeToString(bytes, Base64.NO_WRAP);
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

                //Log.d(TAG, "Answer: " + answer);
                Log.d(TAG, "Statuscode: " + con.getHeaderField(API.HeaderFields.ERROR));
                Log.d(TAG, "ActionCode: " + con.getHeaderField(API.HeaderFields.ACTION));

                //TODO session cookie wird zwei mal gesetzt, warum?
                this.getSessionCookie(con.getHeaderFields().get("Set-Cookie"));

                return new String[]{con.getHeaderField(API.HeaderFields.ERROR), con.getHeaderField(API.HeaderFields.ACTION), answer};
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        private void getSessionCookie(List<String> cookies) {
            if(cookies != null)
                for(String cookie : cookies)
                    if (cookie.startsWith("PHPSESSID=")) {
                        sessionCookie = cookie;
                        Log.d(TAG, "Cookie set: " + cookie);
                    }
        }

        @Override
        protected void onPostExecute(String[] string) {
            if (string == null) {
                onResponse.onResponse(API.ErrorCodes.SERVER_ERROR, null, null);
            } else if (string[0] == null) {
                onResponse.onResponse(API.ErrorCodes.SERVER_ERROR, null, null);
            } else
                onResponse.onResponse(string[0], string[1], string[2]);
        }
    }

}
