package net.htlgrieskirchen.jthanner18.mstrasser18.bettertravelling;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationChannelCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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

import java.io.Serializable;
import java.lang.reflect.Array;
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

    //Notifications
    static NotificationManagerCompat notificationManagerCompat;
    public static boolean getNotifications = false;
    public static final int CHANNEL_ID = 120;

    //GPS
    public static LocationManager lm;
    public static LocationListener ll;
    public static boolean isGpsGranted;

    private static final String TAG = MainActivity.class.getSimpleName();
    private RightFragment rightFragment;
    private boolean showRight = false;
    static Map<String, ArrayList<Sight>> sights = new HashMap<>();
    // private String currentCity;
    private ArrayList<String> items;
    private ArrayList<String> spinnerItems;
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

        //GPS
        isGpsGranted = false;
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        checkPermissionGPS();

        //Notificaions
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(String.valueOf(CHANNEL_ID), "channel", importance);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

        notificationManagerCompat = NotificationManagerCompat.from(this);
        if(getNotifications){
            startNotificationService();
        }
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

    public void setData(ArrayList<String> items, ArrayList<String> spinnerItems, Map<String, ArrayList<Sight>> sights) {
        this.items = items;
        this.spinnerItems = spinnerItems;
        this.sights = sights;
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
            stopNotificationService();
        }else if(!prevNotificationPreference && getNotifications){
            startNotificationService();
        }
    }

    public static MainActivity getInstance() {
        return instance;
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        if (items != null && spinnerItems != null) {
            savedInstanceState.putSerializable("items", items);
            savedInstanceState.putSerializable("spinneritems", spinnerItems);
            savedInstanceState.putSerializable("map", (Serializable) sights);
        }
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            items = (ArrayList<String>) savedInstanceState.getSerializable("items");
            spinnerItems = (ArrayList<String>) savedInstanceState.getSerializable("spinneritems");
            sights = (Map<String, ArrayList<Sight>>) savedInstanceState.getSerializable("map");

            if (items != null && spinnerItems != null && sights != null) {
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

    //Notifications
    public void startNotificationService() {
        Intent intent = new Intent(this, NotificationService.class);
        startService(intent);
    }

    public void stopNotificationService() {
        Intent intent = new Intent(this, NotificationService.class);
        stopService(intent);
    }

    //GPS
    private void checkPermissionGPS() {
        String permission = Manifest.permission.ACCESS_FINE_LOCATION;
        if (ActivityCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, 321);
        } else {
            gpsGranted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != 321) return;
        if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            new AlertDialog.Builder(this)
                    .setTitle("GPS verweigert")
                    .setMessage("Sie werden nicht über nahe Sehenswürdigkeiten benachrichtigt.")
                    .setNeutralButton("Ok", null)
                    .show();
        } else {
            gpsGranted();
        }
    }

    private void gpsGranted(){
        isGpsGranted = true;
        LocationListener ll = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
            }
        };
    }
}