package com.example.beerrecruitmentexercise;

import android.app.Application;

import com.example.beerrecruitmentexercise.utils.RealmUtils;
import com.facebook.drawee.backends.pipeline.Fresco;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //Initialize fresco, used to show images.
        Fresco.initialize(this);

        //Initialize realm, used to store and retrieve data from device db.
        RealmUtils.INSTANCE.initializeRealm(this);
    }
}
