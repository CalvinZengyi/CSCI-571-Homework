package com.zengyicalvin.homework9;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class FavItemAdapter extends ArrayAdapter<EventListItemModel> {
    private ArrayList<EventListItemModel> dataSet;
    Context context;

    // View lookup cache

    public FavItemAdapter(ArrayList<EventListItemModel> data, Context context) {
        super(context, R.layout.events_list_item, data);
        this.dataSet = data;
        this.context=context;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.events_list_item, parent, false);
        TextView fl = (TextView) rowView.findViewById(R.id.firstLine);
        TextView sl = (TextView) rowView.findViewById(R.id.secondLine);
        TextView tl = (TextView) rowView.findViewById(R.id.thirdLine);
        ImageView icon = (ImageView) rowView.findViewById(R.id.icon);
        final ImageView favIcon = (ImageView) rowView.findViewById(R.id.favIcon);
        favIcon.setImageResource(R.drawable.heart_fill_red);
        favIcon.setTag("heart_fill_red");
        fl.setText(dataSet.get(position).getName());
        sl.setText(dataSet.get(position).getVenue());
        tl.setText(dataSet.get(position).getLocale());
        String type = dataSet.get(position).getType();
        if (type.toLowerCase().equals("sports")) {
            icon.setImageResource(R.drawable.sport_icon);
        } else if (type.toLowerCase().equals("music")) {
            icon.setImageResource(R.drawable.music_icon);
        } else if (type.toLowerCase().equals("arts")) {
            icon.setImageResource(R.drawable.art_icon);
        } else if (type.toLowerCase().equals("miscellaneous")) {
            icon.setImageResource(R.drawable.miscellaneous_icon);
        } else {
            icon.setImageResource(R.drawable.film_icon);
        }

        favIcon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (String.valueOf(favIcon.getTag()).equals("heart_outline_black")) {
                    favIcon.setImageResource(R.drawable.heart_fill_red);
                    favIcon.setTag("heart_fill_red");
                    Log.i("favICONTest", String.valueOf(favIcon.getTag()));
                    MyFavorite yourApplication = (MyFavorite) context.getApplicationContext();
                    yourApplication.myfave.put(dataSet.get(position).getId(), dataSet.get(position));
                }
                else if (String.valueOf(favIcon.getTag()).equals("heart_fill_red")){
                    favIcon.setImageResource(R.drawable.heart_outline_black);
                    favIcon.setTag("heart_outline_black");
                    Log.i("favICONTest", String.valueOf(favIcon.getTag()));
                    MyFavorite yourApplication = (MyFavorite) context.getApplicationContext();
                    yourApplication.myfave.remove(dataSet.get(position).getId());
                    Toast.makeText(context, dataSet.get(position).getName() + "was removed from favorites",
                            Toast.LENGTH_LONG).show();
                    dataSet.remove(position);
                    notifyDataSetChanged();
                }
            }
        });
        return rowView;
    }
}
