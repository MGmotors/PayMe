package com.example.mscha.payme.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.mscha.payme.R;
import com.example.mscha.payme.app.API;
import com.example.mscha.payme.app.APIInteractor;
import com.example.mscha.payme.app.OnResponseListener;
import com.example.mscha.payme.pm.PmActivity;

public class MainActivity extends AppCompatActivity implements OnResponseListener{

    private static final String TAG = "MainActivity";
    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.presenter = new MainPresenter(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToPm();
            }
        });
    }

    public void navigateToPm() {
        startActivity(new Intent(getApplicationContext(), PmActivity.class));
    }

    @Override
    public void onResponse(String statusCode, String action, String data) {
        Log.d(TAG, "StatusCode: " + statusCode);
    }
}
