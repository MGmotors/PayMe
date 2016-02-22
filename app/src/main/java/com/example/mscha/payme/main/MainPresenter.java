package com.example.mscha.payme.main;

import com.example.mscha.payme.app.APIInteractor;
import com.example.mscha.payme.app.OnResponseListener;

public class MainPresenter implements OnResponseListener{

    private MainActivity view;
    private APIInteractor apiInteractor;

    public MainPresenter(MainActivity view) {
        this.view = view;
        this.apiInteractor = new APIInteractor();
    }

    public void onAddPressed(){
        this.apiInteractor.createPm("Einkauf", "", new String[]{"user"}, 15.2, this);
    }

    @Override
    public void onResponse(String statusCode, String action, String data) {

    }
}
