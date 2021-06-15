package net.htlgrieskirchen.jthanner18.mstrasser18.bettertravelling;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class RightFragment extends Fragment implements View.OnClickListener {
    public final static String TAG = RightFragment.class.getSimpleName();
    private TextView info;
    private TextView address;
    private TextView coord;
    private TextView todo_Field;
    private Button showOnMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: entered");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_right, container, false);
        intializeViews(view);
        showOnMap = view.findViewById(R.id.show_on_map);
        showOnMap.setOnClickListener(this::onClick);
        return view;
    }
    private void intializeViews(View view) {
        Log.d(TAG, "intializeViews: entered");
        info = view.findViewById(R.id.info_sight);
        address = view.findViewById(R.id.address_sight);
        coord = view.findViewById(R.id.latlon_sight);
        todo_Field = view.findViewById(R.id.txtview_3);
        /*
        info.setText("Erste Zeile....");
        address.setText("Zweite Zeile....");
        coord.setText("Dritte Zeile....");
        todo_Field.setText("Vierte Zeile....");
         */
    }
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: entered");
    }

    public void show(int pos, String item) {
        Log.d(TAG, "show: entered");
        info.setText(""+pos);
        address.setText(item);
        coord.setText("0.0, 0.0");
        todo_Field.setText(item);
    }

    @Override
    public void onClick(View v) {
        String[] cords = coord.getText().toString().split(", ");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("geo:" + cords[0] + "," + cords[1] + "?z=15"));
        startActivity(intent);
    }
}
