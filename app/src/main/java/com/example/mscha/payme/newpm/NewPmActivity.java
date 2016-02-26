package com.example.mscha.payme.newpm;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.mscha.payme.R;

public class NewPmActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "NewPmActivity";
    private NewPmPresenter presenter;
    private EditText titleET;
    private EditText descriptionET;
    private EditText priceET;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pm);
        presenter = new NewPmPresenter(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.pm_toolbar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.sending_pm_dialog_message));

        ImageButton sendBtn = (ImageButton)findViewById(R.id.send_btn);
        sendBtn.setVisibility(View.VISIBLE);
        sendBtn.setOnClickListener(this);

        titleET = (EditText) findViewById(R.id.titleEditText);
        descriptionET = (EditText) findViewById(R.id.descriptionEditText);
        priceET = (EditText) findViewById(R.id.priceEditText);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        presenter.onSendClicked(titleET.getText().toString(), descriptionET.getText().toString(), priceET.getText().toString());
    }

    public void showProgressDialog(boolean show) {
        if (show)
            this.progressDialog.show();
        else
            this.progressDialog.hide();
    }

    public void showTitleEmptyError() {
        titleET.setError(getString(R.string.empty_title_error));
    }

    public void showPriceEmptyError() {
        priceET.setError(getString(R.string.empty_price_error));
    }

    public void showPriceFormatError() {
        priceET.setError(getString(R.string.price_format_error));
    }
}
