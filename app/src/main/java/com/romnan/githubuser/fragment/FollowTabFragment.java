package com.romnan.githubuser.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.romnan.githubuser.R;
import com.romnan.githubuser.activity.UserDetailActivity;
import com.romnan.githubuser.adapter.UserRecyclerViewAdapter;
import com.romnan.githubuser.helper.ErrorMessageResolver;
import com.romnan.githubuser.model.User;
import com.romnan.githubuser.viewmodel.MainViewModel;

import java.util.ArrayList;

public class FollowTabFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    public static String USERNAME = "username";
    MainViewModel mainViewModel;

    public FollowTabFragment() {
        // Required empty public constructor
    }

    public static FollowTabFragment newInstance(int index) {
        FollowTabFragment fragment = new FollowTabFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.users_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Getting the index of opened tab
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }

        //Set progressbar and not found view
        final ProgressBar loadingBar = view.findViewById(R.id.loadingBar);
        loadingBar.setVisibility(View.VISIBLE);
        final TextView notFound = view.findViewById(R.id.tv_not_found);

        //Set recyclerview
        final RecyclerView rvUsers = view.findViewById(R.id.rv_users);
        rvUsers.setLayoutManager(new LinearLayoutManager(getContext()));
        final UserRecyclerViewAdapter userRecyclerViewAdapter = new UserRecyclerViewAdapter();
        userRecyclerViewAdapter.notifyDataSetChanged();
        rvUsers.setAdapter(userRecyclerViewAdapter);

        RelativeLayout accountListLayout = view.findViewById(R.id.account_list);
        accountListLayout.setBackgroundResource(R.drawable.bg_rounded_bottom_corner);

        //Set mainViewModel
        mainViewModel = new ViewModelProvider
                (this, new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);
        mainViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<ArrayList<User>>() {
            @Override
            public void onChanged(ArrayList<User> list) {
                if (list != null) {
                    loadingBar.setVisibility(View.GONE);
                    userRecyclerViewAdapter.setData(list);
                }
            }
        });

        switch (index) {
            case 1:
                mainViewModel.setFollowList(USERNAME, "followers");
                break;
            case 2:
                mainViewModel.setFollowList(USERNAME, "following");
                break;
        }

        userRecyclerViewAdapter.setOnItemClickCallback(new UserRecyclerViewAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(User data) {
                Intent intent = new Intent(getActivity(),
                        UserDetailActivity.class);
                intent.putExtra(UserDetailActivity.EXTRA_USER, data);
                startActivity(intent);
            }
        });

        mainViewModel.setOnErrorReceivingDataListener(new MainViewModel.OnErrorReceivingDataListener() {
            @Override
            public void onErrorReceivingData(int errorCode) {
                loadingBar.setVisibility(View.GONE);
                notFound.setVisibility(View.VISIBLE);
                notFound.setText(ErrorMessageResolver.getErrorMessageString(getContext(), errorCode));
            }
        });
    }
}