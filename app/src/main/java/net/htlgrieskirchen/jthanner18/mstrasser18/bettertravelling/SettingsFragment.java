package net.htlgrieskirchen.jthanner18.mstrasser18.bettertravelling;

import androidx.appcompat.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.preference.Preference;

import androidx.preference.PreferenceFragmentCompat;


public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        Preference button = findPreference("preference_delete_lists");
        Context instance = this.getContext();
        //Delte List Preference-Button
        button.setOnPreferenceClickListener(new androidx.preference.Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(androidx.preference.Preference preference) {
                new AlertDialog.Builder(instance)
                        .setTitle("Listen löschen")
                        .setMessage("Wollen sie wirklich alle Listen löschen?")
                        .setCancelable(false)
                        .setPositiveButton("OK", (dialog, which)-> resetLists())
                        .setNeutralButton("Abbrechen", null)
                        .show();
                return  true;
            }
        });
    }

    private void resetLists() {
        LeftFragment.sights.clear();
        LeftFragment.spinnerItems.clear();
        LeftFragment.spinnerAdapter.notifyDataSetChanged();
        LeftFragment.items.clear();
        LeftFragment.adapter.notifyDataSetChanged();
    }
}
