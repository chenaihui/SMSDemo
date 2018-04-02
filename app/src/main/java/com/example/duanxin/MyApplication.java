package com.example.duanxin;

import android.app.Application;

import cn.smssdk.SMSSDK;

/**
 * Created by 付晨晨 on 2018/4/2.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        初始化你的Appk 和App Secret  并且在Manifests中添加name=.....
        SMSSDK.initSDK(this,"2487a6986bdb1","1c7283837818bb6a0cf2f73d8175881c");
    }
}
