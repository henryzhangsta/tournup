package mipsmob.tournup.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import mipsmob.tournup.R;
import mipsmob.tournup.controllers.TournamentController;
import mipsmob.tournup.utilities.Callback;

public class ShareActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        Bundle extras = getIntent().getExtras();
        final String id = extras.getString("share_link", "");

        EditText link = (EditText) findViewById(R.id.share_link);
        ImageButton copyButton = (ImageButton) findViewById(R.id.copy);
        ImageButton shareButton = (ImageButton) findViewById(R.id.share);
        ImageButton startButton = (ImageButton) findViewById(R.id.start_tourny);
        TextView joinedPlayers = (TextView) findViewById(R.id.joined_players);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "onramp.ttf");

        link.setTypeface(typeface);
        joinedPlayers.setTypeface(typeface);

        link.setText(id);
        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", id);
                clipboard.setPrimaryClip(clip);

                Toast.makeText(getBaseContext(), "ID copied to clipboard!", Toast.LENGTH_SHORT).show();
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, TournamentController.BASE_URL + "tournament/" + id);
                startActivity(i);
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TournamentController.getInstance(ShareActivity.this).startTournament(id, new Callback() {
                    @Override
                    public void onDataRetrieved(Object data) {
                        Intent i = new Intent(getBaseContext(), WaitingActivity.class);
                        i.putExtra("id", id);
                        startActivity(i);
                    }

                    @Override
                    public void onRetrievalFailed() {
                        Toast.makeText(getBaseContext(), "Error in starting tournament", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}
