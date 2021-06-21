package net.htlgrieskirchen.jthanner18.mstrasser18.bettertravelling;

import android.os.AsyncTask;
import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class GetToken extends AsyncTask<String, Integer, JSONObject> {

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
            HttpURLConnection connection = getPostConnectionForRegister(getPostBytes());
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
        } catch (JSONException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
        return null;
    }

    private byte[] getPostBytes() throws JSONException {
        JSONObject postParams = new JSONObject();
        // postParams.put("username", usernameReg.getText().toString());
        String body = postParams.toString();
        return body.getBytes();
    }

    private HttpURLConnection getPostConnectionForRegister(byte[] data) throws IOException {
        String rawData = "id=10";
        String type = "application/x-www-form-urlencoded";
        String encodedData = URLEncoder.encode( rawData, "UTF-8" );
        URL u = new URL("https://test.api.amadeus.com/v1/security/oauth2/token");
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty( "Content-Type", type );
        //conn.setRequestProperty( "Content-Length", String.valueOf(encodedData.length()));
        conn.setRequestProperty("grant_type", "client_credentials");
        conn.setRequestProperty("client_id", "todo");
        conn.setRequestProperty("client_secret", "todo");
        OutputStream os = conn.getOutputStream();
        os.write(encodedData.getBytes());

        /*
        HttpURLConnection connection =
                (HttpURLConnection) new URL("https://test.api.amadeus.com/v1/security/oauth2/token").openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setFixedLengthStreamingMode(data.length);
        connection.getOutputStream().write(data);
        connection.getOutputStream().flush();
         */
        return conn;
    }

    @Override
    protected void onPostExecute(JSONObject json) {
        /*
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
        */
        super.onPostExecute(json);
    }
}
