package mipsmob.tournup.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import mipsmob.tournup.R;
import mipsmob.tournup.adapters.RankingsAdapter;

public class WaitingScreenActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_screen);

        TextView rankingsTitle = (TextView) findViewById(R.id.current_rankings);
        ListView rankings = (ListView) findViewById(R.id.rankings_list);

        rankingsTitle.setTypeface(Typeface.createFromAsset(getAssets(), "onramp.ttf"));

        RankingsAdapter adapter = new RankingsAdapter(this);
        rankings.setAdapter(adapter);
    }

}
