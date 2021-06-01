package net.htlgrieskirchen.jthanner18.mstrasser18.bettertravelling;

import android.os.Bundle;
import androidx.preference.Preference;

import androidx.preference.PreferenceFragmentCompat;


public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        Preference button = findPreference("preference_delete_lists");
        //Delte List Preference-Button
        button.setOnPreferenceClickListener(new androidx.preference.Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(androidx.preference.Preference preference) {
                //TODO
                //Listen löschen
                return true;
            }
        });
    }
}
