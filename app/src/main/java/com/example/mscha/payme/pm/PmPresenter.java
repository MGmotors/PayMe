package com.example.mscha.payme.pm;

import com.example.mscha.payme.app.APIInteractor;
import com.example.mscha.payme.app.OnResponseListener;

public class PmPresenter implements OnResponseListener{

    private static final String TAG = "PmPresenter";
    private PmActivity view;
    private APIInteractor apiInteractor;
    private String[] debtorsDummyData = new String[] {"max@"};

    public PmPresenter(PmActivity view) {
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
        view.finish();
    }
}
