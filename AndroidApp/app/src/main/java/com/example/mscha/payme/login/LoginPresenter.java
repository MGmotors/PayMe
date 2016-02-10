package com.example.mscha.payme.login;

import android.util.Log;

import com.example.mscha.payme.app.API;
import com.example.mscha.payme.app.APIInteractor;
import com.example.mscha.payme.app.OnResponseListener;

public class LoginPresenter implements OnResponseListener {

    private static final String TAG = "LoginPresenter";
    private LoginActivity loginView;
    private APIInteractor apiInteractor;

    public LoginPresenter(LoginActivity loginView) {
        this.loginView = loginView;
        this.apiInteractor = new APIInteractor();
    }

    public void onLoginClicked(String username, String password) {
        boolean credentialError = false;
        if (username.isEmpty()) {
            loginView.showUsernameEmptyError();
            credentialError = true;
        }
        if (password.isEmpty()) {
            loginView.showPasswordEmptyError();
            credentialError = true;
        }
        if (!credentialError) {
            loginView.showProgress();
            this.apiInteractor.login(username, password, this);
        }
    }

    public void onLinkToRegisterClicked() {
        loginView.navigateToRegister();
    }

    @Override
    public void onResponse(String statusCode) {
        loginView.hideProgress();
        switch (statusCode) {
            case API.ErrorCodes.NO_ERROR:
                loginView.navigateToMain();
                break;
            case API.ErrorCodes.NAME_PW_MISMATCH:
                loginView.showLoginFailedError();
                break;
            case API.ErrorCodes.DATABASE_ERROR:
                loginView.showDatabaseError();
                break;
            //TODO debug code entfernen
            case APIInteractor.IO_EXCEPTION:
                Log.d(TAG, "IO_Exception");
                break;
            default:
                Log.e(TAG, "Unhandled error code in header field " + API.HeaderFields.ERROR + ": " + statusCode);
        }
    }
}
