package com.example.ksreeniv.realmintegration.injections;

import com.example.ksreeniv.realmintegration.ui.UserListFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by ksreeniv on 21/09/16.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(UserListFragment fragment);
}
