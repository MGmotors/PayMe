package com.example.mscha.payme.helper;

import android.util.Log;

import com.example.mscha.payme.app.API;
import com.example.mscha.payme.main.history.HistoryItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class JsonParser {

    private static String TAG = "JsonParser";

    public static List<HistoryItem> parseString(String data) {
        ArrayList<HistoryItem> items = new ArrayList<>();
        JSONObject jO;
        try {
            jO = new JSONObject(data);
            JSONArray itemsJA = jO.getJSONArray(API.JSON.PM_ARRAY);
            for (int i = 0; i < itemsJA.length(); i++) {
                JSONObject itemJO = itemsJA.getJSONObject(i);

                String creator = itemJO.getString(API.JSON.CREATOR);
                String title = itemJO.getString(API.JSON.TITLE);
                String description = itemJO.getString(API.JSON.DESCRIPTION);
                Date dateTime = Timestamp.valueOf(itemJO.getString(API.JSON.DATETIME));
                String price = itemJO.getString(API.JSON.PRICE);

                //parse debtors
                JSONArray debtorsJsonArray = itemJO.getJSONArray(API.JSON.DEBTORS_ARRAY);
                HashMap<String, Boolean> debtors = new HashMap<>(debtorsJsonArray.length());
                Log.d(TAG, String.valueOf(debtorsJsonArray.length()));
                final int numberOfItemsInResp = debtorsJsonArray.length();
                for (int j = 0; j < numberOfItemsInResp; j++) {
                    JSONObject debtorJsonObject = debtorsJsonArray.getJSONObject(j);
                    String debtorName = debtorJsonObject.getString(API.JSON.USERNAME);
                    boolean hasPayed = debtorJsonObject.getInt(API.JSON.HAS_PAYED) == 1;
//                    Log.d(TAG, "Debtor Name: " + debtorName + ", has Payed: " + hasPayed);
                    debtors.put(debtorName, hasPayed);
                }

                HistoryItem item = new HistoryItem(creator, title, description, debtors, dateTime, Double.parseDouble(price));
                Log.d(TAG, "Item " + i + ": " + item.toString());
                items.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return items;
    }
}
