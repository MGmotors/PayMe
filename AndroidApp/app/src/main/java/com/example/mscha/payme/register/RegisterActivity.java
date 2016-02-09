package com.example.mscha.payme.register;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.example.mscha.payme.R;
import com.example.mscha.payme.login.LoginActivity;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private RegisterPresenter registerPresenter;
    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText password1EditText;
    private EditText password2EditText;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerPresenter = new RegisterPresenter(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.registering));

        usernameEditText = (EditText) findViewById(R.id.name);
        emailEditText = (EditText) findViewById(R.id.email);
        password1EditText = (EditText) findViewById(R.id.password1);
        password2EditText = (EditText) findViewById(R.id.password2);

        findViewById(R.id.btnRegister).setOnClickListener(this);
        findViewById(R.id.btnLinkToLogin).setOnClickListener(this);
    }

    public void showUsernameInUseError() {
        Snackbar.make(findViewById(R.id.register_container), R.string.username_taken_error, Snackbar.LENGTH_LONG).show();
    }

    public void showUsernameEmptyError() {
        usernameEditText.setError(getString(R.string.username_empty_error));
    }

    public void showEmailEmptyError() {
        emailEditText.setError(getString(R.string.email_empty_error));
    }

    public void showEmailInUseError() {
        emailEditText.setError(getString(R.string.email_in_use_error)); //TODO email schon in benutzung überprüfen?
    }

    public void showPassword1EmptyError() {
        password1EditText.setError(getString(R.string.password_empty_error));
    }

    public void showPassword2EmptyError() {
        password2EditText.setError(getString(R.string.password_empty_error));
    }

    public void showPasswordMatchError() {
        this.password2EditText.setError(getString(R.string.password_match_error));
    }

    public void showDatabaseError() {
        Snackbar.make(findViewById(R.id.register_container), R.string.database_error, Snackbar.LENGTH_INDEFINITE);
    }

    public void showProgress() {
        progressDialog.show();
    }

    public void hideProgress() {
        progressDialog.hide();
    }

    public void navigateToLogin() {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    /*public void navigateToLogin(String username, String password) {
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        i.putExtra("username", username);
        i.putExtra("password", password);
        startActivity(i);
        finish();
    }*/

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnRegister) {
            String username = usernameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password1 = password1EditText.getText().toString().trim();
            String password2 = password2EditText.getText().toString().trim();
            registerPresenter.onRegisterClicked(username, email, password1, password2);
        } else if (v.getId() == R.id.btnLinkToLogin) {
            registerPresenter.onLinkToLoginClicked();
        }
    }
}
