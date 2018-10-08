package com.example.qiangge.torrtest;

import android.app.Application;
import android.content.Context;


import org.acra.ACRA;

public class MainApplication extends Application
{
    @SuppressWarnings("unused")
    private static final String TAG = MainApplication.class.getSimpleName();

    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);

        ACRA.init(this);
    }
}