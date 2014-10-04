package mipsmob.tournup.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import mipsmob.tournup.R;

public class RankingsAdapter extends BaseAdapter {

    private Context context;

    public RankingsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.rankings_row, parent, false);
        }

        TextView number = (TextView) convertView.findViewById(R.id.number);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView score = (TextView) convertView.findViewById(R.id.score);

        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "onramp.ttf");

        number.setTypeface(typeface);
        name.setTypeface(typeface);
        score.setTypeface(typeface);

        number.setText(i + "");

        return convertView;
    }

}
