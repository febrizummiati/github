package com.febrizummiati.githubuserfinal.adapter;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.febrizummiati.githubuserfinal.R;
import com.febrizummiati.githubuserfinal.fragment.FollowerFragment;
import com.febrizummiati.githubuserfinal.fragment.FollowingFragment;
import com.febrizummiati.githubuserfinal.model.User;


public class AdapterFragment extends FragmentPagerAdapter {

    private final Context sContext;
    private final User user;

    public AdapterFragment(Context mContext, FragmentManager fm, User user) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.sContext = mContext;
        this.user = user;
    }

    @StringRes
    private final int[] TAB_TITLES = new int[]{
            R.string.followers,
            R.string.following
    };

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new FollowerFragment();
                Bundle bundleFollowers = new Bundle();
                bundleFollowers.putString(FollowerFragment.EXTRA_FOLLOWERS, user.getLogin());
                fragment.setArguments(bundleFollowers);
                break;

            case 1:
                fragment = new FollowingFragment();
                Bundle bundleFollowing = new Bundle();
                bundleFollowing.putString(FollowingFragment.EXTRA_FOLLOWING, user.getLogin());
                fragment.setArguments(bundleFollowing);
                break;
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return sContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return 2;
    }
}
