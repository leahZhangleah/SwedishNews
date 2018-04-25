package com.example.android.swedishnews;

import android.content.SharedPreferences;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public static class NewsPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.news_preference);
            Preference search = findPreference(getString(R.string.settings_query_key));
            bindPreferenceSummaryToValue(search);

            final Preference filterOffice = findPreference(getString(R.string.settings_filter_office_key));

            final Preference office = findPreference(getString(R.string.settings_office_key));
            filterOffice.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
                    Boolean isChecked = preferences.getBoolean(preference.getKey(),false);
                    if (isChecked.equals(true)){
                        bindPreferenceSummaryToValue(office);
                    }
                    return true;
                }
            });

            Preference order_by = findPreference(getString(R.string.settings_order_by_key));
            bindPreferenceSummaryToValue(order_by);

            Preference from_time = (Preference) findPreference(getString(R.string.settings_from_time_key));
            bindPreferenceSummaryToValue(from_time);

            Preference end_time = (Preference) findPreference(getString(R.string.settings_end_time_key));
            bindPreferenceSummaryToValue(end_time);

        }

        private void bindPreferenceSummaryToValue(Preference preference){
            //init preferences
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String value = preferences.getString(preference.getKey(),"");
            onPreferenceChange(preference,value);

            preference.setOnPreferenceChangeListener(this);
        }


        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String value = (String) newValue;
            if (preference instanceof ListPreference){
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(value);
                if (prefIndex >= 0){
                    CharSequence[] labels = listPreference.getEntries();
                    preference.setSummary(labels[prefIndex]);
                }
            } else {
                preference.setSummary(value);
            }
            return true;
        }
    }
}
