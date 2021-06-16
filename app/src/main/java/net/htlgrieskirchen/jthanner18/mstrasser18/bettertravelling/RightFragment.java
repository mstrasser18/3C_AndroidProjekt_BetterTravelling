package net.htlgrieskirchen.jthanner18.mstrasser18.bettertravelling;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.print.PrintHelper;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class RightFragment extends Fragment implements View.OnClickListener {
    public final static String TAG = RightFragment.class.getSimpleName();
    private TextView info;
    private TextView address;
    private TextView coord;
    private TextView rating;
    private Button showOnMap;
    private ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: entered");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_right, container, false);
        intializeViews(view);
        showOnMap = view.findViewById(R.id.show_on_map);
        showOnMap.setOnClickListener(this::onClick);
        imageView = view.findViewById(R.id.image_sight);
        return view;
    }
    private void intializeViews(View view) {
        Log.d(TAG, "intializeViews: entered");
        info = view.findViewById(R.id.info_sight);
        address = view.findViewById(R.id.address_sight);
        coord = view.findViewById(R.id.latlon_sight);
        rating = view.findViewById(R.id.rating);
    }
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: entered");
    }

    public void show(String item) {
        Log.d(TAG, "show: entered");
        String[] parts = item.split(";");
        info.setText("Informationen zu " + parts[0]);
        address.setText("Adresse: " + parts[1]);
        coord.setText("Koordinaten: " + parts[2]);
        rating.setText("Rating: " + parts[3]);
        /*
        PrintHelper photoPrinter = new PrintHelper(getActivity());
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        //Bitmap bitmap = imageView.getDrawingCache(  );
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        photoPrinter.printBitmap("test print",bitmap);
         */
    }

    @Override
    public void onClick(View v) {
        String[] cords = coord.getText().toString().split(", ");
        cords[0] = cords[0].replace("Koordinaten: ", "");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("geo:" + cords[0] + "," + cords[1] + "?z=15"));
        startActivity(intent);
    }
}
