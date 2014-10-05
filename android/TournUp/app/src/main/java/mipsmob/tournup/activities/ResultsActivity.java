package mipsmob.tournup.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import mipsmob.tournup.R;
import mipsmob.tournup.adapters.RankingsAdapter;
import mipsmob.tournup.controllers.TournamentController;
import mipsmob.tournup.models.ResultUser;
import mipsmob.tournup.utilities.Callback;

public class ResultsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_screen);

        Bundle extras = getIntent().getExtras();
        String id = extras.getString("results");

        TextView rankingsTitle = (TextView) findViewById(R.id.current_rankings);
        final ListView rankings = (ListView) findViewById(R.id.rankings_list);

        rankingsTitle.setTypeface(Typeface.createFromAsset(getAssets(), "onramp.ttf"));

        TournamentController.getInstance(this).getResults(id, new Callback() {
            @Override
            public void onDataRetrieved(Object data) {
                String json = (String) data;

                ResultUser[] results = new Gson().fromJson(json, ResultUser[].class);
                RankingsAdapter adapter = new RankingsAdapter(getBaseContext(), results);
                rankings.setAdapter(adapter);
            }

            @Override
            public void onRetrievalFailed() {

            }
        });
    }

}
