package com.romnan.consumerapp.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.tabs.TabLayout;
import com.romnan.consumerapp.R;
import com.romnan.consumerapp.adapter.TabPagerAdapter;
import com.romnan.consumerapp.fragment.FollowTabFragment;
import com.romnan.consumerapp.model.User;

public class UserDetailActivity extends AppCompatActivity {
    public static final String EXTRA_USER = "extra_user";
    ImageView userProfileImage;
    TextView tvName, tvUsername, tvLocation, tvCompany, tvRepo, tvFollowers, tvFollowing;
    ViewPager viewPager;
    TabLayout tabs;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        //Menerima extra User dari MainActivity
        user = getIntent().getParcelableExtra(EXTRA_USER);

        //Set activity actionbar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(user.getUsername());
        }

        //Menginisialisasi variabel objek untuk view-view yang ada pada UserDetailActivity
        userProfileImage = findViewById(R.id.user_profile_image);
        tvName = findViewById(R.id.user_name);
        tvUsername = findViewById(R.id.user_username);
        tvLocation = findViewById(R.id.user_location);
        tvCompany = findViewById(R.id.user_company);
        tvRepo = findViewById(R.id.user_repositories);
        tvFollowers = findViewById(R.id.user_followers);
        tvFollowing = findViewById(R.id.user_following);
        viewPager = findViewById(R.id.view_pager);
        tabs = findViewById(R.id.tabs);

        setViews();

        FollowTabFragment.USERNAME = user.getUsername();
        viewPager.setAdapter(new TabPagerAdapter(this, getSupportFragmentManager()));
        tabs.setupWithViewPager(viewPager);
    }

    private void setViews() {
        //Set setiap view yang ada agar sesuai dengan data yang diterima
        Glide
                .with(this)
                .load(user.getAvatar())
                .apply(new RequestOptions().override(250, 250))
                .circleCrop()
                .into(userProfileImage);
        tvName.setText(user.getName());
        tvUsername.setText(user.getUsername());

        if (!user.getLocation().equals("Unknown")) {
            tvLocation.setText(user.getLocation());
        } else {
            tvLocation.setVisibility(View.GONE);
        }

        if (!user.getCompany().equals("null")) {
            tvCompany.setText(user.getCompany());
        } else {
            tvCompany.setVisibility(View.GONE);
        }

        tvRepo.setText(String.valueOf(user.getRepositories()));
        tvFollowers.setText(String.valueOf(user.getFollowers()));
        tvFollowing.setText(String.valueOf(user.getFollowing()));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}