package mipsmob.tournup.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import mipsmob.tournup.R;

public class WaitingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);

        Bundle extras = getIntent().getExtras();
        String id = extras.getString("id");

        TextView waitingText = (TextView) findViewById(R.id.waiting_text);
        waitingText.setTypeface(Typeface.createFromAsset(getAssets(), "onramp.ttf"));
        waitingText.setText("Currently participating in tournament " + id);
    }

}
