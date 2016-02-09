package com.example.mscha.payme.app;

import android.os.AsyncTask;
import android.util.Log;

import com.example.mscha.payme.helper.HTTPPostRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class APIInteractor {

    public final static String TAG = APIInteractor.class.getName();
    public final static String MALFORMED_URL_EXCEPTION = "-1";
    public final static String IOEXCEPTION = "-2";
    public final static String NULLPOINTER = "-3";

    public void register(String username, String email, String password, OnResponseListener listener) {
        HTTPPostRequest<String, String> request = new HTTPPostRequest<>();
        request.put(API.HeaderFields.ACTION, API.ActionCodes.REGISTER);
        request.put(API.HeaderFields.USERNAME, username);
        request.put(API.HeaderFields.EMAIL, email);
        request.put(API.HeaderFields.PASSWORD, password);
        sendAsyncRequest(API.URLs.REGISTER, request, listener);
    }

    public void login(String username, String password, OnResponseListener listener) {
        HTTPPostRequest<String, String> request = new HTTPPostRequest<>();
        request.put(API.HeaderFields.ACTION, API.ActionCodes.LOGIN);
        request.put(API.HeaderFields.USERNAME, username);
        request.put(API.HeaderFields.PASSWORD, password);
        sendAsyncRequest(API.URLs.LOGIN, request, listener);
    }

    private void sendAsyncRequest(final String url, final HTTPPostRequest request, final OnResponseListener onResponseListener) {

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.getOutputStream().write(request.toString().getBytes());
                    //TODO debug code entfernen
                    BufferedReader buff = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line = "";
                    String s = "";
                    while ((line = buff.readLine()) != null)
                        s += line + "\n";
                    Log.d(TAG, s);
                    String response = connection.getHeaderField(API.HeaderFields.ERROR);
                    if (response != null)
                        return response;
                    else
                        return NULLPOINTER;
                } catch (MalformedURLException e) {
                    Log.e(TAG, "MalformedURLException: " + e.getLocalizedMessage());
                    e.printStackTrace();
                    return MALFORMED_URL_EXCEPTION;
                } catch (IOException e) {
                    Log.e(TAG, "IOException: " + e.getLocalizedMessage());
                    e.printStackTrace();
                    return IOEXCEPTION;
                }
            }

            @Override
            protected void onPostExecute(String string) {
                onResponseListener.onResponse(string);
            }
        }.execute();
    }
}
