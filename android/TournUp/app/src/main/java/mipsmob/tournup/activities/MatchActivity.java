package mipsmob.tournup.activities;

import android.graphics.Typeface;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import mipsmob.tournup.R;

public class MatchActivity extends BaseActivity implements NfcAdapter.CreateNdefMessageCallback {

    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        final LinearLayout matchLayout = (LinearLayout) findViewById(R.id.match_layout);
        final TextView matchInfo = (TextView) findViewById(R.id.match_info);
        TextView opponent = (TextView) findViewById(R.id.opponent);
        ImageButton winButton = (ImageButton) findViewById(R.id.win_button);
        ImageButton drawButton = (ImageButton) findViewById(R.id.draw_button);
        ImageButton lossButton = (ImageButton) findViewById(R.id.loss_button);

        matchInfo.setTypeface(Typeface.createFromAsset(getAssets(), "onramp.ttf"));
        opponent.setTypeface(Typeface.createFromAsset(getAssets(), "onramp.ttf"));

        final Toast beamNotify = Toast.makeText(this, "Bump with your opponents device verify results!", Toast.LENGTH_SHORT);

        winButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result = "win";
                matchInfo.setTextColor(getResources().getColor(android.R.color.white));
                matchLayout.setBackgroundColor(getResources().getColor(R.color.dark_green));
                beamNotify.show();
            }
        });

        drawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result = "draw";
                matchInfo.setTextColor(getResources().getColor(android.R.color.black));
                matchLayout.setBackgroundColor(getResources().getColor(R.color.yellow));
                beamNotify.show();
            }
        });

        lossButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result = "loss";
                matchInfo.setTextColor(getResources().getColor(android.R.color.white));
                matchLayout.setBackgroundColor(getResources().getColor(R.color.red));
                beamNotify.show();
            }
        });

        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.setNdefPushMessageCallback(this, this);
    }

    @Override
    public void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent nfcEvent) {
        if (result != null) {
            return new NdefMessage(
                    new NdefRecord[]{NdefRecord.createMime("application/vnd.com.example.android.beam", result.getBytes())}
            );
        }
        return null;
    }

}
