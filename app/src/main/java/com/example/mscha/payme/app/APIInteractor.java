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
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class APIInteractor {

    public final static String TAG = "APIInteractor";
    public final static String IO_EXCEPTION = "-1";

    private static String sessionCookie;
    //TODO unschöne Fehlerbehandlung ändern bzw Fehlerbehandlung allgemein nachschauen (listener?)

    public void register(String username, String email, String password, OnResponseListener listener) {
        HttpPost request = new HttpPost();
        request.put(API.HeaderFields.ACTION, API.ActionCodes.REGISTER);
        request.put(API.HeaderFields.USERNAME, username);
        request.put(API.HeaderFields.EMAIL, email);
        request.put(API.HeaderFields.PASSWORD, byteToString(hash(password)));
        sendAsyncRequest(API.URLs.REGISTER, request, listener);
    }

    public void login(String email, String password, OnResponseListener listener) {
        HttpPost request = new HttpPost();
        request.put(API.HeaderFields.ACTION, API.ActionCodes.LOGIN);
        request.put(API.HeaderFields.EMAIL, email);
        request.put(API.HeaderFields.PASSWORD, byteToString(hash(password)));
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

    private class SendAsyncRequest extends AsyncTask<Void, Void, String> {
        private final String url;
        private final HttpPost postRequest;
        private final OnResponseListener onResponse;

        public SendAsyncRequest(String url, HttpPost postRequest, OnResponseListener onResponse) {
            this.url = url;
            this.postRequest = postRequest;
            this.onResponse = onResponse;
        }

        @Override
        protected String doInBackground(Void... params) {
            //TODO hier weiter machen (cookies und eingaben managen)
            try {
                HttpsURLConnection con = (HttpsURLConnection) new URL(url).openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                if(sessionCookie != null)
                    con.setRequestProperty("Cookie", sessionCookie);
                con.getOutputStream().write(postRequest.toString().getBytes());
                printAnswer(con);
                getSessionCookie(con.getHeaderFields().get("Set-Cookie"));
                return  con.getHeaderField(API.HeaderFields.ERROR);
            } catch (IOException e) {
                e.printStackTrace();
                return IO_EXCEPTION;
            }
        }

        //TODO debug code entfernen
        private void printAnswer(HttpURLConnection connection) throws IOException{
            BufferedReader buff = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder s = new StringBuilder();
            while ((line = buff.readLine()) != null)
                s.append(line).append("\n");
            Log.d(TAG, "Answer: " + s.toString());
        }

        private void getSessionCookie(List<String> cookies) {
            if(cookies != null)
                for(String cookie : cookies)
                    if(cookie.startsWith("PHPSESSID="))
                        sessionCookie = cookie;
        }

        @Override
        protected void onPostExecute(String string) {
            onResponse.onResponse(string);
        }
    }

}