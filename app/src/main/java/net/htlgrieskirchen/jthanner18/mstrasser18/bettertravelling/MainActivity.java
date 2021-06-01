package net.htlgrieskirchen.jthanner18.mstrasser18.bettertravelling;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
<<<<<<< HEAD
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity  implements  LeftFragment.OnSelectionChangedListener{
=======
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Map;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    private static final int RQ_PREFERENCES = 1;
    private SharedPreferences prefs;
    private SharedPreferences.OnSharedPreferenceChangeListener preferencesChangeListener;
    private boolean getNotifications;
>>>>>>> 0bc748bf282425f764e5e5c3c849f36c894870ff

    private static final String TAG = MainActivity.class.getSimpleName();
    private RightFragment rightFragment;
    private boolean showRight = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
<<<<<<< HEAD
        initializeView();
    }

    private void initializeView() {
        Log.d(TAG, "initializeView: entered");
        rightFragment  = (RightFragment) getSupportFragmentManager().findFragmentById(R.id.fragRight);
        showRight = rightFragment != null && rightFragment.isInLayout();
    }

    @Override
    public void onSelectionChanged(int pos, String item) {
        if (showRight) rightFragment.show(pos, item);
        else callRightActivity(pos, item);
    }

    private void callRightActivity(int pos, String item) {
        Log.d(TAG, "callRightActivity: entered");
        Intent intent = new Intent(this, RightActivity.class);
        intent.putExtra("pos", pos);
        intent.putExtra("item", item);
        startActivity(intent);
=======

        //Preferences
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        preferencesChangeListener = ( sharedPrefs, key ) -> preferenceChanged(sharedPrefs, key);
        getNotifications = true;
        //Delte List Preference-Button
        /**
        Preference button = findPreference("preference_delete_lists");
        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //TODO
                //Listen löschen
                return true;
            }
        });
         **/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_preferences) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivityForResult(intent, RQ_PREFERENCES);
        }
        return super.onOptionsItemSelected(item);
    }

    private void preferenceChanged(SharedPreferences sharedPrefs, String key) {
        Map<String, ?> allEntries = sharedPrefs.getAll();
        boolean prevNotificationPreference = getNotifications;
        if (allEntries.get(key) instanceof String){}
        else if (allEntries.get(key) instanceof Boolean) {
            getNotifications = sharedPrefs.getBoolean("preference_get_notifications", true);
        }
        if(prevNotificationPreference && !getNotifications){
            //TODO
            //stopNotificationService();
        }else if(!prevNotificationPreference && getNotifications){
            //TODO
            //startNotificationService();
        }
        addCity = findViewById(R.id.addCity);
        listofCities = findViewById(R.id.listOfCities);
>>>>>>> 0bc748bf282425f764e5e5c3c849f36c894870ff
    }
}