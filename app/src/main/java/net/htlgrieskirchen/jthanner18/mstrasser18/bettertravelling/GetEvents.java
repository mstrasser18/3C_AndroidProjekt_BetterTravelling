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
        /*
        checkToken();

        String sJson = "";
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("https://test.api.amadeus.com/v2/shopping/hotel-offers?latitude="+lat+"&longitude="+lon+"&radius=5&radiusUnit=KM").openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer "+token);

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
         */

        events = getEvents();
        return null;
    }


    @Override
    protected void onPostExecute(JSONObject json) {
        mProgressBar.setVisibility(View.INVISIBLE);

        /*
        JSONArray data = null;
        try {
            data = json.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                JSONObject hotel = data.getJSONObject(i).getJSONObject("hotel");
                hotelNames.add(hotel.getString("name"));
            }
        } catch(JSONException e){
            e.printStackTrace();
        }
         */

        for (Activity e : events) {
            eventDescriptions.add(e.getName() + ";" + e.getShortDescription());
            names.add(e.getName());
        }
        adapter.notifyDataSetChanged();
        super.onPostExecute(json);
    }

        /*
    private void checkToken(){
        if(token == null) {
            token = getNewToken();
            return;
        }

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("https://test.api.amadeus.com/v1/security/oauth2/token/"+token).openConnection();
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
                JSONObject json = new JSONObject(sb.toString());
                String expired = json.getString("state");
                int expires_in = json.getInt("expires_in");
                if(expired.equals("expired")||expires_in < 60){
                    token = getNewToken();
                }
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
         */

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

    /*
    private String getNewToken(){
        try {
            HttpsURLConnection connection = (HttpsURLConnection) new URL("https://test.api.amadeus.com/v1/security/oauth2/token").openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

            String query = "grant_type=client_credentials&client_id=oIS02vhkm9w4eyVhbyPwaPjkGUrzXBeW&client_secret=cHGezOEVzChZJSGM";
            OutputStream output = connection.getOutputStream();
            output.write(query.getBytes("UTF-8"));

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                JSONObject json = new JSONObject(sb.toString());
                return json.getString("access_token");
            }
        }catch (Exception e){
            return null;
        }
        return null;
    }
     */
}
