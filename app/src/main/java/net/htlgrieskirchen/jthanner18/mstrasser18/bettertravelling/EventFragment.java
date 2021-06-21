package net.htlgrieskirchen.jthanner18.mstrasser18.bettertravelling;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;


public class EventFragment extends Fragment {

    private TextView info;
    private ListView events;
    private ArrayList<String> names;
    private ArrayList<String> eventDescriptions;
    static String currentSightName;
    static String currentSightLon;
    static String currentSightLat;
    private ArrayAdapter<String> adapter;
    private static EventFragment instance;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.eventscreen, container, false);
        instance = this;
        intializeViews(view);
        return view;
    }
    private void intializeViews(View view) {
        names = new ArrayList<>();
        eventDescriptions = new ArrayList<>();
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, names);
        info = view.findViewById(R.id.event_text);
        events = view.findViewById(R.id.events);
        events.setAdapter(adapter);
        events.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String currAct = names.get(position);
                for (int i = 0; i < eventDescriptions.size(); i++) {
                    if (eventDescriptions.get(i).split(";")[0].equals(currAct)) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(EventFragment.instance.getActivity());
                        alertDialog.setTitle(currAct);
                        alertDialog.setMessage(eventDescriptions.get(i).split(";")[1]);
                        alertDialog.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                        alertDialog.show();
                    }
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            instance = this;
            names = (ArrayList<String>) savedInstanceState.getSerializable("names");
            currentSightLat = (String) savedInstanceState.getSerializable("lat");
            currentSightLon = (String) savedInstanceState.getSerializable("lon");
            currentSightName = (String) savedInstanceState.getSerializable("name");
            eventDescriptions = (ArrayList<String>) savedInstanceState.getSerializable("desc");

            if (names != null && currentSightLat != null && currentSightLon != null && currentSightName != null && eventDescriptions != null) {
                info.setText("Events in der Nähe von " + currentSightName);
                adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, names);
                events.setAdapter(adapter);
            }
        } else {
            show(view);
        }
    }

    public void show(View v) {
        info.setText("Events in der Nähe von " + currentSightName);

        GetEvents gh = new GetEvents(v.findViewById(R.id.progressBar), currentSightLon, currentSightLat, names, adapter, eventDescriptions);
        gh.execute();
    }

    public static EventFragment getInstance() {
        return instance;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        if (names != null && currentSightLat != null && currentSightLon != null && currentSightName != null) {
            savedInstanceState.putSerializable("names", names);
            savedInstanceState.putSerializable("lat", currentSightLat);
            savedInstanceState.putSerializable("lon", currentSightLon);
            savedInstanceState.putSerializable("name", currentSightName);
            savedInstanceState.putSerializable("desc", eventDescriptions);
        }
        super.onSaveInstanceState(savedInstanceState);
    }
}
