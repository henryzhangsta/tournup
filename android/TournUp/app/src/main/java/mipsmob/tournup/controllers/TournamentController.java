package mipsmob.tournup.controllers;

import android.app.Activity;
import android.content.Context;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mipsmob.tournup.models.Tournament;
import mipsmob.tournup.utilities.Callback;
import mipsmob.tournup.utilities.HttpRequest;

public class TournamentController {

    private class JSONTournament {
        public String owner;
        public String name;
        public String format;
        public Date time;
        public String location;
        public Boolean paid;
        public int max_contestants;
        public String entry_cost;
        public List<Object> contestants;
    }

    public static final String BASE_URL = "http://huadianz.me:3000/api/";

    private static TournamentController instance;

    private Context context;

    public static TournamentController getInstance(Context context) {
        if (instance == null) {
            instance = new TournamentController(context);
        } else {
            instance.context = context;
        }

        return instance;
    }

    public TournamentController(Context context) {
        this.context = context;
    }

    public void createTournament(final Tournament tourny, final Callback callback) {
        new Thread() {

            @Override
            public void run() {
                JSONTournament t = new JSONTournament();
                t.owner = tourny.getHost().getObjectId();
                t.name = tourny.getName();
                t.format = tourny.getType();
                t.time = tourny.getStartTime();
                t.location = tourny.getLocation();
                t.paid = tourny.isPaid();
                t.max_contestants = tourny.getMaxPeople();
                t.entry_cost = tourny.getCost();
                t.contestants = new ArrayList<Object>();

                String jsonString = new Gson().toJson(t);
                try {
                    final String response = HttpRequest.post(BASE_URL + "tournament").header("Content-Type", "application/json").send(jsonString).body();

                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.onDataRetrieved(response);
                        }
                    });
                } catch(Exception e) {
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
