package com.optisoft.emauser;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

/**
 * Created by OptiSoft_A on 5/24/2018.
 */

public class MainApplication extends MultiDexApplication {

    private static MainApplication mainApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mainApplication = this;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }


    public static synchronized MainApplication getInstance() {
        return mainApplication;
    }
}