package mipsmob.tournup.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.Parse;

import mipsmob.tournup.R;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Parse.initialize(this, "22d6Ninlt3Gcjv4fXMljncmHsboYlnCx5KkMIWt8", "2udREdDAjBRKrOcAEQAEJWvqBYDaDDVzjmPHOlbA");

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
//        if (sharedPref.
    }

}
