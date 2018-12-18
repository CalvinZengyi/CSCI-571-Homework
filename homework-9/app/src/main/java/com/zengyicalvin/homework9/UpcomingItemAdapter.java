package com.zengyicalvin.homework9;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class UpcomingItemAdapter extends ArrayAdapter<EventListItemModel> {
    private ArrayList<EventListItemModel> dataSet;
    Context context;

    // View lookup cache

    public UpcomingItemAdapter(ArrayList<EventListItemModel> data, Context context) {
        super(context, R.layout.upcoming_list_item, data);
        this.dataSet = data;
        this.context=context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.upcoming_list_item, parent, false);
        TextView name = (TextView) rowView.findViewById(R.id.upcoming_name);
        TextView artist = (TextView) rowView.findViewById(R.id.upcoming_artist);
        TextView locale = (TextView) rowView.findViewById(R.id.upcoming_locale);
        TextView type = (TextView) rowView.findViewById(R.id.upcoming_type);
        name.setText(dataSet.get(position).getName());
        artist.setText(dataSet.get(position).getVenue());
        locale.setText(dataSet.get(position).getLocale());
        type.setText(dataSet.get(position).getType());
        return rowView;
    }
}
