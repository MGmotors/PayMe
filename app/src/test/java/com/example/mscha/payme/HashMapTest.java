package com.example.mscha.payme;

import android.util.Log;

import com.example.mscha.payme.helper.HttpPost;

import org.junit.Test;

public class HashMapTest {
    @Test
    public void testOutput() {
        HttpPost httpPost = new HttpPost();
        httpPost.put("test", "TESt");
        httpPost.put("test1", "test1");
        System.out.println(httpPost);
    }
}
