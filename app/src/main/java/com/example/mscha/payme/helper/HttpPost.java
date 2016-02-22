package com.example.mscha.payme.helper;

import java.util.HashMap;

public class HttpPost extends HashMap<String, String>{
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Entry<String, String> entry : this.entrySet()){
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        return sb.deleteCharAt(sb.length()).toString();
    }
}
