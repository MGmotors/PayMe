package com.example.mscha.payme.register;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mscha.payme.R;
import com.example.mscha.payme.login.LoginActivity;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private RegisterPresenter presenter;
    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText password1EditText;
    private EditText password2EditText;
    private ProgressDialog progressDialog;
    private Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        presenter = new RegisterPresenter(this);
        this.setSupportActionBar((Toolbar) findViewById(R.id.register_toolbar));

        registerBtn = (Button) findViewById(R.id.btnRegister);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.registering));

        usernameEditText = (EditText) findViewById(R.id.name);
        emailEditText = (EditText) findViewById(R.id.email);
        password1EditText = (EditText) findViewById(R.id.password1);
        password2EditText = (EditText) findViewById(R.id.password2);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                presenter.onType(usernameEditText.getText().toString(), emailEditText.getText().toString(), password1EditText.getText().toString(), password2EditText.getText().toString());
            }
        };

        usernameEditText.addTextChangedListener(textWatcher);
        emailEditText.addTextChangedListener(textWatcher);
        password1EditText.addTextChangedListener(textWatcher);
        password2EditText.addTextChangedListener(textWatcher);

        findViewById(R.id.btnRegister).setOnClickListener(this);
        findViewById(R.id.btnLinkToLogin).setOnClickListener(this);
    }

    public void showUsernameInUseError() {
        showSnackbar(R.string.username_taken_error);
    }

    public void showEmailInUseError() {
        showSnackbar(R.string.email_in_use_error);
    }

    public void showPasswordMatchError() {
        this.password2EditText.setError(getString(R.string.password_match_error));
    }

    public void showDatabaseError() {
        showSnackbar(R.string.database_error);
    }

    public void enableRegisterButton(boolean b) {
        this.registerBtn.setEnabled(b);
    }

    private void showSnackbar(int stringResId) {
        Snackbar.make(findViewById(R.id.register_container), stringResId, Snackbar.LENGTH_LONG).show();
    }

    public void showProgress() {
        progressDialog.show();
    }

    public void hideProgress() {
        progressDialog.hide();
    }

    public void navigateToLogin(String message) {
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        if(message != null)
            i.putExtra("Message", message);
        startActivity(i);
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnRegister) {
            String username = usernameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password1 = password1EditText.getText().toString().trim();
            String password2 = password2EditText.getText().toString().trim();
            presenter.onRegisterClicked(username, email, password1, password2);
        } else if (v.getId() == R.id.btnLinkToLogin) {
            this.navigateToLogin(null);
        }
    }
}
