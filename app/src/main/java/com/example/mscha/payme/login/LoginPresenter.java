package com.example.mscha.payme.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.mscha.payme.app.API;
import com.example.mscha.payme.app.APIInteractor;
import com.example.mscha.payme.app.OnResponseListener;

public class LoginPresenter implements OnResponseListener {

    private static final String TAG = "LoginPresenter";
    private LoginActivity view;
    private APIInteractor apiInteractor;

    public LoginPresenter(LoginActivity view) {
        this.view = view;
        this.apiInteractor = new APIInteractor();
    }

    public void onLoginClicked(String email, String password, boolean remember) {
        view.showProgress();
        String hashedPassword = apiInteractor.byteToString(apiInteractor.hash(password));
        if (remember)
            saveCredentials(email, hashedPassword);
        this.apiInteractor.login(email, hashedPassword, this);
    }

    private void saveCredentials(String email, String hashedPassword) {
        //TODO sharedPrefs name ändern + inhalt verschlüsseln
        SharedPreferences sharedPreferences = view.getSharedPreferences(LoginActivity.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //TODO statische variable erstellen?
        editor.putString("email", email);
        editor.putString("hashedPassword", hashedPassword);
        Log.d(TAG, "saved email: " + email);
        Log.d(TAG, "saved hashedPassword: " + hashedPassword);
        editor.apply();
    }

    public void onType(String email, String password) {
        if (email.contains("@") && !password.isEmpty())
            this.view.enableLoginButton(true);
        else
            this.view.enableLoginButton(false);
    }

    public void onLinkToRegisterClicked() {
        view.navigateToRegister();
    }

    @Override
    public void onResponse(String statusCode, String Action, String data) {
        view.hideProgress();
        if (statusCode == null) {
            Log.e(TAG, "Status code = null");
            return;
        }
        switch (statusCode) {
            case API.ErrorCodes.NO_ERROR:
                view.navigateToMain();
                break;
            case API.ErrorCodes.NAME_PW_MISMATCH:
                view.showLoginFailedError();
                break;
            case API.ErrorCodes.DATABASE_ERROR:
                view.showDatabaseError();
                break;
            default:
                Log.e(TAG, "Unhandled error code in header field " + API.HeaderFields.ERROR + ": " + statusCode);
        }
    }

}
