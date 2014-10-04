package mipsmob.tournup.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import mipsmob.tournup.R;


public class TitleActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

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

}
