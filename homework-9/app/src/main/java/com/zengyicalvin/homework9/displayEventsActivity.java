package com.zengyicalvin.homework9;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class displayEventsActivity extends AppCompatActivity {
    ArrayList<EventListItemModel> eventsLst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_events);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setTitle("Search Results");

        String keyword = getIntent().getStringExtra("EXTRA_SESSION_KEYWORD");
        String category = getIntent().getStringExtra("EXTRA_SESSION_CATEGORY");
        String distance = getIntent().getStringExtra("EXTRA_SESSION_DISTANCE");
        String unit = getIntent().getStringExtra("EXTRA_SESSION_UNIT");
        String location = getIntent().getStringExtra("EXTRA_SESSION_LOCATION");
        String longitude = getIntent().getStringExtra("EXTRA_SESSION_LONGITUDE");
        String latitude = getIntent().getStringExtra("EXTRA_SESSION_LATITUDE");
        String address = getIntent().getStringExtra("EXTRA_SESSION_ADDRESS");



        String url = "http://calvinzengyiuscnodejs.us-west-1.elasticbeanstalk.com/searchEvents?keyword=" + keyword.toLowerCase().replaceAll(" ", "+") +
                "&category=" + category.toLowerCase() + "&distance=" + distance + "&unit=" + unit.toLowerCase();
        Log.i("requestURL", url);
        if (location.equals("C")) {
            url += "&location=los+angeles";
        } else {
            url += "&location=" + address.toLowerCase().replaceAll(" ", "+");
        }
        getEvents(url);
    }

    private void getEvents(String RequestURL) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Fetching data");
        dialog.show();
// Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, RequestURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        JSONObject json;
                        try {
                            json = new JSONObject(response);
                        } catch (JSONException e) {
                            setContentView(R.layout.no_record);
                            return;
                        }
                        JSONArray events = null;
                        try {
                            events = json.getJSONObject("_embedded").getJSONArray("events");
                        } catch (JSONException err) {
                            Log.i("parsingError", "no key found");
                        }

                        final List<EventListItemModel> lst = new ArrayList<>();
                        String name = "";
                        String type = "";
                        String venue = "";
                        String locale = "";
                        String id = "";
                        String artistLst = "";
                        String detailUrl = "";
                        Date date = null;
                        if (events == null) {
                            setContentView(R.layout.no_record);
                            return;
                        }
                        for (int i = 0; i < events.length(); i++) {
                            name = "";
                            type = "";
                            venue = "";
                            locale = "";
                            id = "";
                            artistLst = "";
                            try {
                                name = events.getJSONObject(i).getString("name");

                            } catch (JSONException err) {
                                Log.e("jsonparsingerror", "cant find name");
                            }

                            try {
                                type = events.getJSONObject(i).getJSONArray("classifications").getJSONObject(0).getJSONObject("segment").getString("name");
                            } catch (JSONException err) {
                                Log.e("jsonparsingerror", "cant find type");
                            }

                            try {
                                venue = events.getJSONObject(i).getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getString("name");
                            } catch (JSONException err) {
                                Log.e("jsonparsingerror", "cant find venue");
                            }
                            String localDate = "";
                            String localTime = "";
                            try {
                                localDate = events.getJSONObject(i).getJSONObject("dates").getJSONObject("start").getString("localDate");
                                SimpleDateFormat month_date = new SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH);
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                date = sdf.parse(localDate);
                                localTime = events.getJSONObject(i).getJSONObject("dates").getJSONObject("start").getString("localTime");
                                locale = month_date.format(date) + " " + localTime;
                            } catch (JSONException err) {
                                SimpleDateFormat month_date = new SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH);
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                locale = month_date.format(date);
                                Log.e("jsonparsingerror", "cant find locale");

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            try {
                                id = events.getJSONObject(i).getString("id");
                            } catch (JSONException err) {
                                Log.e("jsonparsingerror", "cant find id");
                            }

                            try {
                                for (int j = 0; j < events.getJSONObject(i).getJSONObject("_embedded").getJSONArray("attractions").length(); j++) {
                                    artistLst += "&artistsName[]=" + events.getJSONObject(i).getJSONObject("_embedded").getJSONArray("attractions").getJSONObject(j).getString("name").replace(" ", "+");
                                }
                            } catch (JSONException err) {
                                Log.e("jsonparsingerror", "cant find artist");
                            }
                            detailUrl = "http://calvinzengyiuscnodejs.us-west-1.elasticbeanstalk.com/eventDetails/?id=" + id + "&category=" + type + artistLst + "&venue=" + venue.replace(' ', '+');
                            Log.i("detailUrl", detailUrl);
                            lst.add(new EventListItemModel(name, type, venue, locale, id, detailUrl, date));
                        }
                        eventsLst = (ArrayList<EventListItemModel>)lst;
                        final EventItemAdapter adapter = new EventItemAdapter((ArrayList<EventListItemModel>) lst, getApplicationContext());
                        final ListView listView=(ListView)findViewById(R.id.listView1);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                                EventListItemModel listItem = (EventListItemModel)listView.getItemAtPosition(position);
                                String url = listItem.getDetailUrl();
                                String eventName = listItem.getName();
                                displayDetail(url, view.getContext(), eventName);
                            }
                        });
                        listView.setAdapter(adapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void displayDetail(String requestURL, final Context context, final String evenName) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Fetching data");
        dialog.show();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = requestURL;
// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        Intent myIntent = new Intent(context, DetailActivity.class);
                        Log.i("JSON_DATA", response);

                        myIntent.putExtra("JSON_DATA", response);
                        myIntent.putExtra("EXTRA_EVENTNAME", evenName);
                        startActivityForResult(myIntent, 0);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("DisplayDetailError", error.toString());
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {  // After a pause OR at startup
        super.onResume();
        if (eventsLst == null)
            return;
        final EventItemAdapter adapter = new EventItemAdapter(eventsLst , getApplicationContext());
        final ListView listView=(ListView)findViewById(R.id.listView1);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                EventListItemModel listItem = (EventListItemModel)listView.getItemAtPosition(position);
                String url = listItem.getDetailUrl();
                String eventName = listItem.getName();
                displayDetail(url, view.getContext(), eventName);
            }
        });
        listView.setAdapter(adapter);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
