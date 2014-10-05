package mipsmob.tournup.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import mipsmob.tournup.R;
import mipsmob.tournup.models.ResultUser;

public class RankingsAdapter extends BaseAdapter {

    private Context context;
    private ResultUser[] results;

    public RankingsAdapter(Context context, ResultUser[] results) {
        this.context = context;
        this.results = results;
    }

    @Override
    public int getCount() {
        return results.length;
    }

    @Override
    public ResultUser getItem(int i) {
        return results[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        ResultUser user = getItem(i);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.rankings_row, parent, false);
        }

        TextView number = (TextView) convertView.findViewById(R.id.number);
        TextView name = (TextView) convertView.findViewById(R.id.ranking_name);
        TextView score = (TextView) convertView.findViewById(R.id.score);

        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "onramp.ttf");

        number.setTypeface(typeface);
        name.setTypeface(typeface);
        score.setTypeface(typeface);

        number.setText((i+1) + "");
        name.setText(user.id);
        System.out.println(user.score);
//        score.setText(user.score);

        return convertView;
    }

}
