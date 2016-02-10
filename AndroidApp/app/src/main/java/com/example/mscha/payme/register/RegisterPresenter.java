package com.example.mscha.payme.register;

import android.util.Log;

import com.example.mscha.payme.app.API;
import com.example.mscha.payme.app.APIInteractor;
import com.example.mscha.payme.app.OnResponseListener;

public class RegisterPresenter implements OnResponseListener {

    private static final String TAG = "LoginPresenter";
    private RegisterActivity registerView;
    private APIInteractor apiInteractor;

    public RegisterPresenter(RegisterActivity registerView) {
        this.registerView = registerView;
        this.apiInteractor = new APIInteractor();
    }

    public void onRegisterClicked(String username, String email, String password1, String password2) {
        boolean credentialError = false;
        if (username.isEmpty()) {
            registerView.showUsernameEmptyError();
            credentialError = true;
        }
        if (email.isEmpty()) {
            registerView.showEmailEmptyError();
            credentialError = true;
        }
        if (password1.isEmpty()) {
            registerView.showPassword1EmptyError();
            credentialError = true;
        }
        if (password2.isEmpty()) {
            registerView.showPassword2EmptyError();
            credentialError = true;
        }
        if (!password1.equals(password2)) {
            registerView.showPasswordMatchError();
            credentialError = true;
        }
        //// TODO: 08.02.2016 auf gültige email überprüfen

        if (!credentialError) {
            registerView.showProgress();
            apiInteractor.register(username, email, password1, this);
        }
    }

    public void onLinkToLoginClicked() {
        registerView.navigateToLogin();
    }

    @Override
    public void onResponse(String statusCode) {
        registerView.hideProgress();
        switch (statusCode) {
            case API.ErrorCodes.NO_ERROR:
                registerView.navigateToLogin();
                break;
            case API.ErrorCodes.USERNAME_TAKEN:
                registerView.showUsernameInUseError();
                break;
            case API.ErrorCodes.EMAIL_TAKEN:
                registerView.showEmailInUseError();
                break;
            case API.ErrorCodes.DATABASE_ERROR:
                registerView.showDatabaseError();
                break;
            //TODO debug code entfernen
            case APIInteractor.IO_EXCEPTION:
                Log.d(TAG, "IOEXEPTION");
                break;
            default:
                Log.e(TAG, "Unhandled error code in header field " + API.HeaderFields.ERROR + ": " + statusCode);
        }
    }
}