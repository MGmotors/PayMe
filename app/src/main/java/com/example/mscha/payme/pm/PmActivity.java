package com.example.mscha.payme.pm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.mscha.payme.R;

public class PmActivity extends AppCompatActivity implements View.OnClickListener {

    private PmPresenter presenter;
    private EditText titleED;
    private EditText descriptionED;
    private EditText priceEd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pm);
        presenter = new PmPresenter(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.pm_toolbar);
        setSupportActionBar(toolbar);

        ImageButton sendBtn = (ImageButton)findViewById(R.id.send_btn);
        sendBtn.setVisibility(View.VISIBLE);
        sendBtn.setOnClickListener(this);

        titleED = (EditText) findViewById(R.id.titleEditText);
        descriptionED = (EditText)findViewById(R.id.descriptionEditText);
        priceEd = (EditText) findViewById(R.id.priceEditText);

    }

    @Override
    public void onClick(View v) {
        presenter.onSendClicked(titleED.getText().toString(), descriptionED.getText().toString(), priceEd.getText().toString());
    }

    public void showTitleEmptyError() {
        titleED.setError(getString(R.string.empty_title_error));
    }

    public void showPriceEmptyError() {
        priceEd.setError(getString(R.string.empty_price_error));
    }

    public void showPriceFormatError() {
        priceEd.setError(getString(R.string.price_format_error));
    }
}
