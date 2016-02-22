package com.example.mscha.payme.main;

import android.util.Log;

import com.example.mscha.payme.app.API;
import com.example.mscha.payme.app.APIInteractor;
import com.example.mscha.payme.app.OnResponseListener;

public class MainPresenter implements OnResponseListener{

    private static final String TAG = "MAINP";
    private MainActivity view;
    private APIInteractor apiInteractor;

    public MainPresenter(MainActivity view) {
        this.view = view;
        this.apiInteractor = new APIInteractor();
    }

    public void getMyPMs(){
        this.apiInteractor.getMyPMs(this);
    }

    public void onAddPressed(){
        this.apiInteractor.createPm("Einkauf", "", new String[]{"user"}, 15.2, this);
    }

    @Override
    public void onResponse(String statusCode, String action, String data) {
        Log.d(TAG,"statuscode: " + statusCode);
        if(statusCode.equals(API.ErrorCodes.UNKNOWN_ERROR)){
            return;
        }

        if(action.equals(API.ActionCodes.GET_MY_PMS)){
            if(data == null){
                Log.d(TAG,"nix data");
                return;
            }
            Log.d(TAG,"data: " + data);
            view.setData(data);
        }
    }
}
