package net.htlgrieskirchen.jthanner18.mstrasser18.bettertravelling;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.Manifest;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements LeftFragment.OnSelectionChangedListener {

    private static final int RQ_PREFERENCES = 1;
    private SharedPreferences prefs;
    private SharedPreferences.OnSharedPreferenceChangeListener preferencesChangeListener;

    //Notifications
    static NotificationManagerCompat notificationManagerCompat;
    public static boolean getNotifications = false;
    public static final int CHANNEL_ID = 120;
    private Intent notificationsIntent;

    //GPS
    public static LocationManager lm;
    public static LocationListener ll;
    public static boolean isGpsGranted;

    private RightFragment rightFragment;
    private boolean showRight = false;
    static Map<String, ArrayList<Sight>> sights = new HashMap<>();
    static String currentCity;
    static ArrayList<String> items;
    static ArrayList<String> spinnerItems;
    private static MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeView();

        instance = this;

        //Preferences
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        preferencesChangeListener = ( sharedPrefs, key ) -> preferenceChanged(sharedPrefs, key);
        prefs.registerOnSharedPreferenceChangeListener(preferencesChangeListener);
        getNotifications = true;

        //GPS
        isGpsGranted = false;
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        checkPermissionGPS();

        //Notificaions
        notificationsIntent = new Intent(this, NotificationService.class);
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
        rightFragment  = (RightFragment) getSupportFragmentManager().findFragmentById(R.id.fragRight);
        showRight = rightFragment != null && rightFragment.isInLayout();
    }

    @Override
    public void onSelectionChanged(int pos, String item) {
        if (showRight) rightFragment.show(item);
        else callRightActivity(item);
    }

    public void callRightActivity(String item) {
        Intent intent = new Intent(this, RightActivity.class);
        intent.putExtra("item", item);
        startActivity(intent);
    }

    public static void setData(ArrayList<String> items1, ArrayList<String> spinnerItems1, Map<String, ArrayList<Sight>> sights1, String city) {
        items = items1;
        spinnerItems = spinnerItems1;
        sights = sights1;
        currentCity = city;
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
        if (items != null && spinnerItems != null && sights != null && currentCity != null) {
            savedInstanceState.putSerializable("items", items);
            savedInstanceState.putSerializable("spinneritems", spinnerItems);
            savedInstanceState.putSerializable("map", (Serializable) sights);
            savedInstanceState.putSerializable("currentcity", currentCity);
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
            currentCity = (String) savedInstanceState.getSerializable("currentcity");

            if (items != null && spinnerItems != null && sights != null && currentCity != null) {
                if (showRight) {
                    for (int i = 0; i < sights.get(currentCity).size(); i++) {
                        rightFragment.show(sights.get(currentCity).get(i).getName() + ";"
                                + sights.get(currentCity).get(i).getAddress() + ";"
                                + (sights.get(currentCity).get(i).getLat() + ", " + sights.get(currentCity).get(i).getLon()) + ";"
                                + sights.get(currentCity).get(i).getRating() + ";");
                    }
                } else {
                    for (int i = 0; i < sights.get(currentCity).size(); i++) {
                        callRightActivity(sights.get(currentCity).get(i).getName() + ";"
                                + sights.get(currentCity).get(i).getAddress() + ";"
                                + (sights.get(currentCity).get(i).getLat() + ", " + sights.get(currentCity).get(i).getLon()) + ";"
                                + sights.get(currentCity).get(i).getRating() + ";");
                    }
                }
            }
        }
    }

    //Notifications
    public void startNotificationService() {
        startService(notificationsIntent);
    }

    public void stopNotificationService() {
        stopService(notificationsIntent);
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
                    .setMessage("Sie werden nicht ??ber nahe Sehensw??rdigkeiten benachrichtigt.")
                    .setNeutralButton("Ok", null)
                    .show();
        } else {
            gpsGranted();
        }
    }

    private void gpsGranted() {
        isGpsGranted = true;
        LocationListener ll = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
            }
        };
    }
}