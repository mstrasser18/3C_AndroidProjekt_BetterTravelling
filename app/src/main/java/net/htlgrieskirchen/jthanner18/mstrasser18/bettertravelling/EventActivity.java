package net.htlgrieskirchen.jthanner18.mstrasser18.bettertravelling;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class EventActivity extends AppCompatActivity {

    Fragment mMyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mMyFragment = getSupportFragmentManager().getFragment(savedInstanceState, "myFragmentName");
        } else {
            mMyFragment = new EventFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(android.R.id.content, mMyFragment)
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "myFragmentName", mMyFragment);
    }
}
