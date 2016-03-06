package com.example.mscha.payme.newpm;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.mscha.payme.R;

public class NewPmActivity extends AppCompatActivity {

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.sending_pm_dialog_message));

        titleET = (EditText) findViewById(R.id.titleEditText);
        descriptionET = (EditText) findViewById(R.id.descriptionEditText);
        priceET = (EditText) findViewById(R.id.priceEditText);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_pm_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            finish();
        else if (id == R.id.action_send)
            presenter.onSendClicked(titleET.getText().toString(), descriptionET.getText().toString(), priceET.getText().toString());
        return super.onOptionsItemSelected(item);
    }

    public void showProgressDialog(boolean show) {
        if (show)
            this.progressDialog.show();
        else
            this.progressDialog.dismiss();
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
