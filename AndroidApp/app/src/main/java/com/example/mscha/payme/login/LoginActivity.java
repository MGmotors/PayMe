package com.example.mscha.payme.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mscha.payme.R;
import com.example.mscha.payme.main.MainActivity;
import com.example.mscha.payme.register.RegisterActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private LoginPresenter loginPresenter;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginPresenter = new LoginPresenter(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.logging_in));

        usernameEditText = (EditText) findViewById(R.id.username);
        passwordEditText = (EditText) findViewById(R.id.password);

        Button loginBtn = (Button) findViewById(R.id.btnLogin);
        loginBtn.setOnClickListener(this);
        findViewById(R.id.btnLinkToRegister).setOnClickListener(this);

        if (this.getIntent().hasExtra("username")) {
            String username = this.getIntent().getStringExtra("username");
            String password = this.getIntent().getStringExtra("password");
            usernameEditText.setText(username);
            passwordEditText.setText(password);
            this.onClick(findViewById(R.id.btnLogin));
        }
    }

    public void showLoginFailedError() {
        Snackbar.make(findViewById(R.id.login_container), R.string.login_error, Snackbar.LENGTH_LONG).show();
    }

    public void showUsernameEmptyError() {
        usernameEditText.setError(getString(R.string.username_empty_error));
    }

    public void showPasswordEmptyError() {
        passwordEditText.setError(getString(R.string.password_empty_error));
    }

    public void showDatabaseError() {
        Snackbar.make(findViewById(R.id.login_container), R.string.database_error, Snackbar.LENGTH_INDEFINITE);
    }

    public void showProgress() {
        progressDialog.show();
    }

    public void hideProgress() {
        progressDialog.hide();
    }

    public void navigateToMain() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    public void navigateToRegister() {
        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnLogin) {
            loginPresenter.onLoginClicked(usernameEditText.getText().toString().trim(), passwordEditText.getText().toString().trim());
        } else if (v.getId() == R.id.btnLinkToRegister) {
            loginPresenter.onLinkToRegisterClicked();
        }
    }
}
