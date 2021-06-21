package net.htlgrieskirchen.jthanner18.mstrasser18.bettertravelling;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class HotelActivity extends AppCompatActivity {

    Fragment mMyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            //Restore the fragment's instance
            mMyFragment = getSupportFragmentManager().getFragment(savedInstanceState, "myFragmentName");
        } else {
            mMyFragment = new HotelFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(android.R.id.content, mMyFragment)
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        getSupportFragmentManager().putFragment(outState, "myFragmentName", mMyFragment);
    }
}
