package com.example.rain.objectbox;

import android.content.Context;

import com.example.rain.objectbox.bean.MyObjectBox;

import io.objectbox.BoxStore;

public class ObjectBox {
    private static BoxStore mBoxStore;
    public static void init(Context context) {
        mBoxStore = MyObjectBox.builder()
                .androidContext(context.getApplicationContext())
                .build();
    }
    public static BoxStore get() { return mBoxStore; }
}
