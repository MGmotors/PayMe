package com.example.mscha.payme.helper;

import java.util.HashMap;

public class HTTPPostRequest<K, V> extends HashMap<K, V> {
    @Override
    public String toString() {
        String s = "";
        for (Entry<K, V> entry : this.entrySet()) {
            s += entry.getKey() + "=" + entry.getValue() + "&";
        }
        return s.substring(0, s.length() - 1);
    }
}
