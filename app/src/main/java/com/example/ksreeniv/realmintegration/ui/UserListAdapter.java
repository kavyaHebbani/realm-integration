package com.example.ksreeniv.realmintegration.ui;

import com.example.ksreeniv.realmintegration.R;
import com.example.ksreeniv.realmintegration.model.GitHubUser;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ksreeniv on 21/09/16.
 */

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.GitHubViewHolder> {

    private Context context;
    private List<GitHubUser> gitHubUserList;
    private OnItemClickListener mItemClickListener;

    public UserListAdapter(Context context) {
        this.context = context;
        gitHubUserList = new ArrayList<>();
    }

    @Override
    public GitHubViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.recycler_item, viewGroup, false);
        return new GitHubViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GitHubViewHolder gitHubViewHolder, int position) {
        if (gitHubUserList.isEmpty()) {
            return;
        }

        gitHubViewHolder.vUserLogin.setText(gitHubUserList.get(position).getLogin());
        gitHubViewHolder.vUserId.setText(String.format(context.getString(R.string.userId),
                                                       gitHubUserList.get(position).getId()));
        Picasso.with(context)
               .load(gitHubUserList.get(position).getAvatar_url())
               .placeholder(R.mipmap.ic_launcher)
               .resize(48, 48)
               .centerCrop()
               .into(gitHubViewHolder.vUserIcon);
    }

    @Override
    public int getItemCount() {
        if (gitHubUserList.isEmpty()) {
            return 1;
        }
        return gitHubUserList.size();
    }

    public void setDataList(List<GitHubUser> gitHubUserList) {
        this.gitHubUserList.clear();
        this.gitHubUserList.addAll(gitHubUserList);
        notifyDataSetChanged();
    }

    public void reset() {
        this.gitHubUserList.clear();
    }

    /* viewHolder */
    class GitHubViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView vUserIcon;
        TextView vUserLogin;
        TextView vUserId;

        GitHubViewHolder(View v) {
            super(v);

            if (gitHubUserList.isEmpty()) {
                return;
            }

            vUserIcon = (ImageView) v.findViewById(R.id.user_icon);
            vUserLogin = (TextView) v.findViewById(R.id.user_name);
            vUserId = (TextView) v.findViewById(R.id.user_type);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, gitHubUserList.get(getAdapterPosition()));
            }
        }
    }

    /* onClick listener */
    public interface OnItemClickListener {

        void onItemClick(View v, GitHubUser gitHubUser);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mItemClickListener = onItemClickListener;
    }
}
