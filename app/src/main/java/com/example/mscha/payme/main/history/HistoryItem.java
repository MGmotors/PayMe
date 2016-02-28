package com.example.mscha.payme.main.history;

import java.util.Date;
import java.util.Map;

public class HistoryItem {

    public final String creator;
    public final String title;
    public final String description;
    public final Map<String, Boolean> debtors;
    public final double price;
    public final Date dateTime;

    public HistoryItem(String creator, String title, String description, Map<String, Boolean> debtors, Date dateTime, double price) {
        this.creator = creator;
        this.title = title;
        this.description = description;
        this.debtors = debtors;
        this.price = price;
        this.dateTime = dateTime;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry debtor : debtors.entrySet()) {
            sb.append(", debtor: ").append(debtor.getKey()).append(" has Payed: ").append(debtor.getValue());
        }
        return "Creator: " + creator + ", Title: " + title + ", Description: " + description + sb + ", Date: " + dateTime + ", Price: " + price;
    }
}
