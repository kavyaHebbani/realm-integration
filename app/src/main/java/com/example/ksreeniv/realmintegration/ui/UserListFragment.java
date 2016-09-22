package com.example.ksreeniv.realmintegration.ui;

import com.example.ksreeniv.realmintegration.MainApplication;
import com.example.ksreeniv.realmintegration.R;
import com.example.ksreeniv.realmintegration.UserListPresenter;
import com.example.ksreeniv.realmintegration.model.GitHubUser;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by ksreeniv on 21/09/16.
 */

public class UserListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @Inject
    UserListPresenter mUserListPresenter;

    @Inject
    UserListAdapter mUserListAdapter;

    @BindView(R.id.mainRecyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.refresh_list)
    SwipeRefreshLayout mPullToRefreshLayout;

    private Unbinder mUnbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        ((MainApplication) getActivity().getApplication()).getApplicationComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_list_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);
        mUnbinder = ButterKnife.bind(this, view);

        // pullToRefresh
        mPullToRefreshLayout.setOnRefreshListener(this);
        mPullToRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mPullToRefreshLayout.canChildScrollUp();

        // Recycler view
        initializeRecyclerView();

        mUserListPresenter.bind(this);
    }

    private void initializeRecyclerView() {
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mUserListAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (llm.findFirstCompletelyVisibleItemPosition() == 0) {
                    mPullToRefreshLayout.setEnabled(true);
                } else {
                    mPullToRefreshLayout.setEnabled(false);
                }
            }
        });
    }

    public void startRefreshAnimation() {
        mPullToRefreshLayout.post(() -> mPullToRefreshLayout.setRefreshing(true));
    }

    public void stopRefreshAnimation() {
        mPullToRefreshLayout.post(() -> mPullToRefreshLayout.setRefreshing(false));
    }

    @Override
    public void onRefresh() {
        /* load fresh data, when pullToRefresh is called */
        mUserListPresenter.loadGitHubUserList(true);
    }

    public void setDataList(List<GitHubUser> gitHubUserList) {
        mUserListAdapter.setDataList(gitHubUserList);
    }

    @Override
    public void onDestroyView() {
        mUnbinder.unbind();
        mUserListPresenter.unBind();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        mUserListPresenter.unSubscribe();
        mUserListAdapter.reset();
        super.onDestroy();
    }

}
