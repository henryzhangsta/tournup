package mipsmob.tournup.activities;

import android.graphics.Typeface;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import mipsmob.tournup.R;

public class MatchActivity extends BaseActivity implements NfcAdapter.CreateNdefMessageCallback {

    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        TextView matchInfo = (TextView) findViewById(R.id.match_info);
        TextView opponent = (TextView) findViewById(R.id.opponent);
        ImageButton winButton = (ImageButton) findViewById(R.id.win_button);
        ImageButton drawButton = (ImageButton) findViewById(R.id.draw_button);
        ImageButton lossButton = (ImageButton) findViewById(R.id.loss_button);

        matchInfo.setTypeface(Typeface.createFromAsset(getAssets(), "onramp.ttf"));
        opponent.setTypeface(Typeface.createFromAsset(getAssets(), "onramp.ttf"));

        winButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result = "win";
            }
        });

        drawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result = "draw";
            }
        });

        lossButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result = "loss";
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
