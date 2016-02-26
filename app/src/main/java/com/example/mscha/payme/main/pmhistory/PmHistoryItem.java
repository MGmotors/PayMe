package com.example.mscha.payme.main.pmhistory;

import java.util.Date;
import java.util.Map;

public class PmHistoryItem {

    public final String title;
    public final String description;
    public final Map<String, Boolean> debtors;
    public final double price;
    public final Date dateTime;

    public PmHistoryItem(String title, String description, Map<String, Boolean> debtors, Date dateTime, double price) {
        this.title = title;
        this.description = description;
        this.debtors = debtors;
        this.price = price;
        this.dateTime = dateTime;
    }

    public String toString() {
        return "Title: " + title + ", Description: " + description + ", Date: " + dateTime + ", Price: " + price;
    }
}
