package com.example.ksreeniv.realmintegration;

import com.example.ksreeniv.realmintegration.injections.ApplicationComponent;
import com.example.ksreeniv.realmintegration.injections.ApplicationModule;
import com.example.ksreeniv.realmintegration.injections.DaggerApplicationComponent;

import android.app.Application;

/**
 * Created by ksreeniv on 21/09/16.
 */

public class MainApplication extends Application {

    private ApplicationComponent mComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mComponent = DaggerApplicationComponent.builder()
                                               .applicationModule(new ApplicationModule(this))
                                               .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return mComponent;
    }
}
