package mipsmob.tournup.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import mipsmob.tournup.R;
import mipsmob.tournup.controllers.TournamentController;
import mipsmob.tournup.models.Tournament;
import mipsmob.tournup.utilities.Callback;

public class JoinActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        final EditText url = (EditText) findViewById(R.id.url);
        ImageButton go = (ImageButton) findViewById(R.id.go);

        url.setTypeface(Typeface.createFromAsset(getAssets(), "onramp.ttf"));

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TournamentController.getInstance(JoinActivity.this).getTournament(url.getText().toString(), new Callback() {
                    @Override
                    public void onDataRetrieved(Object data) {
                        TournamentInfoActivity.staticTournament = (Tournament) data;
                        Intent i = new Intent(getBaseContext(), TournamentInfoActivity.class);
                        startActivity(i);
                    }

                    @Override
                    public void onRetrievalFailed() {
                        Toast.makeText(getBaseContext(), "Not a valid tournament", Toast.LENGTH_SHORT).show();
                    }
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
