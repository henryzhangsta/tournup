package mipsmob.tournup.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

import java.util.Date;

import mipsmob.tournup.R;
import mipsmob.tournup.controllers.TournamentController;
import mipsmob.tournup.models.Tournament;
import mipsmob.tournup.utilities.Callback;

public class HostDetailsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_details);

        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Bundle extras = getIntent().getExtras();
        final String format = extras.getString("format");

        TextView entryFeeLabel = (TextView) findViewById(R.id.entry_fee_label);
        TextView prizeLabel = (TextView) findViewById(R.id.prize_label);
        TextView locationLabel = (TextView) findViewById(R.id.location_label);
        TextView nameLabel = (TextView) findViewById(R.id.name_label);

        final EditText entryFee = (EditText) findViewById(R.id.cost);
        final EditText prize = (EditText) findViewById(R.id.prize_percentage);
        final EditText location = (EditText) findViewById(R.id.location);
        final EditText name = (EditText) findViewById(R.id.name);

        ImageButton createTournyButton = (ImageButton) findViewById(R.id.create_tourny);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "onramp.ttf");

        entryFeeLabel.setTypeface(typeface);
        prizeLabel.setTypeface(typeface);
        locationLabel.setTypeface(typeface);
        nameLabel.setTypeface(typeface);
        entryFee.setTypeface(typeface);
        prize.setTypeface(typeface);
        location.setTypeface(typeface);
        name.setTypeface(typeface);

        createTournyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Tournament tournament = new Tournament(
                        name.getText().toString(),
                        format,
                        new Date(),
                        location.getText().toString(),
                        ParseUser.getCurrentUser(),
                        entryFee.getText().toString(),
                        Integer.parseInt(entryFee.getText().toString()) != 0,
                        20
                );

                TournamentController.getInstance(HostDetailsActivity.this).createTournament(tournament, new Callback() {
                    @Override
                    public void onDataRetrieved(Object data) {
                        tournament.setId((String) data);
                        Toast.makeText(getBaseContext(), "Tournament successfully created!", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onRetrievalFailed() {}
                });
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