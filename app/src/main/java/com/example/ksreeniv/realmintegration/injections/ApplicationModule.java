package com.example.ksreeniv.realmintegration.injections;

import com.example.ksreeniv.realmintegration.UserListPresenter;
import com.example.ksreeniv.realmintegration.data.GitHubUserListDataSource;
import com.example.ksreeniv.realmintegration.data.RealmDatabaseProvider;
import com.example.ksreeniv.realmintegration.network.ApiInterface;
import com.example.ksreeniv.realmintegration.ui.UserListAdapter;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ksreeniv on 21/09/16.
 */

@Module
public class ApplicationModule {

    private static final String baseUrl = "https://api.github.com/";

    private Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    GitHubUserListDataSource provideGitHubUserListDataSource(ApiInterface apiInterface,
                                                             RealmDatabaseProvider realmDatabaseProvider) {
        return new GitHubUserListDataSource(apiInterface, realmDatabaseProvider);
    }

    @Provides
    UserListPresenter provideGitHubUserPresenter(GitHubUserListDataSource dataSource) {
        return new UserListPresenter(dataSource);
    }

    @Provides
    UserListAdapter provideMainAdapter() {
        return new UserListAdapter(application.getApplicationContext());
    }

    @Provides
    @Singleton
    RealmDatabaseProvider provideRealmDatabase() {
        return new RealmDatabaseProvider(application.getApplicationContext());
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder().build();
    }

    @Provides
    @Singleton
    ApiInterface provideGitHubApiInterface(OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        return retrofit.create(ApiInterface.class);
    }
}
