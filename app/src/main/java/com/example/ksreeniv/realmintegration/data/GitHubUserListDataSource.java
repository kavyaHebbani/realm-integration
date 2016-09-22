package com.example.ksreeniv.realmintegration.data;

import com.example.ksreeniv.realmintegration.model.GitHubUser;
import com.example.ksreeniv.realmintegration.network.ApiInterface;

import io.realm.RealmResults;
import rx.Observable;

public class GitHubUserListDataSource {

    private enum statusCodes {
        GITHUB_USER_LIST_AVAILABLE,
        GITHUB_USER_LIST_NOT_AVAILABLE,
        DEFAULT_RESPONSE
    }

    private ApiInterface mApiInterface;
    private RealmDatabaseProvider mRealmDatabaseProvider;

    public GitHubUserListDataSource(ApiInterface apiInterface,
                                    RealmDatabaseProvider realmDatabaseProvider) {
        mRealmDatabaseProvider = realmDatabaseProvider;
        mApiInterface = apiInterface;
    }

    public Observable<RealmResults<GitHubUser>> getUserListFromDatabase() {
        return mRealmDatabaseProvider.getObjectAsync(GitHubUser.class)
                                     .filter(gitHubUsers -> gitHubUsers.isLoaded()
                                                            && gitHubUsers.isValid()
                                                            && gitHubUsers.size() > 0);
    }

    public Observable<Boolean> fetchGitHubUsersStatus(boolean isForced) {
        return Observable.concat(getGitHubUsersFromRealm(isForced),
                                 getGitHubUsersFromRetrofit(),
                                 getDefaultResponse())
                         .takeFirst(gitHubUserListResponse -> gitHubUserListResponse
                                                              != statusCodes.GITHUB_USER_LIST_NOT_AVAILABLE)
                         .map(status -> !status.equals(statusCodes.DEFAULT_RESPONSE));
    }

    private Observable<statusCodes> getGitHubUsersFromRealm(boolean isForced) {
        return Observable.just(isForced)
                         .filter(isForcedIn -> !isForcedIn)
                         .map(__ -> (mRealmDatabaseProvider.isObjectInDatabase(GitHubUser.class))
                                 ? statusCodes.GITHUB_USER_LIST_AVAILABLE
                                 : statusCodes.GITHUB_USER_LIST_NOT_AVAILABLE);
    }

    private Observable<statusCodes> getGitHubUsersFromRetrofit() {
        return mApiInterface.getGitHubUsersList()
                            .map(gitHubUserList -> {
                                mRealmDatabaseProvider.storeUpdateObject(gitHubUserList);
                                return gitHubUserList.size() > 0
                                        ? statusCodes.GITHUB_USER_LIST_AVAILABLE
                                        : statusCodes.GITHUB_USER_LIST_NOT_AVAILABLE;
                            });
    }

    private Observable<statusCodes> getDefaultResponse() {
        return Observable.just(statusCodes.DEFAULT_RESPONSE);
    }

    public void dispose() {
        mRealmDatabaseProvider.dispose();
    }
}
