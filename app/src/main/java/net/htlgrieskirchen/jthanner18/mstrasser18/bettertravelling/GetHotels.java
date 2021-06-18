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

public class GetHotels extends AsyncTask<String, Integer, JSONObject> {
    private String sightName;
    private String address;
    private String cords;
    private final String KEY;

    public GetHotels(String sightName, String address, String cords) {
        this.sightName = sightName;
        this.address = address;
        this.cords = cords;
        this.KEY = "";
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
            HttpURLConnection connection = (HttpURLConnection) new URL("").openConnection();
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
