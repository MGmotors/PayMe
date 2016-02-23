package com.example.mscha.payme.register;

import android.util.Log;

import com.example.mscha.payme.R;
import com.example.mscha.payme.app.API;
import com.example.mscha.payme.app.APIInteractor;
import com.example.mscha.payme.app.OnResponseListener;

public class RegisterPresenter implements OnResponseListener {

    private static final String TAG = "LoginPresenter";
    private RegisterActivity view;
    private APIInteractor apiInteractor;

    public RegisterPresenter(RegisterActivity view) {
        this.view = view;
        this.apiInteractor = new APIInteractor();
    }

    public void onRegisterClicked(String username, String email, String password1, String password2) {
        if(password1.equals(password2)) {
            view.showProgress();
            apiInteractor.register(username, email, apiInteractor.byteToString(apiInteractor.hash(password1)), this);
        } else
            this.view.showPasswordMatchError();
    }


    public void onType(String username, String email, String password1, String password2) {
        if(!username.isEmpty() && email.contains("@") && !password1.isEmpty() && !password2.isEmpty())
            this.view.enableRegisterButton(true);
        else
            this.view.enableRegisterButton(false);
    }

    @Override
    public void onResponse(String statusCode, String action, String data) {
        view.hideProgress();
        if (statusCode == null) {
            Log.e(TAG, "Status code = null");
            return;
        }
        switch (statusCode) {
            case API.ErrorCodes.NO_ERROR:
                view.navigateToLogin(view.getString(R.string.registration_link_message));
                break;
            case API.ErrorCodes.USERNAME_TAKEN:
                view.showUsernameInUseError();
                break;
            case API.ErrorCodes.EMAIL_TAKEN:
                view.showEmailInUseError();
                break;
            case API.ErrorCodes.DATABASE_ERROR:
                view.showDatabaseError();
                break;
            default:
                Log.e(TAG, "Unhandled error code in header field " + API.HeaderFields.ERROR + ": " + statusCode);
        }
    }
}