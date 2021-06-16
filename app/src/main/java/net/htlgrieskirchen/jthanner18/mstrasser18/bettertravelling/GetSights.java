package net.htlgrieskirchen.jthanner18.mstrasser18.bettertravelling;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetSights extends AsyncTask<String, Integer, JSONObject> {
    private String city;
    private final String KEY;

    public GetSights(String city) {
        this.city = city;
        this.KEY = "AIzaSyClUs4MaprwwKPIqcnE4Dd8PzKxO4vYQhE";
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }


    @Override
    protected JSONObject doInBackground(String... strings) {
        String sJson = "";
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("https://maps.googleapis.com/maps/api/place/textsearch/json?query="+city+"+sehenswürdigkeiten&language=de&key="+KEY).openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                sJson = sb.toString();
                return new JSONObject(sJson);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONObject json) {
        super.onPostExecute(json);
    }
}
