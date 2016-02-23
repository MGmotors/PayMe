package com.example.mscha.payme.pm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.mscha.payme.R;

public class PmActivity extends AppCompatActivity {

    private PmPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pm);
        presenter = new PmPresenter(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.pm_toolbar);
        setSupportActionBar(toolbar);

    }
}
