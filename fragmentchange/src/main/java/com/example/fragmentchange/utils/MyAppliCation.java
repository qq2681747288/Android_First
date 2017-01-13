package com.example.fragmentchange.utils;

import android.app.Application;

/**
 * Created by Administrator on 2017/1/3.
 */

public class MyAppliCation extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //当应用程序打开后调用初始化方法
        ImageUtils.initCache(getApplicationContext());

    }
}
