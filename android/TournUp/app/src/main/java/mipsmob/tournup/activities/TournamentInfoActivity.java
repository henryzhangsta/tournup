package mipsmob.tournup.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import mipsmob.tournup.R;
import mipsmob.tournup.controllers.TournamentController;
import mipsmob.tournup.models.Tournament;
import mipsmob.tournup.utilities.Callback;
import mipsmob.tournup.venmo.VenmoLibrary;
import mipsmob.tournup.venmo.VenmoWebViewActivity;

public class TournamentInfoActivity extends BaseActivity {

    public static final int VENMO_RESULT = 1;

    public static Tournament staticTournament;
    private Tournament tournament;

    private TextView name;
    private TextView location;
    private TextView host;
    private TextView format;
    private TextView people;
    private TextView entryFee;
    private ImageButton joinButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament_info);

        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        name = (TextView) findViewById(R.id.tourny_name);
        location = (TextView) findViewById(R.id.tourny_loc);
        host = (TextView) findViewById(R.id.tourny_host);
        format = (TextView) findViewById(R.id.tourny_format);
        people = (TextView) findViewById(R.id.tourny_people);
        entryFee = (TextView) findViewById(R.id.entry_fee);
        joinButton = (ImageButton) findViewById(R.id.join);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "onramp.ttf");
        name.setTypeface(typeface);
        location.setTypeface(typeface);
        host.setTypeface(typeface);
        format.setTypeface(typeface);
        people.setTypeface(typeface);
        entryFee.setTypeface(typeface);

        tournament = staticTournament;

        if (tournament == null) {
            String url = getIntent().getData().toString();
            String id = url.substring(url.indexOf("nt/") + 3, url.length());
            TournamentController.getInstance(this).getTournament(id, new Callback() {
                @Override
                public void onDataRetrieved(Object data) {
                    tournament = (Tournament) data;
                    initialize();
                }

                @Override
                public void onRetrievalFailed() {}
            });
        } else {
            initialize();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VENMO_RESULT) {
            if(resultCode == RESULT_OK) {
                String signedrequest = data.getStringExtra("signedrequest");
                if(signedrequest != null) {
                    VenmoLibrary.VenmoResponse response = (new VenmoLibrary()).validateVenmoPaymentResponse(signedrequest, "h3ZUjVbNXCd4QA6apQhnFRuNJWCLBCEq");
                    if(response.getSuccess().equals("1")) {
                        //Payment successful.  Use data from response object to display a success message
                        String note = response.getNote();
                        String amount = response.getAmount();
                        Toast.makeText(this, "Successfully paid $" + amount + " for " + note, Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(getBaseContext(), WaitingActivity.class);
                        i.putExtra("id", tournament.getId());
                        startActivity(i);
                        finish();
                    }
                } else {
                    String error_message = data.getStringExtra("error_message");
                    Toast.makeText(this, error_message, Toast.LENGTH_SHORT).show();
                }
            } else if(resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Transaction cancelled", Toast.LENGTH_SHORT).show();
            }
        }
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

    private void initialize() {
        name.setText(tournament.getName());
        location.setText("Loc: " + tournament.getLocation());
        host.setText("Host: " + tournament.getHost().getString("name"));
        format.setText("Format: " + tournament.getType());
        people.setText("Max People: " + tournament.getMaxPeople());
        entryFee.setText("Cost: $" + tournament.getCost());

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TournamentController.getInstance(TournamentInfoActivity.this).joinTournament(tournament.getId(), new Callback() {
                    @Override
                    public void onDataRetrieved(Object data) {
                        if (!tournament.getCost().equals("null") && Integer.parseInt(tournament.getCost()) > 0) {
                            try {
                                Intent venmoIntent = VenmoLibrary.openVenmoPayment("2003", "TournUp", tournament.venmoId, tournament.getCost(), "Tournament Entry Fee", "pay");
                                startActivityForResult(venmoIntent, VENMO_RESULT);
                            } catch (android.content.ActivityNotFoundException e) //Venmo native app not install on device, so let's instead open a mobile web version of Venmo in a WebView
                            {
                                Intent venmoIntent = new Intent(getBaseContext(), VenmoWebViewActivity.class);
                                String venmo_uri = VenmoLibrary.openVenmoPaymentInWebView("2003", "TournUp", tournament.venmoId, tournament.getCost(), "Tournament Entry Fee", "pay");
                                venmoIntent.putExtra("url", venmo_uri);
                                startActivityForResult(venmoIntent, VENMO_RESULT);
                            }
                        } else {
                            Intent i = new Intent(getBaseContext(), WaitingActivity.class);
                            i.putExtra("id", tournament.getId());
                            startActivity(i);
                            finish();
                        }
                    }

                    @Override
                    public void onRetrievalFailed() {

                    }
                });
            }
        });
    }

}
