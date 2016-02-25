package com.example.mscha.payme.pm;

import com.example.mscha.payme.app.APIInteractor;
import com.example.mscha.payme.app.OnResponseListener;

public class NewPmPresenter implements OnResponseListener {

    private static final String TAG = "NewPmPresenter";
    private NewPmActivity view;
    private APIInteractor apiInteractor;
    private String[] debtorsDummyData = new String[] {"max"};

    public NewPmPresenter(NewPmActivity view) {
        this.view = view;
        this.apiInteractor = new APIInteractor();
    }

    public void onSendClicked(String title, String description, String priceString) {
        boolean inputError = false;
        double price = 0;

        if(title.isEmpty()) {
            inputError = true;
            this.view.showTitleEmptyError();
        }

        if(priceString.isEmpty()) {
            inputError = true;
            this.view.showPriceEmptyError();
        } else {
            try {
                price = Double.parseDouble(priceString);
            } catch (NumberFormatException e) {
                inputError = true;
                this.view.showPriceFormatError();
            }
        }

        if(!inputError) {
            this.apiInteractor.createPm(title, description, debtorsDummyData, price, this);
        }
    }

    @Override
    public void onResponse(String statusCode, String action, String data) {
        //TODO fehlerbehandlung
        if(statusCode.equals("0"))
            view.finish();
    }
}
