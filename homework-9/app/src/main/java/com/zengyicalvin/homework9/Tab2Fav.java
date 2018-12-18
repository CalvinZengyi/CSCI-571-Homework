package com.zengyicalvin.homework9;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Tab2Fav extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2fav, container, false);
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            ArrayList<EventListItemModel> lst = new ArrayList<>();
            MyFavorite yourApplication = (MyFavorite) getActivity().getApplicationContext();
            if (yourApplication.myfave.isEmpty()) {
                ((ListView)getActivity().findViewById(R.id.lstviewFav)).setVisibility(View.GONE);
                ((TextView)getActivity().findViewById(R.id.favNR)).setVisibility(View.VISIBLE);
                return;
            } else {
                ((ListView)getActivity().findViewById(R.id.lstviewFav)).setVisibility(View.VISIBLE);
                ((TextView)getActivity().findViewById(R.id.favNR)).setVisibility(View.GONE);
            }
            Set<String> keys =  yourApplication.myfave.keySet();
            for (String key : keys) {
                lst.add(yourApplication.myfave.get(key));
            }
            final FavItemAdapter adapter = new FavItemAdapter(lst, getContext());
            final ListView ls = (ListView) getActivity().findViewById(R.id.lstviewFav);
            ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                    EventListItemModel listItem = (EventListItemModel)ls.getItemAtPosition(position);
                    String url = listItem.getDetailUrl();
                    String eventName = listItem.getName();
                    displayDetail(url, view.getContext(), eventName);
                }
            });
            ls.setAdapter(adapter);
        }
    }
    public void displayDetail(String requestURL, final Context context, final String evenName) {
        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage("Fetching data");
        dialog.show();
        RequestQueue queue = Volley.newRequestQueue(getContext());
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
}
