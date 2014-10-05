package mipsmob.tournup.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageButton;

import com.parse.ParseUser;

import mipsmob.tournup.R;


public class TitleActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (!nfcAdapter.isEnabled()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Please turn on NFC before using TournUp.");
            builder.setPositiveButton("Go to Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
                    startActivity(intent);
                    finish();
                }
            });
            builder.show();
        } else if (!nfcAdapter.isNdefPushEnabled()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Please turn on Android Beam before using TournUp.");
            builder.setPositiveButton("Go to Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(Settings.ACTION_NFCSHARING_SETTINGS);
                    startActivity(intent);
                    finish();
                }
            });
            builder.show();
        }

        ImageButton hostButton = (ImageButton) findViewById(R.id.host);
        ImageButton joinButton = (ImageButton) findViewById(R.id.join);

        hostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), HostActivity.class);
                startActivity(i);
            }
        });

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), JoinActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        String currTourny = ParseUser.getCurrentUser().getString("curr_tournament");
        if (ParseUser.getCurrentUser().getString("curr_match") != null) {
            Intent i = new Intent(this, MatchActivity.class);
            startActivity(i);
            finish();
        } else if (currTourny != null) {
            Intent i = new Intent(this, WaitingActivity.class);
            i.putExtra("id", currTourny);
            startActivity(i);
            finish();
        }
    }

}
