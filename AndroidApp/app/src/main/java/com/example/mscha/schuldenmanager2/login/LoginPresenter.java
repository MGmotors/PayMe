package com.example.mscha.schuldenmanager2.login;

import android.util.Log;

import com.example.mscha.schuldenmanager2.app.API;
import com.example.mscha.schuldenmanager2.app.APIInteractor;
import com.example.mscha.schuldenmanager2.app.OnResponseListener;

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
            case APIInteractor.NULLPOINTER:
                Log.d(TAG, "ERROR header = null");
                break;
            case APIInteractor.IOEXCEPTION:
                Log.d(TAG, "IOEXEPTION");
                break;
            case APIInteractor.MALFORMED_URL_EXCEPTION:
                Log.d(TAG, "Host unreachable");
                break;
            default:
                Log.e(TAG, "Unhandled error code in header field " + API.HeaderFields.ERROR + ": " + statusCode);
        }
    }
}
