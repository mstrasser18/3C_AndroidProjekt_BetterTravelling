package net.htlgrieskirchen.jthanner18.mstrasser18.bettertravelling;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.datepicker.OnSelectionChangedListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LeftFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = LeftFragment.class.getSimpleName();
    private ListView list;
    private ArrayList<String> items = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private Button addCity;
    private Map<String, ArrayList<Sight>> sights = new HashMap<>();
    private String current_city = "";
    MainActivity ma = MainActivity.getInstance();
    private static LeftFragment instance;

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
        return view;
    }

    private void initializeViews(View view) {
        Log.d(TAG, "initializeViews: entered");
        list = view.findViewById(R.id.listOfCities);
        list.setOnItemClickListener((parent, view1, position, id) -> itemSelected(position));
    }

    private void itemSelected(int position) {
        String item = items.get(position);
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
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                /*
                GetSights gs = new GetSights(city[0]);
                gs.execute();
                try {
                    JSONObject result = gs.get();
                    // JSONObject Auswertung Todo
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                */

                // Die ArrayList ist noch als Test gedacht.
                ArrayList<Sight> s = new ArrayList<>();
                s.add(new Sight("Freiheitsstatue"));
                sights.put(current_city, s);
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
                    items.add(entry.getKey() + ": " + entry.getValue().get(i).getName());
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    public static LeftFragment getInstance() {
        return instance;
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        if (items != null) {
            savedInstanceState.putSerializable("items", items);
        }
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onViewStateRestored(@NonNull Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            items = (ArrayList<String>) savedInstanceState.getSerializable("items");
            if (items != null) {
                ma.setData(items);
            }
        }
        /*
        if (savedInstanceState != null) {
            items = (ArrayList<String>) savedInstanceState.getSerializable("items");
            if (items != null) {
                if (ma.showRight) {
                    for (int i = 0; i < items.size(); i++) {
                        ma.rightFragment.show(i,items.get(i));
                    }
                } else {
                    for (int i = 0; i < items.size(); i++) {
                        ma.callRightActivity(i,items.get(i));
                    }
                }
            }
        }
        */
    }
}
