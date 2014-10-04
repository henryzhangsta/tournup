package mipsmob.tournup.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import mipsmob.tournup.R;

public class TournamentInfoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament_info);

        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TextView name = (TextView) findViewById(R.id.tourny_name);
        TextView time = (TextView) findViewById(R.id.tourny_time);
        TextView location = (TextView) findViewById(R.id.tourny_loc);
        TextView host = (TextView) findViewById(R.id.tourny_host);
        TextView format = (TextView) findViewById(R.id.tourny_format);
        TextView people = (TextView) findViewById(R.id.tourny_people);
        ImageButton joinButton = (ImageButton) findViewById(R.id.join);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "onramp.ttf");
        name.setTypeface(typeface);
        time.setTypeface(typeface);
        location.setTypeface(typeface);
        host.setTypeface(typeface);
        format.setTypeface(typeface);
        people.setTypeface(typeface);

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Join tournament.
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
