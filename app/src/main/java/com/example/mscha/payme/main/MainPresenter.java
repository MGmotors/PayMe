package com.example.mscha.payme.main;

import android.util.Log;

import com.example.mscha.payme.app.API;
import com.example.mscha.payme.app.APIInteractor;
import com.example.mscha.payme.app.OnResponseListener;
import com.example.mscha.payme.main.pmhistory.PmHistoryItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainPresenter implements OnResponseListener{

    private static final String TAG = "MainPresenter";
    private MainActivity view;
    private APIInteractor apiInteractor;

    public MainPresenter(MainActivity view) {
        this.view = view;
        this.apiInteractor = new APIInteractor();
    }

    public void getMyPMs(){
        this.apiInteractor.getMyPMs(this);
    }

    @Override
    public void onResponse(String statusCode, String action, String data) {
        //TODO fehlerbehandlung
        if (statusCode.equals(API.ErrorCodes.NO_ERROR) && action.equals(API.ActionCodes.GET_MY_PMS)) {
            if(data == null){
                Log.e(TAG, "nix data");
                return;
            }
            this.view.updatePmHistoryItems(parseJsonString(data));
        } else {
            Log.d(TAG, "Error! Status code: " + statusCode);
        }
    }

    public void onRefreshClicked() {
        getMyPMs();
    }

    public List<PmHistoryItem> parseJsonString(String data) {
        ArrayList<PmHistoryItem> items = new ArrayList<>();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.getJSONArray("myPMs");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject pm = jsonArray.getJSONObject(i);
                String name = String.valueOf(pm.get("name"));
                String description = String.valueOf(pm.get("description"));
                String debtors = String.valueOf(pm.get("debtors"));
                String price = String.valueOf(pm.get("price"));
                Date dateTime = Timestamp.valueOf(String.valueOf(pm.get("datetime")));
                PmHistoryItem item = new PmHistoryItem(name, description, debtors, dateTime, Double.parseDouble(price));
                Log.d(TAG, "Item " + i + ": " + item.toString());
                items.add(item);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return items;
    }

    public void onResume() {
        getMyPMs();
    }
}
