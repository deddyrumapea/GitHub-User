package com.romnan.consumerapp.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.romnan.consumerapp.R;
import com.romnan.consumerapp.fragment.FollowTabFragment;

public class TabPagerAdapter extends FragmentPagerAdapter {

    private final Context mContext;

    @StringRes
    private final int[] TAB_TITLES = new int[]{
            R.string.label_followers,
            R.string.label_following
    };

    public TabPagerAdapter(Context context, FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mContext = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return FollowTabFragment.newInstance(position + 1);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return 2;
    }
}
