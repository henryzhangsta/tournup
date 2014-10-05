package mipsmob.tournup.receivers;

import android.content.Context;
import android.content.Intent;

import com.parse.ParsePushBroadcastReceiver;
import com.parse.ParseUser;

import org.json.JSONObject;

import mipsmob.tournup.activities.MatchActivity;
import mipsmob.tournup.activities.ResultsActivity;

public class PushReceiver extends ParsePushBroadcastReceiver {

    @Override
    public void onPushReceive(Context context, Intent intent) {
        try {
            JSONObject data = new JSONObject(intent.getExtras().getString("com.parse.Data"));

            if (data.has("match")) {
                String matchId = data.getString("match");
                String opponentId = data.getString("opponent");

                ParseUser.getCurrentUser().put("curr_match", matchId);
                ParseUser.getCurrentUser().put("curr_opponent", opponentId);
                ParseUser.getCurrentUser().saveInBackground();
            } else {
                ParseUser.getCurrentUser().remove("curr_tournament");
                ParseUser.getCurrentUser().remove("curr_match");
                ParseUser.getCurrentUser().saveInBackground();
            }

            super.onPushReceive(context, intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPushOpen(Context context, Intent intent) {
        try {
            JSONObject data = new JSONObject(intent.getExtras().getString("com.parse.Data"));

            if (data.has("match")) {
                Intent i = new Intent(context, MatchActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            } else {
                Intent i = new Intent(context, ResultsActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("results", data.getString("tournament_id"));
                context.startActivity(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
