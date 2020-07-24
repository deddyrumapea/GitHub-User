package com.romnan.githubuser.activity;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.romnan.githubuser.R;
import com.romnan.githubuser.adapter.TabPagerAdapter;
import com.romnan.githubuser.fragment.FollowTabFragment;
import com.romnan.githubuser.model.User;
import com.romnan.githubuser.widget.FavUserStackWidget;

import static android.provider.BaseColumns._ID;
import static com.romnan.githubuser.database.DatabaseContract.FavUserColumns.AVATAR;
import static com.romnan.githubuser.database.DatabaseContract.FavUserColumns.COMPANY;
import static com.romnan.githubuser.database.DatabaseContract.FavUserColumns.CONTENT_URI;
import static com.romnan.githubuser.database.DatabaseContract.FavUserColumns.FOLLOWERS;
import static com.romnan.githubuser.database.DatabaseContract.FavUserColumns.FOLLOWING;
import static com.romnan.githubuser.database.DatabaseContract.FavUserColumns.LOCATION;
import static com.romnan.githubuser.database.DatabaseContract.FavUserColumns.NAME;
import static com.romnan.githubuser.database.DatabaseContract.FavUserColumns.REPOSITORIES;
import static com.romnan.githubuser.database.DatabaseContract.FavUserColumns.USERNAME;

public class UserDetailActivity extends AppCompatActivity {
    public static final String EXTRA_USER = "extra_user";
    FloatingActionButton favoriteBtn;
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
        favoriteBtn = findViewById(R.id.favorite_btn);
        viewPager = findViewById(R.id.view_pager);
        tabs = findViewById(R.id.tabs);

        setViews();

        favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFavoriteUser(user.getId());
            }
        });

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

        if (isFavoriteUser(user.getId())) {
            favoriteBtn.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                    R.drawable.ic_baseline_favorite));
        } else {
            favoriteBtn.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                    R.drawable.ic_baseline_favorite_border));
        }
    }

    private void setFavoriteUser(int id) {
        if (isFavoriteUser(id)) {
            Uri uriWithId = Uri.parse(CONTENT_URI + "/" + id);
            getContentResolver().delete(uriWithId, null, null);

            Snackbar.make(findViewById(R.id.user_detail_layout), R.string.removed_favorite,
                    Snackbar.LENGTH_SHORT).show();

            favoriteBtn.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                    R.drawable.ic_baseline_favorite_border));
        } else {
            favoriteBtn.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                    R.drawable.ic_baseline_favorite));

            ContentValues values = new ContentValues();
            values.put(_ID, user.getId());
            values.put(AVATAR, user.getAvatar());
            values.put(NAME, user.getName());
            values.put(USERNAME, user.getUsername());
            values.put(LOCATION, user.getLocation());
            values.put(COMPANY, user.getCompany());
            values.put(REPOSITORIES, user.getRepositories());
            values.put(FOLLOWERS, user.getFollowers());
            values.put(FOLLOWING, user.getFollowing());

            getContentResolver().insert(CONTENT_URI, values);
            Snackbar.make(findViewById(R.id.user_detail_layout), R.string.added_favorite,
                    Snackbar.LENGTH_SHORT).show();
        }
        updateFavStackWidget();
    }

    boolean isFavoriteUser(int id) {
        Uri uriWithId = Uri.parse(CONTENT_URI + "/" + id);
        if (uriWithId != null) {
            Cursor cursor = getContentResolver().query(uriWithId, null, null,
                    null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
        }
        return false;
    }

    private void updateFavStackWidget() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
        ComponentName thisWidget = new ComponentName(getApplicationContext(), FavUserStackWidget.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stack_view);
    }
}