package mipsmob.tournup.controllers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mipsmob.tournup.R;
import mipsmob.tournup.activities.MainActivity;
import mipsmob.tournup.models.Tournament;
import mipsmob.tournup.utilities.Callback;
import mipsmob.tournup.utilities.HttpRequest;

public class TournamentController {

    private class JSONTournament {
        public String _id;
        public String owner;
        public String name;
        public String format;
        public String location;
        public Boolean paid;
        public int max_contestants;
        public String entry_cost;
        public List<Object> contestants;
        public String venmo_id;
    }

    private class User {
        public String user;
    }

    public static final String BASE_URL = "http://huadianz.me:3000/api/";
    public static final String VENMO_URL = "https://api.venmo.com/v1/oauth/authorize?client_id=2003&scope=make_payments&response_type=code";
    public static final String REDIRECT_URL = "http://huadianz.me:3000/api/venmo/oauth";

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

    public static String checkError(int code, String response) {
        if (code != 200) {
            Type mapType = new TypeToken<Map<String, String>>() {}.getType();
            final Map<String, String> error = (new Gson()).fromJson(response, mapType);

            return error.get("message");
        } else {
            return null;
        }
    }

    public TournamentController(Context context) {
        this.context = context;
    }

    public void createTournament(Tournament tourny, final Callback callback) {
        final CreationThread thread = new CreationThread(tourny, callback);

        if (tourny.isPaid()) {
            final LinearLayout mainLayout = (LinearLayout) ((Activity) context).findViewById(R.id.main_layout);
            final WebView webView = (WebView) ((Activity) context).findViewById(R.id.web_view);
            webView.setVisibility(View.VISIBLE);
            mainLayout.setVisibility(View.INVISIBLE);
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    if (url.contains(REDIRECT_URL) && !url.contains("error")) {
                        webView.setVisibility(View.GONE);
                        mainLayout.setVisibility(View.VISIBLE);
                        thread.start();
                    } else if (url.contains(REDIRECT_URL)) {
                        webView.setVisibility(View.GONE);
                        mainLayout.setVisibility(View.VISIBLE);
                        callback.onRetrievalFailed();
                    }
                }
            });

            webView.loadUrl(VENMO_URL);
        } else {
            thread.start();
        }
    }

    public void getTournament(final String id, final Callback callback) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        final HttpRequest req = HttpRequest.get(BASE_URL + "tournament/" + id);
                        final String response = req.body();
                        final String error = checkError(req.code(), response);

                        if (error != null) {
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, req.code() + ": " + error, Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            final JSONTournament JSONTourny = new Gson().fromJson(response, JSONTournament.class);

                            ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
                            userQuery.getInBackground(JSONTourny.owner, new GetCallback<ParseUser>() {
                                @Override
                                public void done(ParseUser parseUser, ParseException e) {
                                    if (e == null) {
                                        final Tournament tourny = new Tournament(JSONTourny.name, JSONTourny.format, JSONTourny.location, parseUser, JSONTourny.entry_cost, JSONTourny.paid, JSONTourny.max_contestants, JSONTourny.venmo_id);
                                        tourny.setId(JSONTourny._id);
                                        ((Activity) context).runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                callback.onDataRetrieved(tourny);
                                            }
                                        });
                                    } else {
                                        e.printStackTrace();
                                        ((Activity) context).runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                callback.onRetrievalFailed();
                                            }
                                        });
                                    }
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

    public void joinTournament(final String id, final Callback callback) {
        new Thread() {

            @Override
            public void run() {
                try {
                    User user = new User();
                    user.user = ParseUser.getCurrentUser().getObjectId();
                    String jsonUser = new Gson().toJson(user);
                    final HttpRequest req = HttpRequest.post(BASE_URL + "tournament/" + id + "/add").header("Content-Type", "application/json").send(jsonUser);
                    final String response = req.body();
                    final String error = checkError(req.code(), response);

                    if (error != null) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, req.code() + ": " + error, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ParsePush.subscribeInBackground("tournament_" + id, new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e != null) {
                                            ((Activity) context).runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    callback.onRetrievalFailed();
                                                }
                                            });
                                        } else {
                                            ParseUser.getCurrentUser().put("curr_tournament", id);
                                            ParseUser.getCurrentUser().saveInBackground();
                                            MainActivity.finishActivity();
                                            callback.onDataRetrieved(response);
                                        }
                                    }
                                });
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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

    public void getResults(final String id, final Callback callback) {
        new Thread() {

            @Override
            public void run() {
                try {
                    final HttpRequest req = HttpRequest.get(BASE_URL + "tournament/" + id + "/results");
                    final String response = req.body();
                    final String error = checkError(req.code(), response);

                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.onDataRetrieved(response);
                        }
                    });

                } catch (Exception e) {

                }
            }

        }.start();
    }

    public void startTournament(final String id, final Callback callback) {
        new Thread() {

            @Override
            public void run() {
                try {
                    final HttpRequest req = HttpRequest.post(BASE_URL + "tournament/" + id + "/start").header("Content-Type", "application/json").send("");
                    final String response = req.body();
                    final String error = checkError(req.code(), response);

                    if (error != null) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, req.code() + ": " + error, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                callback.onDataRetrieved(response);
                            }
                        });
                    }
                } catch(Exception e) {
                    e.printStackTrace();
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

    public void payout() {

    }

    private class CreationThread extends Thread {

        private Tournament tourny;
        private Callback callback;

        public CreationThread(Tournament tourny, Callback callback) {
            this.tourny = tourny;
            this.callback = callback;
        }

        @Override
        public void run() {
            JSONTournament t = new JSONTournament();
            t._id = tourny.getId();
            t.owner = tourny.getHost().getObjectId();
            t.name = tourny.getName();
            t.format = tourny.getType();
            t.location = tourny.getLocation();
            t.paid = tourny.isPaid();
            t.max_contestants = tourny.getMaxPeople();
            t.entry_cost = tourny.getCost();
            t.contestants = new ArrayList<Object>();

            String jsonString = new Gson().toJson(t);
            try {
                final HttpRequest req = HttpRequest.post(BASE_URL + "tournament").header("Content-Type", "application/json").send(jsonString);
                final String response = req.body();
                final String error = checkError(req.code(), response);

                if (error != null) {
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, req.code() + ": " + error, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.onDataRetrieved(response);
                        }
                    });
                }
            } catch(Exception e) {
                e.printStackTrace();
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callback.onRetrievalFailed();
                    }
                });
            }
        }

    }

}
