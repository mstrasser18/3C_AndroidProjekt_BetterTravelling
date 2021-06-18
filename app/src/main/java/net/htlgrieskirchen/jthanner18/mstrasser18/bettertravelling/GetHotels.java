package net.htlgrieskirchen.jthanner18.mstrasser18.bettertravelling;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class GetHotels extends AsyncTask<String, Integer, JSONObject> {
    private ProgressBar mProgressBar;
    private String lon;
    private String lat;
    private final String TOKEN;
    private ArrayList<String> hotelNames;
    private ArrayAdapter<String> adapter;

    public GetHotels(ProgressBar pb, String lon, String lat, ArrayList<String> hotelNames, ArrayAdapter<String> adapter) {
        this.mProgressBar = pb;
        this.lon = lon;
        this.lat = lat;
        this.TOKEN = "LoZ91R0mIq6lYRfAaAdsomHnk39x";
        this.hotelNames = hotelNames;
        this.adapter = adapter;
    }

    @Override
    protected void onPreExecute() {
        mProgressBar.setVisibility(View.VISIBLE);
        super.onPreExecute();
    }

    @Override
    protected JSONObject doInBackground(String... strings) {
        String sJson = "";
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("https://test.api.amadeus.com/v2/shopping/hotel-offers?latitude="+lat+"&longitude="+lon+"&radius=5&radiusUnit=KM").openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer "+TOKEN);

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
                System.out.println(sJson);
                return new JSONObject(sJson);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONObject json) {
        mProgressBar.setVisibility(View.INVISIBLE);

        JSONArray data = null;
        try {
            data = json.getJSONArray("data");
            for(int i = 0; i < data.length(); i++){
                JSONObject hotel = data.getJSONObject(i).getJSONObject("hotel");
                hotelNames.add(hotel.getString("name"));
            }
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        super.onPostExecute(json);
    }
}
