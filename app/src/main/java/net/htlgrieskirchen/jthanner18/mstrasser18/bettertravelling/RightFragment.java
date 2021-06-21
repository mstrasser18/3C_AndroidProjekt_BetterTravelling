package net.htlgrieskirchen.jthanner18.mstrasser18.bettertravelling;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class RightFragment extends Fragment implements View.OnClickListener {
    private TextView info;
    private TextView address;
    private TextView coord;
    private TextView rating;
    private Button showOnMap;
    private Button showEvents;
    private ImageView imageView;
    private static RightFragment instance;
    private static final int RQ_PREFERENCES = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_right, container, false);
        intializeViews(view);
        instance = this;
        showOnMap = view.findViewById(R.id.show_on_map);
        showOnMap.setOnClickListener(this::onClick);
        showEvents = view.findViewById(R.id.find_events);
        showEvents.setOnClickListener(this::onClick);
        imageView = view.findViewById(R.id.image_sight);
        return view;
    }
    private void intializeViews(View view) {
        info = view.findViewById(R.id.info_sight);
        address = view.findViewById(R.id.address_sight);
        coord = view.findViewById(R.id.latlon_sight);
        rating = view.findViewById(R.id.rating);
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    public void show(String item) {
        String[] parts = item.split(";");
        info.setText(parts[0]);
        address.setText("Adresse: " + parts[1]);
        coord.setText("Koordinaten: " + parts[2]);
        rating.setText("Rating: " + parts[3]);
        showOnMap.setVisibility(View.VISIBLE);
        showEvents.setVisibility(View.VISIBLE);

        new Thread(new Runnable() {
            public void run() {
                imageView.post(new Runnable() {
                    public void run() {
                        Picasso.get().load(parts[4]).into(imageView);
                    }
                });
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.show_on_map:
                String[] cords = coord.getText().toString().split(", ");
                cords[0] = cords[0].replace("Koordinaten: ", "");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("geo:" + cords[0] + "," + cords[1] + "?z=15"));
                startActivity(intent);
                break;
            case R.id.find_events:
                Intent eventIntent = new Intent(RightFragment.instance.getActivity(), EventActivity.class);
                startActivityForResult(eventIntent, RQ_PREFERENCES);
                EventFragment.currentSightName = info.getText().toString();
                EventFragment.currentSightLat = coord.getText().toString().split(", ")[0].replace("Koordinaten: ", "");
                EventFragment.currentSightLon = coord.getText().toString().split(", ")[1];
        }
    }

    public static RightFragment getInstance() {
        return instance;
    }
}
