package net.htlgrieskirchen.jthanner18.mstrasser18.bettertravelling;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class HotelActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new HotelFragment())
                .commit();
    }
}
