package com.example.mscha.payme.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;

import com.example.mscha.payme.R;
import com.example.mscha.payme.main.MainActivity;
import com.example.mscha.payme.register.RegisterActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String SHARED_PREFS_NAME = "Credentials";
    public static String TAG = "LoginActivity";
    private LoginPresenter presenter;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private CheckedTextView rememberMeCheckBox;
    private Button loginBtn;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        presenter = new LoginPresenter(this);
        setSupportActionBar((Toolbar) findViewById(R.id.login_toolbar));

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.logging_in));

        usernameEditText = (EditText) findViewById(R.id.email);
        passwordEditText = (EditText) findViewById(R.id.password);
        rememberMeCheckBox = (CheckedTextView) findViewById(R.id.remember_me);
        rememberMeCheckBox.setOnClickListener(this);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                presenter.onType(usernameEditText.getText().toString(), passwordEditText.getText().toString());
            }
        };

        usernameEditText.addTextChangedListener(textWatcher);
        passwordEditText.addTextChangedListener(textWatcher);
        passwordEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER) && loginBtn.isEnabled()) {
                    presenter.onLoginClicked(usernameEditText.getText().toString(), passwordEditText.getText().toString(), rememberMeCheckBox.isChecked());
                    return true;
                }
                return false;
            }
        });
        loginBtn = (Button) findViewById(R.id.btnLogin);
        loginBtn.setOnClickListener(this);

        findViewById(R.id.btnLinkToRegister).setOnClickListener(this);

        if(getIntent().hasExtra("Message"))
            Snackbar.make(findViewById(R.id.login_container), getIntent().getStringExtra("Message"), Snackbar.LENGTH_INDEFINITE).show();
    }

    public void showDatabaseError() {
        showSnackBar(R.string.database_error);
    }

    public void showLoginFailedError() {
        showSnackBar(R.string.login_error);
    }

    private void showSnackBar(int stringResId) {
        Snackbar.make(findViewById(R.id.login_container), stringResId, Snackbar.LENGTH_LONG).show();
    }

    public void enableLoginButton(boolean b) {
        this.loginBtn.setEnabled(b);
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
            presenter.onLoginClicked(usernameEditText.getText().toString().trim(), passwordEditText.getText().toString().trim(), rememberMeCheckBox.isChecked());
        } else if (v.getId() == R.id.btnLinkToRegister) {
            presenter.onLinkToRegisterClicked();
        } else if (v.getId() == R.id.remember_me) {
            if (rememberMeCheckBox.isChecked())
                this.rememberMeCheckBox.setChecked(false);
            else
                this.rememberMeCheckBox.setChecked(true);
        }
    }
}
