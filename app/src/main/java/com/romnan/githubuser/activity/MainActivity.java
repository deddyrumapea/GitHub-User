package com.romnan.githubuser.activity;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.romnan.githubuser.R;
import com.romnan.githubuser.adapter.UserRecyclerViewAdapter;
import com.romnan.githubuser.model.User;
import com.romnan.githubuser.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final String STATE_SEARCHED = "state_searched";
    MainViewModel mainViewModel;
    private ProgressBar progressBar;
    private TextView tvNotFound;
    private boolean searched = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setup loading progress bar and not found screen
        progressBar = findViewById(R.id.loadingBar);
        tvNotFound = findViewById(R.id.tv_not_found);

        //display instruction screen
        displayInstruction(savedInstanceState);

        //Set up recyclerView
        RecyclerView rvUsers = findViewById(R.id.rv_users);
        rvUsers.setLayoutManager(new LinearLayoutManager(this));
        final UserRecyclerViewAdapter userRecyclerViewAdapter = new UserRecyclerViewAdapter();
        userRecyclerViewAdapter.notifyDataSetChanged();
        rvUsers.setAdapter(userRecyclerViewAdapter);

        // Seach bar
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        //Create searchView
        if (searchManager != null) {
            final SearchView searchView = findViewById(R.id.search_bar);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setQueryHint(getResources().getString(R.string.hint_search_user));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    mainViewModel.clearData(); //clear search result on the screen
                    tvNotFound.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    mainViewModel.searchUser(query);
                    searchView.clearFocus(); // hide keyboard
                    searched = true;
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }

        //Set up mainViewModel
        mainViewModel = new ViewModelProvider
                (this, new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);

        mainViewModel.getUser().observe(this, new Observer<ArrayList<User>>() {
            @Override
            public void onChanged(ArrayList<User> list) {
                if (list != null) {
                    userRecyclerViewAdapter.setData(list);

                    //If the list is not cleared, hide the loadingBar
                    if (!list.equals(new ArrayList<User>())) {
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }
        });

        //recyclerView item click callback method, open UserDetailActivity for specific user clicked
        userRecyclerViewAdapter.setOnItemClickCallback(new UserRecyclerViewAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(User data) {
                if (data != null) {
                    Intent intent = new Intent(MainActivity.this,
                            UserDetailActivity.class);
                    intent.putExtra(UserDetailActivity.EXTRA_USER, data);
                    startActivity(intent);
                }
            }
        });

        //set error loading data from client listener
        mainViewModel.setOnErrorReceivingDataListener(new MainViewModel.OnErrorReceivingDataListener() {
            @Override
            public void onErrorReceivingData(String errorMessage) {
                //display error message when there is an error
                progressBar.setVisibility(View.GONE);
                tvNotFound.setVisibility(View.VISIBLE);
                tvNotFound.setText(errorMessage);
                searched = false; //display instruction screen again if the state changed
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate OptionsMenu
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_SEARCHED, String.valueOf(searched));
    }

    private void displayInstruction(Bundle savedInstanceState) {
        // If search feature not yet used, display instruction, otherwise hide it
        if (savedInstanceState != null && Objects.equals(savedInstanceState.getString(STATE_SEARCHED),
                "true")) {
            tvNotFound.setVisibility(View.GONE);
            searched = true;
        } else {
            tvNotFound.setText(getString(R.string.label_search_instruction));
            tvNotFound.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.open_about:
                displayAboutDialog();
                break;
            case R.id.open_fav:
                startActivity(new Intent(MainActivity.this, FavUserActivity.class));
                break;
            case R.id.open_setting:
                startActivity(new Intent(MainActivity.this, PreferenceActivity.class));
                break;
        }
        return true;
    }

    private void displayAboutDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_about);
        ImageView profilePicture = dialog.findViewById(R.id.about_profile_image);
        Button closeBtn = dialog.findViewById(R.id.close_btn);

        Glide
                .with(profilePicture)
                .load(R.drawable.img_profile)
                .apply(new RequestOptions().override(200, 200))
                .circleCrop()
                .into(profilePicture);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }
}