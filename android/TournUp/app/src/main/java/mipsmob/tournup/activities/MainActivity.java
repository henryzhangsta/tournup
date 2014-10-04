package mipsmob.tournup.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.model.GraphUser;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import java.util.Date;

import mipsmob.tournup.R;
import mipsmob.tournup.controllers.TournamentController;
import mipsmob.tournup.models.Tournament;
import mipsmob.tournup.utilities.Callback;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView loggingIn = (TextView) findViewById(R.id.logging_in);
        loggingIn.setTypeface(Typeface.createFromAsset(getAssets(), "onramp.ttf"));

        Parse.initialize(this, "22d6Ninlt3Gcjv4fXMljncmHsboYlnCx5KkMIWt8", "2udREdDAjBRKrOcAEQAEJWvqBYDaDDVzjmPHOlbA");
        ParseFacebookUtils.initialize("358751534281138");

        ParseFacebookUtils.logIn(this, new LogInCallback() {
            @Override
            public void done(final ParseUser parseUser, ParseException e) {
                if (parseUser != null) {
                    if (parseUser.isNew()) {
                        Request.newMeRequest(ParseFacebookUtils.getSession(), new Request.GraphUserCallback() {
                            @Override
                            public void onCompleted(GraphUser user, Response response) {
                                parseUser.put("name", user.getFirstName() + " " + user.getLastName());
                                parseUser.saveInBackground();

                                Toast.makeText(getBaseContext(), "Successfully signed in with Facebook", Toast.LENGTH_SHORT).show();

                                Intent i = new Intent(getBaseContext(), TitleActivity.class);
                                startActivity(i);
                                finish();
                            }
                        }).executeAsync();

                    } else {
                        TournamentController.getInstance(MainActivity.this).createTournament(new Tournament("Smash bros", "roundrobin", new Date(), "Berkeley", parseUser, "5.00", true, 6), new Callback() {
                            @Override
                            public void onDataRetrieved(Object data) {
                                System.out.println((String) data);
                            }

                            @Override
                            public void onRetrievalFailed() {

                            }
                        });


                        Intent i = new Intent(getBaseContext(), TitleActivity.class);
                        startActivity(i);
                        finish();
                    }
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
