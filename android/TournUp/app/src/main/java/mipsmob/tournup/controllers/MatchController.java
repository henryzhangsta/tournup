package mipsmob.tournup.controllers;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import mipsmob.tournup.utilities.Callback;
import mipsmob.tournup.utilities.HttpRequest;

public class MatchController {

    private class Match {
        public String result;
        public String winner;
    }

    public static final String BASE_URL = "http://huadianz.me:3000/api/";

    private static MatchController instance;

    private Context context;

    public static MatchController getInstance(Context context) {
        if (instance == null) {
            instance = new MatchController(context);
        } else {
            instance.context = context;
        }

        return instance;
    }

    public MatchController(Context context) {
        this.context = context;
    }

    public void updateMatch(final String id, final String result, final String winner, final String opponent, final Callback callback) {
        new Thread() {

            @Override
            public void run() {
                try {
                    Match match = new Match();
                    match.result = result;
                    match.winner = winner;
                    String jsonString = new Gson().toJson(match);
                    HttpRequest req = HttpRequest.put(BASE_URL + "match/" + id).header("Content-Type", "application/json").send(jsonString);

                    final int code = req.code();
                    final String response = req.body();
                    final String error = TournamentController.checkError(code, response);

                    if (error != null) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, code + ": " + error, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ParseUser.getCurrentUser().remove("curr_match");
                                ParseUser.getCurrentUser().saveInBackground();
                                ParseQuery<ParseUser>  userQuery = ParseUser.getQuery();
                                userQuery.getInBackground(opponent, new GetCallback<ParseUser>() {
                                    @Override
                                    public void done(ParseUser parseUser, ParseException e) {
                                        if (e == null) {
                                            parseUser.remove("curr_match");
                                            parseUser.saveInBackground();
                                        }
                                    }
                                });
                                callback.onDataRetrieved(response);
                            }
                        });
                    }
                } catch (Exception e) {
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.onRetrievalFailed();
                        }
                    });
                }
            }

        }.start();
    }

}
