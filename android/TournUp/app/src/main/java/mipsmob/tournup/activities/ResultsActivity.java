package mipsmob.tournup.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.parse.ParseUser;

import mipsmob.tournup.R;
import mipsmob.tournup.adapters.RankingsAdapter;
import mipsmob.tournup.controllers.TournamentController;
import mipsmob.tournup.models.ResultUser;
import mipsmob.tournup.utilities.Callback;

public class ResultsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_screen);

        Bundle extras = getIntent().getExtras();
        String id = extras.getString("results");

        TextView rankingsTitle = (TextView) findViewById(R.id.current_rankings);
        final ListView rankings = (ListView) findViewById(R.id.rankings_list);
        final ImageButton payout = (ImageButton) findViewById(R.id.payout_button);

        rankingsTitle.setTypeface(Typeface.createFromAsset(getAssets(), "onramp.ttf"));

        TournamentController.getInstance(this).getResults(id, new Callback() {
            @Override
            public void onDataRetrieved(Object data) {
                String json = (String) data;

                ResultUser[] results = new Gson().fromJson(json, ResultUser[].class);
                RankingsAdapter adapter = new RankingsAdapter(getBaseContext(), results);
                rankings.setAdapter(adapter);

                boolean isHost = true;
                for (ResultUser user : results) {
                    if (user.id.equals(ParseUser.getCurrentUser().getObjectId())) {
                        isHost = false;
                    }
                }

                if (isHost) {
                    payout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onRetrievalFailed() {

            }
        });

        payout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

}
