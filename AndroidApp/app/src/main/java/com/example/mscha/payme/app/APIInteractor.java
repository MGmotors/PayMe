package com.example.mscha.payme.app;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.example.mscha.payme.helper.HTTPPostRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.SecretKeyFactory;
import javax.net.ssl.HttpsURLConnection;

public class APIInteractor {

    public final static String TAG = APIInteractor.class.getName();
    public final static String IO_EXCEPTION = "-1";
    //TODO unschöne Fehlerbehandlung ändern bzw Fehlerbehandlung allgemein nachschauen (listener?)
    private static String pw;

    public void register(String username, String email, String password, OnResponseListener listener) {
        HTTPPostRequest<String, String> request = new HTTPPostRequest<>();
        request.put(API.HeaderFields.ACTION, API.ActionCodes.REGISTER);
        request.put(API.HeaderFields.USERNAME, username);
        request.put(API.HeaderFields.EMAIL, email);
        String hashedPassword = byteToString(hash(password));
        Log.d(TAG, hashedPassword);
        request.put(API.HeaderFields.PASSWORD, hashedPassword);
        new SendAsyncRequest(API.URLs.REGISTER, request, listener).execute();
    }

    public void login(String username, String password, OnResponseListener listener) {
        HTTPPostRequest<String, String> request = new HTTPPostRequest<>();
        request.put(API.HeaderFields.ACTION, API.ActionCodes.LOGIN);
        request.put(API.HeaderFields.USERNAME, username);
        String hashedPassword = byteToString(hash(password));
        Log.d(TAG, hashedPassword);
        request.put(API.HeaderFields.PASSWORD, hashedPassword);
        new SendAsyncRequest(API.URLs.LOGIN, request, listener).execute();
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
        return new String(Base64.decode(bytes, Base64.DEFAULT));
    }


    private class SendAsyncRequest extends AsyncTask<Void, Void, String> {
        private final String url;
        private final HTTPPostRequest request;
        private final OnResponseListener onResponse;

        public SendAsyncRequest(String url, HTTPPostRequest request, OnResponseListener onResponse) {
            this.url = url;
            this.request = request;
            this.onResponse = onResponse;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.getOutputStream().write(request.toString().getBytes());
                //TODO debug code entfernen
                BufferedReader buff = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                String s = "";
                while ((line = buff.readLine()) != null)
                    s += line + "\n";
                Log.d(TAG, s);
                return  connection.getHeaderField(API.HeaderFields.ERROR);
            } catch (IOException e) {
                e.printStackTrace();
                return IO_EXCEPTION;
            }
        }

        @Override
        protected void onPostExecute(String string) {
            onResponse.onResponse(string);
        }
    }

}
