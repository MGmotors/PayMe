package com.example.mscha.payme.contact;

import com.example.mscha.payme.app.APIInteractor;
import com.example.mscha.payme.app.OnResponseListener;

public class ContactPresenter implements OnResponseListener{

    private static final String TAG = "ContactPresenter";
    private ContactActivity view;
    private APIInteractor apiInteractor;

    public ContactPresenter(ContactActivity view) {
        this.view = view;
        apiInteractor = new APIInteractor();
    }

    public void onTextChange(String text) {
        this.apiInteractor.getUsers(text, this);
    }

    @Override
    public void onResponse(String statusCode, String actionCode, String data) {
        if(data != null) {
            String[] users = data.split("\n");
            this.view.fillListView(users);
        }
    }
}
