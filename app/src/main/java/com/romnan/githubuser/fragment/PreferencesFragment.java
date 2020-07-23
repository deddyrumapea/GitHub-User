package com.romnan.githubuser.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.romnan.githubuser.R;
import com.romnan.githubuser.receiver.AlarmReceiver;

import java.util.Locale;

public class PreferencesFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String ALARM_TIME = "09:00";
    private String REMINDER;
    private String LANGUAGE;

    private SwitchPreference reminderPref;
    private ListPreference languagePref;

    private AlarmReceiver alarmReceiver;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
        init();
        setSummaries();
    }

    private void init() {
        REMINDER = getResources().getString(R.string.key_reminder);
        LANGUAGE = getResources().getString(R.string.key_language);

        reminderPref = findPreference(REMINDER);
        languagePref = findPreference(LANGUAGE);
        alarmReceiver = new AlarmReceiver();
    }

    private void setSummaries() {
        SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
        reminderPref.setChecked(sharedPreferences.getBoolean(REMINDER, false));
        languagePref.setSummary(sharedPreferences.getString(LANGUAGE,
                Locale.getDefault().getDisplayLanguage()));
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(REMINDER)) {
            if (getContext() != null) {
                reminderPref.setChecked(sharedPreferences.getBoolean(REMINDER, false));
                if (reminderPref.isChecked()) {
                    alarmReceiver.setRepeatingAlarm(getContext(), AlarmReceiver.TYPE_REPEATING,
                            ALARM_TIME, getString(R.string.alarm_message));
                } else {
                    alarmReceiver.cancelAlarm(getContext());
                }
            }
        }
        if (key.equals(LANGUAGE)) {
            languagePref.setSummary(sharedPreferences.getString(LANGUAGE,
                    Locale.getDefault().getDisplayLanguage()));
        }
    }
}