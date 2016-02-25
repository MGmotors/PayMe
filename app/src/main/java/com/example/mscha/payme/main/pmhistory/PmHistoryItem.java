package com.example.mscha.payme.main.pmhistory;

public class PmHistoryItem {

    public final String title;
    public final String description;
    public final String debtors;
    public final double price;
    public final String date;

    public PmHistoryItem(String title, String description, String debtors, double price, String date) {
        this.title = title;
        this.description = description;
        this.debtors = debtors;
        this.price = price;
        this.date = date;
    }

    public String toString() {
        return "Title: " + title + ", Description: " + description + ", Price: " + price;
    }
}
