package mipsmob.tournup.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import mipsmob.tournup.R;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Parse.initialize(this, "22d6Ninlt3Gcjv4fXMljncmHsboYlnCx5KkMIWt8", "2udREdDAjBRKrOcAEQAEJWvqBYDaDDVzjmPHOlbA");
        ParseFacebookUtils.initialize("358751534281138");

        ParseFacebookUtils.logIn(this, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (parseUser != null) {
                    if (parseUser.isNew()) {
                        Toast.makeText(getBaseContext(), "Successfully signed in with Facebook", Toast.LENGTH_SHORT).show();
                    }
                    Intent i = new Intent(getBaseContext(), TitleActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(getBaseContext(), "Could not log into Facebook, now exiting...", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
    }

}
