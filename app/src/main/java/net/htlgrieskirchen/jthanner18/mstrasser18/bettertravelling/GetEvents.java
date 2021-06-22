package net.htlgrieskirchen.jthanner18.mstrasser18.bettertravelling;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.Activity;
import org.json.JSONObject;
import java.util.ArrayList;

public class GetEvents extends AsyncTask<String, Integer, JSONObject> {
    private ProgressBar mProgressBar;
    private String lon;
    private String lat;
    private ArrayList<String> names;
    private ArrayAdapter<String> adapter;
    private Activity[] events;
    private ArrayList<String> eventDescriptions;

    public GetEvents(ProgressBar pb, String lon, String lat, ArrayList<String> names, ArrayAdapter<String> adapter, ArrayList<String> eventDescriptions) {
        this.mProgressBar = pb;
        this.lon = lon;
        this.lat = lat;
        this.names = names;
        this.eventDescriptions = eventDescriptions;
        this.adapter = adapter;
    }

    @Override
    protected void onPreExecute() {
        mProgressBar.setVisibility(View.VISIBLE);
        super.onPreExecute();
    }

    @Override
    protected JSONObject doInBackground(String... strings) {
        events = getEvents();
        return null;
    }


    @Override
    protected void onPostExecute(JSONObject json) {
        mProgressBar.setVisibility(View.INVISIBLE);

        for (Activity e : events) {
            eventDescriptions.add(e.getName() + ";" + e.getShortDescription());
            names.add(e.getName());
        }
        adapter.notifyDataSetChanged();
        super.onPostExecute(json);
    }

    public Activity[] getEvents(){
        Amadeus a = Amadeus.builder("oIS02vhkm9w4eyVhbyPwaPjkGUrzXBeW", "cHGezOEVzChZJSGM").build();

        try {
            Activity[] activities = a.shopping.activities.get(Params
                    .with("latitude", lat)
                    .and("longitude", lon)
                    .and("radius", 2));
            return activities;
        } catch (ResponseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
