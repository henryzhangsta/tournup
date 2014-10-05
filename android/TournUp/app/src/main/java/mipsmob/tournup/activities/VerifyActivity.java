package mipsmob.tournup.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

import mipsmob.tournup.R;
import mipsmob.tournup.controllers.MatchController;
import mipsmob.tournup.utilities.Callback;

public class VerifyActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        Parcelable[] parcelableResults = getIntent().getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage ndefResult = (NdefMessage) parcelableResults[0];
        String result = new String(ndefResult.getRecords()[0].getPayload());

        TextView resultText = (TextView) findViewById(R.id.result);
        final ImageButton acceptButton = (ImageButton) findViewById(R.id.accept);
        ImageButton rejectButton = (ImageButton) findViewById(R.id.reject);

        resultText.setTypeface(Typeface.createFromAsset(getAssets(), "onramp.ttf"));

        final String matchId = ParseUser.getCurrentUser().getString("curr_match");
        final String opponentId = ParseUser.getCurrentUser().getString("curr_opponent");

        final String matchResult;
        final String winner;

        if (result.equals("win")) {
            matchResult = "win";
            winner = opponentId;
            resultText.setText("Accept this loss?");
        } else if (result.equals("draw")) {
            matchResult = "draw";
            winner = "";
            resultText.setText("Accept this draw?");
        } else if (result.equals("loss")) {
            matchResult = "win";
            winner = ParseUser.getCurrentUser().getObjectId();
            resultText.setText("Accept this win?");
        } else {
            matchResult = "";
            winner = "";
        }

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MatchController.getInstance(VerifyActivity.this).updateMatch(matchId, matchResult, winner, opponentId, new Callback() {
                    @Override
                    public void onDataRetrieved(Object data) {
                        Toast.makeText(getBaseContext(), "Results received!", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onRetrievalFailed() {
                        Toast.makeText(getBaseContext(), "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), MatchActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();

        finish();
    }

}
