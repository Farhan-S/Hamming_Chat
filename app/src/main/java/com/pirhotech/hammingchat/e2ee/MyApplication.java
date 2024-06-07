package com.pirhotech.hammingchat.e2ee;

import android.app.Application;
import com.google.crypto.tink.config.TinkConfig;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        try {
            TinkConfig.register();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
