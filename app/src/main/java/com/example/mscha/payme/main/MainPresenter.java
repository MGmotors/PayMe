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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class MainPresenter implements OnResponseListener{

    private static final String TAG = "MainPresenter";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private MainActivity view;
    private APIInteractor apiInteractor;

    public MainPresenter(MainActivity view) {
        this.view = view;
        this.apiInteractor = new APIInteractor();
    }

    public void onPostCreate() {
        if(!apiInteractor.isLoggedIn()) {
            boolean loginSuccessful = tryLoginByStoredCredentials();
            if(loginSuccessful){
                Log.d(TAG, "No stored credentials");
                this.view.navigateToLogin();
            }
        } else {
            onLoginSuccessful();
        }
    }

    public boolean tryLoginByStoredCredentials() {
        SharedPreferences sharedPreferences = view.getSharedPreferences(LoginActivity.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", null);
        String hashedPassword = sharedPreferences.getString("hashedPassword", null);
        if (email != null && hashedPassword != null) {
            this.view.showLoginProgressDialog(true);
            Log.d(TAG, "load email: " + email);
            Log.d(TAG, "load hashedPassword: " + hashedPassword);
            this.apiInteractor.login(email, hashedPassword, this);
            return true;
        } else {
            return false;
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
                onLoginSuccessful();
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
            this.apiInteractor.getMyPMs(this);
        else if (fragmentId == HistoryFragment.PT_FRAGMENT_ID)
            this.apiInteractor.getMyPTs(this);
    }

    public void onLoginSuccessful() {
        checkPlayServices();
        this.apiInteractor.getMyPMs(this);
        this.apiInteractor.getMyPTs(this);
    }

    public void onNewPmSent() {
        //TODO hier vielleicht pm eizeln einfügen und nicht alle updaten?
        this.apiInteractor.getMyPMs(this);
    }

    public void onLogout() {
        this.view.showLogoutProgressDialog(true);
        this.clearStoredCredentials();
        apiInteractor.logout(this);
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(view);
        if (resultCode != ConnectionResult.SUCCESS) {
            Log.d(TAG, "checkPlayServices failed");
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(view, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                view.finish();
            }
            return false;
        }
        Log.d(TAG, "checkPlayServices successful");
        return true;
    }
}
