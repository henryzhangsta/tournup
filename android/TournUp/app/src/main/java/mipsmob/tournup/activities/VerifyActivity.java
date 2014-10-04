package mipsmob.tournup.activities;

import android.graphics.Typeface;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import mipsmob.tournup.R;

public class VerifyActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        Parcelable[] parcelableResults = getIntent().getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage ndefResult = (NdefMessage) parcelableResults[0];
        String result = new String(ndefResult.getRecords()[0].getPayload());

        TextView resultText = (TextView) findViewById(R.id.result);
        ImageButton acceptButton = (ImageButton) findViewById(R.id.accept);
        ImageButton rejectButton = (ImageButton) findViewById(R.id.reject);

        resultText.setTypeface(Typeface.createFromAsset(getAssets(), "onramp.ttf"));

        if (result.equals("win")) {
            resultText.setText("Accept this loss?");
        } else if (result.equals("draw")) {
            resultText.setText("Accept this draw?");
        } else if (result.equals("loss")) {
            resultText.setText("Accept this win?");
        }

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Send info to server.
            }
        });

        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Something...
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();

        finish();
    }

}
