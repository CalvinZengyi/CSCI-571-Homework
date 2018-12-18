package com.zengyicalvin.homework9;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

public class Tab1Event extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab1event, container, false);
        JSONObject json = ((DetailActivity)getActivity()).getTab1JSON();
        TextView artist = (TextView)rootView.findViewById(R.id.event_artists);
        TextView venue = (TextView)rootView.findViewById(R.id.event_venue);
        TextView time = (TextView)rootView.findViewById(R.id.event_time);
        TextView category = (TextView)rootView.findViewById(R.id.event_category);
        TextView priceRange = (TextView)rootView.findViewById(R.id.event_pricerange);
        TextView ticketStatus = (TextView)rootView.findViewById(R.id.event_ticketstatus);
        TextView buyTicket = (TextView)rootView.findViewById(R.id.event_buyticket);
        TextView seatMap = (TextView)rootView.findViewById(R.id.event_seatmap);

        try {
            String artistStr = "";
            for (int i = 0; i < json.getJSONObject("_embedded").getJSONArray("attractions").length(); i++) {
                artistStr += json.getJSONObject("_embedded").getJSONArray("attractions").getJSONObject(i).getString("name") + " | ";
            }
            artist.setText(artistStr.substring(0, artistStr.length() - 3));
        } catch (JSONException err) {
            err.printStackTrace();
        }

        try {
            venue.setText(json.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            String timeStr = "";
            String dateString = json.getJSONObject("dates").getJSONObject("start").getString("localDate");
            String timeString = json.getJSONObject("dates").getJSONObject("start").getString("localTime");
            SimpleDateFormat month_date = new SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(dateString);
            timeStr += month_date.format(date) + " " + timeString;
            time.setText(timeStr);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String categoryStr = "";
        try {
            String segment = json.getJSONArray("classifications").getJSONObject(0).getJSONObject("segment").getString("name");
            if (!segment.toLowerCase().equals("undefined")) {
                categoryStr += segment + " | ";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            String genre = json.getJSONArray("classifications").getJSONObject(0).getJSONObject("genre").getString("name");
            if (!genre.toLowerCase().equals("undefined")) {
                categoryStr += genre + " | ";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            String subGenre = json.getJSONArray("classifications").getJSONObject(0).getJSONObject("subGenre").getString("name");
            if (!subGenre.toLowerCase().equals("undefined")) {
                categoryStr += subGenre + " | ";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            String type = json.getJSONArray("classifications").getJSONObject(0).getJSONObject("type").getString("name");
            if (!type.toLowerCase().equals("undefined")) {
                categoryStr += type + " | ";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            String subType = json.getJSONArray("classifications").getJSONObject(0).getJSONObject("subType").getString("name");
            if (!subType.toLowerCase().equals("undefined")) {
                categoryStr += subType + " | ";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        category.setText(categoryStr.substring(0, categoryStr.length() - 3));

        try {
            String min = json.getJSONArray("priceRanges").getJSONObject(0).getString("min");
            String max = json.getJSONArray("priceRanges").getJSONObject(0).getString("max");
            priceRange.setText("$" + min + " ~ $" + max);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            String statusString = json.getJSONObject("dates").getJSONObject("status").getString("code");
            ticketStatus.setText(statusString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            String buyTicketURL = json.getString("url");
            buyTicket.setClickable(true);
            buyTicket.setMovementMethod(LinkMovementMethod.getInstance());
            String text = "<a href='" + buyTicketURL + "'>Ticketmaster</a>";
            buyTicket.setText(Html.fromHtml(text));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            String seatMapURL = json.getJSONObject("seatmap").getString("staticUrl");
            seatMap.setClickable(true);
            seatMap.setMovementMethod(LinkMovementMethod.getInstance());
            String text = "<a href='" + seatMapURL + "'> View Seat Map Here </a>";
            seatMap.setText(Html.fromHtml(text));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rootView;
    }
}
