package com.example.ksreeniv.realmintegration;

import com.example.ksreeniv.realmintegration.ui.UserListFragment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String FLAG_COMMIT_FRAGMENT = "UserListFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openUserListFragment();
    }

    private void openUserListFragment() {
        UserListFragment gitHubUserListFragment = (UserListFragment) getSupportFragmentManager()
                .findFragmentByTag(FLAG_COMMIT_FRAGMENT);
        if (gitHubUserListFragment == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.activity_main, new UserListFragment(), FLAG_COMMIT_FRAGMENT)
                    .commit();
        }
    }

}
