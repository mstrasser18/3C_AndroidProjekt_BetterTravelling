package net.htlgrieskirchen.jthanner18.mstrasser18.bettertravelling;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.HotelOffer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;


public class HotelFragment extends Fragment {

    private TextView info;
    private ListView hotels;
    private ArrayList<String> hotelNames;
    static String currentSightName;
    static String currentSightLon;
    static String currentSightLat;
    private ArrayAdapter<String> adapter;
    private static HotelFragment instance;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.hotelscreen, container, false);
        intializeViews(view);
        return view;
    }
    private void intializeViews(View view) {
        hotelNames = new ArrayList<>();
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, hotelNames);
        info = view.findViewById(R.id.hotel_text);
        hotels = view.findViewById(R.id.hotels);
        hotels.setAdapter(adapter);
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            //Restore the fragment's state here
            instance = this;
            hotelNames = (ArrayList<String>) savedInstanceState.getSerializable("names");
            currentSightLat = (String) savedInstanceState.getSerializable("lat");
            currentSightLon = (String) savedInstanceState.getSerializable("lon");
            currentSightName = (String) savedInstanceState.getSerializable("name");

            if (hotelNames != null && currentSightLat != null && currentSightLon != null && currentSightName != null) {
                info.setText("Hotels in der Nähe von " + currentSightName);
                adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, hotelNames);
                hotels.setAdapter(adapter);
            }
        } else {
            show(view);
        }
    }

    public void show(View v) {
        info.setText("Hotels in der Nähe von " + currentSightName);

        GetHotels gh = new GetHotels(v.findViewById(R.id.progressBar), currentSightLon, currentSightLat, hotelNames, adapter/*, gt.get().getString("access_token")*/);
        gh.execute();
    }

    public static HotelFragment getInstance() {
        return instance;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        if (hotelNames != null && currentSightLat != null && currentSightLon != null && currentSightName != null) {
            savedInstanceState.putSerializable("names", hotelNames);
            savedInstanceState.putSerializable("lat", currentSightLat);
            savedInstanceState.putSerializable("lon", currentSightLon);
            savedInstanceState.putSerializable("name", currentSightName);
        }
        super.onSaveInstanceState(savedInstanceState);
    }
}
