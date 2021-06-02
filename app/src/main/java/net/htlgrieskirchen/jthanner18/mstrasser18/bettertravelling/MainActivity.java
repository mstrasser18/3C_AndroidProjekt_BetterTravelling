package net.htlgrieskirchen.jthanner18.mstrasser18.bettertravelling;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements LeftFragment.OnSelectionChangedListener {

    private static final int RQ_PREFERENCES = 1;
    private SharedPreferences prefs;
    private SharedPreferences.OnSharedPreferenceChangeListener preferencesChangeListener;
    private boolean getNotifications;

    private static final String TAG = MainActivity.class.getSimpleName();
    private RightFragment rightFragment;
    private boolean showRight = false;
    private Map<String, ArrayList<Sight>> sights = new HashMap<>();
    // private String currentCity;
    private ArrayList<String> items;
    private static MainActivity instance;
    // LeftFragment lf = LeftFragment.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeView();

        instance = this;

        //Preferences
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        preferencesChangeListener = ( sharedPrefs, key ) -> preferenceChanged(sharedPrefs, key);
        getNotifications = true;
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

    public void callRightActivity(int pos, String item) {
        Log.d(TAG, "callRightActivity: entered");
        Intent intent = new Intent(this, RightActivity.class);
        intent.putExtra("pos", pos);
        intent.putExtra("item", item);
        startActivity(intent);
    }

    public void setData(ArrayList<String> items) {
        this.items = items;
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
    }

    public static MainActivity getInstance() {
        return instance;
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        if (items != null) {
            savedInstanceState.putSerializable("items", items);
        }
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            items = (ArrayList<String>) savedInstanceState.getSerializable("items");
            if (items != null) {
                if (showRight) {
                    for (int i = 0; i < items.size(); i++) {
                        rightFragment.show(i, items.get(i));
                    }
                } else {
                    for (int i = 0; i < items.size(); i++) {
                        callRightActivity(i, items.get(i));
                    }
                }
            }
        }
    }
}