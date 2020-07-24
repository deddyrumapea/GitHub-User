package com.romnan.githubuser.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.romnan.githubuser.R;
import com.romnan.githubuser.fragment.PreferencesFragment;

public class PreferenceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.settings);
        }
        getSupportFragmentManager().beginTransaction()
                .add(R.id.preference_layout, new PreferencesFragment()).commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}