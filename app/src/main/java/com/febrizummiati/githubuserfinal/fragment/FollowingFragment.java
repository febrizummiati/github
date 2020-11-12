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
import com.febrizummiati.githubuserfinal.viewmodel.FollowingVM;

import java.util.List;

public class FollowingFragment extends Fragment {

    private ProgressBar progressBar;
    private RecyclerView rvFollowing;
    private AdapterUser adapter;
    private FollowingVM followingVM;

    public static final String EXTRA_FOLLOWING = "extra_following";

    public FollowingFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_following, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.pb_following);
        rvFollowing = view.findViewById(R.id.rv_following);
        rvFollowing.setLayoutManager(new LinearLayoutManager(getContext()));

        String username = getArguments().getString(EXTRA_FOLLOWING);

        showLoading(false);

        followingVM = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(FollowingVM.class);
        followingVM.setListFollowing(username, getContext());
        followingVM.getListFollowing().observe(getViewLifecycleOwner(), new Observer<List<User>>() {

            @Override
            public void onChanged(List<User> list) {
                adapter = new AdapterUser();
                adapter.notifyDataSetChanged();
                adapter.setLists(list);

                rvFollowing.setAdapter(adapter);
                showLoading(false);
                adapter.setOnItemClickCallback((User data) -> {
                    showLoading(true);
                    Intent mIntent = new Intent(getContext(), DetailActivity.class);
                    mIntent.putExtra(DetailActivity.EXTRA_USER, data);
                    startActivity(mIntent);
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