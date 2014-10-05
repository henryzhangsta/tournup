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
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import mipsmob.tournup.R;

public class MainActivity extends BaseActivity {

    private static MainActivity instance;

    public static void finishActivity() {
        if (instance != null) {
            instance.finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instance = this;

        TextView loggingIn = (TextView) findViewById(R.id.logging_in);
        loggingIn.setTypeface(Typeface.createFromAsset(getAssets(), "onramp.ttf"));

        ParsePush.subscribeInBackground("user_" + ParseUser.getCurrentUser().getObjectId(), new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "Something went wrong with push notifications, please try again", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    ParseFacebookUtils.initialize("358751534281138");
                    ParseFacebookUtils.logIn(MainActivity.this, new LogInCallback() {
                        @Override
                        public void done(final ParseUser parseUser, ParseException e) {
                            if (parseUser != null) {
                                Request.newMeRequest(ParseFacebookUtils.getSession(), new Request.GraphUserCallback() {
                                    @Override
                                    public void onCompleted(GraphUser user, Response response) {
                                        parseUser.put("name", user.getFirstName() + " " + user.getLastName());
                                        parseUser.saveInBackground();

                                        if (parseUser.isNew()) {
                                            Toast.makeText(getBaseContext(), "Successfully signed in with Facebook", Toast.LENGTH_SHORT).show();
                                        }

                                        Intent i = new Intent(getBaseContext(), TitleActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                }).executeAsync();
                            } else {
                                Toast.makeText(getBaseContext(), "Could not log into Facebook, now exiting...", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });
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
