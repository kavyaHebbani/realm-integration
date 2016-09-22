package com.example.ksreeniv.realmintegration;

import com.example.ksreeniv.realmintegration.data.GitHubUserListDataSource;
import com.example.ksreeniv.realmintegration.ui.UserListFragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ksreeniv on 21/09/16.
 */

public class UserListPresenter {

    private WeakReference<UserListFragment> mUserListFragment;
    private Subscription mViewSubscription;
    private Subscription mDataSubscription;
    private GitHubUserListDataSource mGitHubUserListDataSource;
    private boolean mIsViewLoadedAtLeastOnce;
    private boolean mAreWeLoadingSomething;

    public UserListPresenter(GitHubUserListDataSource gitHubUserListDataSource) {
        mGitHubUserListDataSource = gitHubUserListDataSource;
        mIsViewLoadedAtLeastOnce = false;
        mAreWeLoadingSomething = false;
    }

    public void bind(UserListFragment gitHubUserListFragment) {
        mUserListFragment = new WeakReference<>(gitHubUserListFragment);

        if (!mIsViewLoadedAtLeastOnce || mAreWeLoadingSomething) {
            getUserListFragment().startRefreshAnimation();
        }

        if (mViewSubscription == null || mViewSubscription.isUnsubscribed()) {
            mViewSubscription = mGitHubUserListDataSource
                    .getUserListFromDatabase()
                    .subscribe(getUserListFragment()::setDataList,
                               error -> getUserListFragment().stopRefreshAnimation());
        }

        if (!mIsViewLoadedAtLeastOnce && !mAreWeLoadingSomething) {
            loadGitHubUserList(false);
        }
    }

    public void loadGitHubUserList(boolean isForced) {
        if (shouldLoadData(isForced)) {
            /* This is not needed here, since pullToRefresh will not trigger onRefresh()
               a second time as long as we don't stop the animation, this is to demonstrate
               how it can be done */
            if (mDataSubscription != null && !mDataSubscription.isUnsubscribed()) {
                mDataSubscription.unsubscribe();
            }

            mAreWeLoadingSomething = true;

            mDataSubscription = mGitHubUserListDataSource
                    .fetchGitHubUsersStatus(isForced)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                                   if (!response) {
                                       forceFetchGitHubUsers();
                                   } else {
                                       mIsViewLoadedAtLeastOnce = true;
                                   }

                                   getUserListFragment().stopRefreshAnimation();
                                   mAreWeLoadingSomething = false;
                               },
                               error -> {
                                   forceFetchGitHubUsers();
                                   getUserListFragment().stopRefreshAnimation();
                                   mAreWeLoadingSomething = false;
                               });
        }
    }

    private boolean shouldLoadData(boolean isForced) {
        return mDataSubscription == null
               || mDataSubscription.isUnsubscribed()
               || isForced;
    }

    public void unBind() {
        if (mViewSubscription != null && !mViewSubscription
                .isUnsubscribed()) {
            mViewSubscription.unsubscribe();
        }

        this.mUserListFragment = null;
    }

    private void forceFetchGitHubUsers() {
        if (!mIsViewLoadedAtLeastOnce) {
            getUserListFragment().setDataList(new ArrayList<>());
            getUserListFragment().startRefreshAnimation();
            loadGitHubUserList(true);
        }
    }

    public void unSubscribe() {
        if (mDataSubscription != null && !mDataSubscription.isUnsubscribed()) {
            mDataSubscription.unsubscribe();
        }

        mGitHubUserListDataSource.dispose();
    }

    private UserListFragment getUserListFragment() {
        return mUserListFragment.get();
    }

}
