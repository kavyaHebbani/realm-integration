package com.example.ksreeniv.realmintegration.network;

import com.example.ksreeniv.realmintegration.model.GitHubUser;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by ksreeniv on 21/09/16.
 */

public interface ApiInterface {

    @GET("/users")
    Observable<List<GitHubUser>> getGitHubUsersList();

}
