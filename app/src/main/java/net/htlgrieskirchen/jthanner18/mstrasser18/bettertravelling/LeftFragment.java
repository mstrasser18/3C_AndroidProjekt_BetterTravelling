package net.htlgrieskirchen.jthanner18.mstrasser18.bettertravelling;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.datepicker.OnSelectionChangedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class LeftFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = LeftFragment.class.getSimpleName();
    private ListView list;
    static ArrayList<String> items = new ArrayList<>();
    static ArrayList<String> spinnerItems = new ArrayList<>();
    static ArrayAdapter<String> adapter;
    static ArrayAdapter<String> spinnerAdapter;
    private Button addCity;
    static Spinner dropdown;
    static Map<String, ArrayList<Sight>> sights = new HashMap<>();
    private String current_city = "";
    MainActivity ma = MainActivity.getInstance();
    private static LeftFragment instance;

    private final String KEY = "AIzaSyClUs4MaprwwKPIqcnE4Dd8PzKxO4vYQhE";

    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach: entered");
        super.onAttach(context);
        instance = this;
        if (context instanceof OnSelectionChangedListener) {
            listener = (OnSelectionChangedListener) context;
        } else {
            Log.d(TAG, "onAttach: Activity does not implement OnSelectionChangedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: entered");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_left, container, false);
        initializeViews(view);
        addCity = view.findViewById(R.id.addCity);
        addCity.setOnClickListener(this::onClick);
        dropdown = view.findViewById(R.id.spinnerLists);
        spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, spinnerItems);
        dropdown.setAdapter(spinnerAdapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                current_city = spinnerItems.get(position);
                refreshAdapter(sights);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    private void initializeViews(View view) {
        Log.d(TAG, "initializeViews: entered");
        list = view.findViewById(R.id.listOfCities);
        list.setOnItemClickListener((parent, view1, position, id) -> itemSelected(position));
    }

    private void itemSelected(int position) {
        String sight = items.get(position);
        String item = "";
        for (int i = 0; i < sights.get(current_city).size(); i++) {
            if (sights.get(current_city).get(i).getName().equals(sight)) {
                item += sights.get(current_city).get(i).getName() + ";" + sights.get(current_city).get(i).getAddress() + ";" + (sights.get(current_city).get(i).getLat() + ", " + sights.get(current_city).get(i).getLon()) + ";" + sights.get(current_city).get(i).getRating() + ";" + sights.get(current_city).get(i).getPicLink();
            }
        }
        listener.onSelectionChanged(position, item);
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart: entered");
        super.onStart();
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, items);
        list.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        showDialog(v);
    }

    public interface OnSelectionChangedListener {
        void onSelectionChanged( int pos, String item);
    }

    private OnSelectionChangedListener listener;


    private void showDialog(View v) {
        ArrayList<Sight> sight = new ArrayList<>();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                GetSights gs = new GetSights(current_city);
                gs.execute();
                try {
                    JSONObject result = gs.get();
                    JSONArray results = result.getJSONArray("results");
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject jo = results.getJSONObject(i);
                        String name = jo.getString("name");
                        String address = jo.getString("formatted_address");
                        double lat = jo.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                        double lon = jo.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                        double rating = jo.getDouble("rating");
                        int height = jo.getJSONArray("photos").getJSONObject(0).getInt("height");
                        int width = jo.getJSONArray("photos").getJSONObject(0).getInt("width");
                        String reference = jo.getJSONArray("photos").getJSONObject(0).getString("photo_reference");
                        sight.add(new Sight(name, address, lat, lon, rating, ("https://maps.googleapis.com/maps/api/place/photo?photoreference=" + reference + "&sensor=false&maxheight=" + height + "&maxwidth=" + width + "&key=" + KEY)));
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                sights.put(current_city, sight);
                boolean exists = false;
                for (int i = 0; i < spinnerItems.size(); i++) {
                    if (spinnerItems.get(i).equals(current_city)) {
                        exists = true;
                    }
                }
                if (!exists) {
                    spinnerItems.add(current_city);
                    spinnerAdapter.notifyDataSetChanged();
                }
                dropdown.setSelection(spinnerItems.indexOf(current_city));
                refreshAdapter(sights);
            }
        };
        userInput(runnable, v);
    }

    private void userInput(final Runnable func, View v) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LeftFragment.instance.getActivity());
        alertDialog.setTitle("Stadt auswählen");
        alertDialog.setMessage("Wählen Sie eine Stadt aus, zu der Sehenswürdigkeiten ausgegeben werden sollen.");

        final EditText input = new EditText(v.getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);

        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        current_city = input.getText().toString();
                        func.run();
                    }
                });

        alertDialog.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        current_city = "";
                    }
                });

        alertDialog.show();
    }

    public void refreshAdapter(Map<String, ArrayList<Sight>> sights) {
        items.clear();
        for (Map.Entry<String, ArrayList<Sight>> entry : sights.entrySet()) {
            if (entry.getKey().equals(current_city)) {
                for (int i = 0; i < entry.getValue().size(); i++) {
                    items.add(entry.getValue().get(i).getName());
                }
            }
        }
        adapter.notifyDataSetChanged();
        NotificationService.setData(sights);
    }

    public static LeftFragment getInstance() {
        return instance;
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        if (items != null && spinnerItems != null && sights != null && current_city != null) {
            savedInstanceState.putSerializable("items", items);
            savedInstanceState.putSerializable("spinneritems", spinnerItems);
            savedInstanceState.putSerializable("map", (Serializable) sights);
            savedInstanceState.putSerializable("currentcity", current_city);
        }
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onViewStateRestored(@NonNull Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            items = (ArrayList<String>) savedInstanceState.getSerializable("items");
            spinnerItems = (ArrayList<String>) savedInstanceState.getSerializable("spinneritems");
            sights = (Map<String, ArrayList<Sight>>) savedInstanceState.getSerializable("map");
            current_city = (String) savedInstanceState.getSerializable("currentcity");

            if (items != null && spinnerItems != null && sights != null && current_city != null) {
                MainActivity.setData(items, spinnerItems, sights, current_city);
            }
        }
    }
}
