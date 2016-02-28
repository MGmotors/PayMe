package com.example.mscha.payme.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.mscha.payme.app.API;
import com.example.mscha.payme.app.APIInteractor;
import com.example.mscha.payme.app.OnResponseListener;
import com.example.mscha.payme.helper.JsonParser;
import com.example.mscha.payme.login.LoginActivity;
import com.example.mscha.payme.main.history.HistoryFragment;

public class MainPresenter implements OnResponseListener{

    private static final String TAG = "MainPresenter";
    private MainActivity view;
    private APIInteractor apiInteractor;

    public MainPresenter(MainActivity view) {
        this.view = view;
        this.apiInteractor = new APIInteractor();
    }


    public void onPostCreate() {
        tryLoginByStoredCredentials();
    }

    public void tryLoginByStoredCredentials() {
        if (apiInteractor.loggedIn()) {
            getMyPMs();
            getMyPTs();
            return;
        }
        SharedPreferences sharedPreferences = view.getSharedPreferences(LoginActivity.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", null);
        String hashedPassword = sharedPreferences.getString("hashedPassword", null);
        if (email != null && hashedPassword != null) {
            this.view.showLoginProgressDialog(true);
            Log.d(TAG, "load email: " + email);
            Log.d(TAG, "load hashedPassword: " + hashedPassword);
            this.apiInteractor.login(email, hashedPassword, this);
        } else {
            Log.d(TAG, "No stored credentials");
            this.view.navigateToLogin();
        }
    }

    private void clearStoredCredentials() {
        SharedPreferences sharedPreferences = view.getSharedPreferences(LoginActivity.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("email");
        editor.remove("hashedPassword");
        Log.d(TAG, "Credentials cleared");
        editor.apply();
    }

    public void getMyPMs(){
        this.apiInteractor.getMyPMs(this);
    }


    private void getMyPTs() {
        this.apiInteractor.getMyPTs(this);
    }

    @Override
    public void onResponse(String statusCode, String actionCode, String data) {
        //TODO fehlerbehandlung
        if (!statusCode.equals(API.ErrorCodes.NO_ERROR)) {
            Log.d(TAG, "Error! Action code: " + actionCode + ", Status code: " + statusCode);
            this.view.showLoginProgressDialog(false);
            this.view.showLogoutProgressDialog(false);
            return;
        }

        switch (actionCode) {
            case API.ActionCodes.GET_MY_PMS:
                this.view.updateHistoryItems(JsonParser.parseString(data), HistoryFragment.PM_FRAGMENT_ID);
                break;
            case API.ActionCodes.GET_MY_PTS:
                this.view.updateHistoryItems(JsonParser.parseString(data), HistoryFragment.PT_FRAGMENT_ID);
                break;
            case API.ActionCodes.LOGIN:
                Log.d(TAG, "Login successful");
                getMyPMs();
                getMyPTs();
                this.view.showLoginProgressDialog(false);
                break;
            case API.ActionCodes.LOGOUT:
                apiInteractor.clearCookie();
                view.showLogoutProgressDialog(false);
                Log.d(TAG, "Logged out");
                view.navigateToLogin();
                break;
            default:
                break;
        }
    }

    public void onRefresh(int fragmentId) {
        if (fragmentId == HistoryFragment.PM_FRAGMENT_ID)
            getMyPMs();
        else if (fragmentId == HistoryFragment.PT_FRAGMENT_ID)
            getMyPTs();
    }

    public void onNewPmSent() {
        //TODO hier vielleicht pm eizeln einfügen und nicht alle updaten?
        getMyPMs();
    }

    public void onLogout() {
        this.view.showLogoutProgressDialog(true);
        this.clearStoredCredentials();
        apiInteractor.logout(this);
    }
}
