package net.htlgrieskirchen.jthanner18.mstrasser18.bettertravelling;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.util.Size;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotificationService extends IntentService {

    double longitude = 0.0;
    double latitude = 0.0;

    int notificationId = 1;

    boolean b = true;

    static Map<String, ArrayList<Sight>> sights = new HashMap<>();


    public NotificationService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        while (true) {
            if(!MainActivity.getNotifications){
                stopSelf();
                return;
            }
            try {
                ArrayList<Sight> e;
                if(MainActivity.isGpsGranted){
                    e = checkIfNear();
                    if(!e.isEmpty())postNotification(e);
                }
                Thread.sleep(10000);
            } catch (Exception e) {
                return;
            }

        }
    }

    private ArrayList<Sight> checkIfNear(){
        ArrayList<Sight> out = new ArrayList<>();
        getCurrentLocation();

        for (Map.Entry<String, ArrayList<Sight>> entry : sights.entrySet()) {
            ArrayList<Sight> arr = entry.getValue();
            for(Sight s: arr){
                if(isNear(s) && !s.getUserNotified()){
                    out.add(s);
                    s.setUserNotified();
                }
            }
            LeftFragment.sights.put(entry.getKey(), arr);
        }

        return out;
    }

    private void postNotification(ArrayList<Sight> e) {
        NotificationManagerCompat notificationManager = MainActivity.notificationManagerCompat;

        NotificationCompat.Builder builder = null;

        for(Sight s: e) {
            builder = new NotificationCompat.Builder(this, String.valueOf(MainActivity.CHANNEL_ID))
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher))
                    .setContentTitle("Nahe Sehenswürdigkeit")
                    .setContentText(s.toString())
                    .setWhen(System.currentTimeMillis())
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true);
            notificationManager.notify(notificationId, builder.build());
            notificationId++;
        }
    }

    private void getCurrentLocation(){
        Location l = null;
        try{
            l = MainActivity.lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(l != null){
                longitude = l.getLongitude();
                latitude = l.getLatitude();
            }
        }catch (SecurityException e){
            e.printStackTrace();
        }
    }

    private boolean isNear(Sight e){
        Location current = new Location("current");
        current.setLatitude(latitude);
        current.setLongitude(longitude);
        Location sight = new Location("sight");
        sight.setLatitude(e.getLat());
        sight.setLongitude(e.getLon());

        //distance in km
        float distance = current.distanceTo(sight)/1000;
        if(distance <= 5){
            return true;
        }
        return false;
    }

    public static void setData(Map<String, ArrayList<Sight>> sights1) {
        sights = sights1;
    }
}
