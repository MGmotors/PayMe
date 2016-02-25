package com.example.mscha.payme.main.pmhistory;

import java.util.Date;

public class PmHistoryItem {

    public final String title;
    public final String description;
    public final String debtors;
    public final double price;
    public final Date dateTime;

    public PmHistoryItem(String title, String description, String debtors, Date dateTime,  double price) {
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
