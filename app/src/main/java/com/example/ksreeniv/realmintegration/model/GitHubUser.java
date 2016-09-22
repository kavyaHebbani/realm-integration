package com.example.ksreeniv.realmintegration.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by ksreeniv on 21/09/16.
 */
@Getter
@Setter
public class GitHubUser extends RealmObject {
    @PrimaryKey
    private String login;
    private int id;
    private String avatar_url;

    public GitHubUser() {
    }

    public GitHubUser(int id, String login, String avatar_url) {
        this.id = id;
        this.login = login;
        this.avatar_url = avatar_url;
    }
}
