package com.febrizummiati.githubuserfinal.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.febrizummiati.githubuserfinal.R;
import com.febrizummiati.githubuserfinal.adapter.AdapterUser;
import com.febrizummiati.githubuserfinal.model.User;
import com.febrizummiati.githubuserfinal.ui.DetailActivity;
import com.febrizummiati.githubuserfinal.viewmodel.FollowerVM;

import java.util.List;

public class FollowerFragment extends Fragment {

    private ProgressBar progressBar;
    private RecyclerView rvFollowers;
    private AdapterUser adapterUser;
    private FollowerVM followerVM;

    public static final String EXTRA_FOLLOWERS = "extra_followers";

    public FollowerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_follower, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.pb_followers);
        rvFollowers = view.findViewById(R.id.rv_followers);
        rvFollowers.setLayoutManager(new LinearLayoutManager(getContext()));

        String username = getArguments().getString(EXTRA_FOLLOWERS);

        showLoading(false);

        followerVM = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(FollowerVM.class);
        followerVM.setListFollower(username, getContext());
        followerVM.getListFollower().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> list) {
                adapterUser = new AdapterUser();
                adapterUser.notifyDataSetChanged();
                adapterUser.setLists(list);

                rvFollowers.setAdapter(adapterUser);
                showLoading(false);
                adapterUser.setOnItemClickCallback((User data) -> {
                    showLoading(true);
                    Intent mIntentFollower = new Intent(getContext(), DetailActivity.class);
                    mIntentFollower.putExtra(DetailActivity.EXTRA_USER, data);
                    startActivity(mIntentFollower);
                });
            }
        });


    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        showLoading(false);
    }
}