package com.romnan.githubuser.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.romnan.githubuser.R;
import com.romnan.githubuser.adapter.UserRecyclerViewAdapter;
import com.romnan.githubuser.database.FavUserHelper;
import com.romnan.githubuser.model.User;
import com.romnan.githubuser.viewmodel.FavUserViewModel;

import java.util.ArrayList;


public class FavUserActivity extends AppCompatActivity {
    private static final String EXTRA_STATE = "extra_state";
    private UserRecyclerViewAdapter userRecyclerViewAdapter;
    private ProgressBar progressBar;
    private TextView tvNotFound;
    private FavUserViewModel favUserViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users_list);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.favorite_users);
        }

        progressBar = findViewById(R.id.loadingBar);
        tvNotFound = findViewById(R.id.tv_not_found);
        loadFavUser();
        setRecyclerView();

        favUserViewModel.getFavUsers().observe(this, new Observer<ArrayList<User>>() {
            @Override
            public void onChanged(ArrayList<User> users) {
                progressBar.setVisibility(View.GONE);
                if (users.size() > 0) {
                    userRecyclerViewAdapter.setData(users);
                    tvNotFound.setVisibility(View.INVISIBLE);
                } else {
                    userRecyclerViewAdapter.setData(new ArrayList<User>());
                    tvNotFound.setVisibility(View.VISIBLE);
                    tvNotFound.setText(R.string.not_found);
                }
            }
        });

    }

    private void loadFavUser() {
        progressBar.setVisibility(View.VISIBLE);
        FavUserHelper favUserHelper = FavUserHelper.getInstance(getApplicationContext());
        favUserHelper.open();
        favUserViewModel = new ViewModelProvider
                (this, new ViewModelProvider.NewInstanceFactory()).get(FavUserViewModel.class);
        favUserViewModel.loadFavUser(favUserHelper);
    }

    private void setRecyclerView() {
        RecyclerView rvFavUsers = findViewById(R.id.rv_users);
        rvFavUsers.setLayoutManager(new LinearLayoutManager(this));
        rvFavUsers.setHasFixedSize(true);
        userRecyclerViewAdapter = new UserRecyclerViewAdapter();
        rvFavUsers.setAdapter(userRecyclerViewAdapter);

        //recyclerView item click callback method, open UserDetailActivity for specific user clicked
        userRecyclerViewAdapter.setOnItemClickCallback(new UserRecyclerViewAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(User data) {
                if (data != null) {
                    Intent intent = new Intent(FavUserActivity.this,
                            UserDetailActivity.class);
                    intent.putExtra(UserDetailActivity.EXTRA_USER, data);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_fav_user_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                loadFavUser();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, userRecyclerViewAdapter.getUsersList());
    }
}