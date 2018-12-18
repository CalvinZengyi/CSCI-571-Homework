package com.zengyicalvin.homework9;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Tab4Upcoming extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab4upcoming, container, false);

        //****************Category spinner***************
        final Spinner spinner1 = (Spinner)rootView.findViewById(R.id.spinner3);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getActivity(), R.array.sorting_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner1.setAdapter(adapter1);

        //****************Category spinner***************
        final Spinner spinner2 = (Spinner)rootView.findViewById(R.id.spinner4);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(), R.array.sortmethod_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner2.setAdapter(adapter2);

        spinner2.setEnabled(false);



        List<EventListItemModel> lst = new ArrayList<>();
        lst = fillOutList(lst);
        if (lst == null) {
            return inflater.inflate(R.layout.no_record, container, false);
        }
        final UpcomingItemAdapter adapter = new UpcomingItemAdapter((ArrayList<EventListItemModel>) lst, getActivity().getApplicationContext());
        final ListView listView=(ListView)rootView.findViewById(R.id.upcoming_list);
        listView.setAdapter(adapter);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String sortBase = spinner1.getSelectedItem().toString().toLowerCase();
                String sortMet = spinner2.getSelectedItem().toString().toLowerCase();
                Log.i("Spinner2Testing", sortMet);
                if (sortMet.equals("ascending")) {
                    spinner2.setEnabled(true);
                    if (sortBase.equals("event name")) {
                        adapter.sort(new Comparator<EventListItemModel>() {
                            @Override
                            public int compare(EventListItemModel o1, EventListItemModel o2) {
                                return o1.getName().compareTo(o2.getName());
                            }
                        });
                    } else if (sortBase.equals("time")) {
                        adapter.sort(new Comparator<EventListItemModel>() {
                            @Override
                            public int compare(EventListItemModel o1, EventListItemModel o2) {
                                return o1.getDate().compareTo(o2.getDate());
                            }
                        });
                    } else if (sortBase.equals("artist")) {
                        adapter.sort(new Comparator<EventListItemModel>() {
                            @Override
                            public int compare(EventListItemModel o1, EventListItemModel o2) {
                                return o1.getVenue().compareTo(o2.getVenue());
                            }
                        });
                    } else if (sortBase.equals("type")) {
                        adapter.sort(new Comparator<EventListItemModel>() {
                            @Override
                            public int compare(EventListItemModel o1, EventListItemModel o2) {
                                return o1.getType().compareTo(o2.getType());
                            }
                        });
                    } else {
                        spinner2.setEnabled(false);
                    }
                } else {
                    spinner2.setEnabled(true);
                    if (sortBase.equals("event name")) {
                        adapter.sort(new Comparator<EventListItemModel>() {
                            @Override
                            public int compare(EventListItemModel o1, EventListItemModel o2) {
                                return -o1.getName().compareTo(o2.getName());
                            }
                        });
                    } else if (sortBase.equals("time")) {
                        adapter.sort(new Comparator<EventListItemModel>() {
                            @Override
                            public int compare(EventListItemModel o1, EventListItemModel o2) {
                                return -o1.getDate().compareTo(o2.getDate());
                            }
                        });
                    } else if (sortBase.equals("artist")) {
                        adapter.sort(new Comparator<EventListItemModel>() {
                            @Override
                            public int compare(EventListItemModel o1, EventListItemModel o2) {
                                return -o1.getVenue().compareTo(o2.getVenue());
                            }
                        });
                    } else if (sortBase.equals("type")) {
                        adapter.sort(new Comparator<EventListItemModel>() {
                            @Override
                            public int compare(EventListItemModel o1, EventListItemModel o2) {
                                return -o1.getType().compareTo(o2.getType());
                            }
                        });
                    } else {
                        spinner2.setEnabled(false);
                    }
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String sortBase = spinner1.getSelectedItem().toString().toLowerCase();
                String sortMet = spinner2.getSelectedItem().toString().toLowerCase();
                Log.i("Spinner2Testing", sortMet);
                if (sortMet.equals("ascending")) {
                    if (sortBase.equals("event name")) {
                        adapter.sort(new Comparator<EventListItemModel>() {
                            @Override
                            public int compare(EventListItemModel o1, EventListItemModel o2) {
                                return o1.getName().compareTo(o2.getName());
                            }
                        });
                    } else if (sortBase.equals("time")) {
                        adapter.sort(new Comparator<EventListItemModel>() {
                            @Override
                            public int compare(EventListItemModel o1, EventListItemModel o2) {
                                return o1.getDate().compareTo(o2.getDate());
                            }
                        });
                    } else if (sortBase.equals("artist")) {
                        adapter.sort(new Comparator<EventListItemModel>() {
                            @Override
                            public int compare(EventListItemModel o1, EventListItemModel o2) {
                                return o1.getVenue().compareTo(o2.getVenue());
                            }
                        });
                    } else if (sortBase.equals("type")) {
                        adapter.sort(new Comparator<EventListItemModel>() {
                            @Override
                            public int compare(EventListItemModel o1, EventListItemModel o2) {
                                return o1.getType().compareTo(o2.getType());
                            }
                        });
                    } else {
                        spinner2.setEnabled(false);
                    }
                } else {
                    if (sortBase.equals("event name")) {
                        adapter.sort(new Comparator<EventListItemModel>() {
                            @Override
                            public int compare(EventListItemModel o1, EventListItemModel o2) {
                                return -o1.getName().compareTo(o2.getName());
                            }
                        });
                    } else if (sortBase.equals("time")) {
                        adapter.sort(new Comparator<EventListItemModel>() {
                            @Override
                            public int compare(EventListItemModel o1, EventListItemModel o2) {
                                return -o1.getDate().compareTo(o2.getDate());
                            }
                        });
                    } else if (sortBase.equals("artist")) {
                        adapter.sort(new Comparator<EventListItemModel>() {
                            @Override
                            public int compare(EventListItemModel o1, EventListItemModel o2) {
                                return -o1.getVenue().compareTo(o2.getVenue());
                            }
                        });
                    } else if (sortBase.equals("type")) {
                        adapter.sort(new Comparator<EventListItemModel>() {
                            @Override
                            public int compare(EventListItemModel o1, EventListItemModel o2) {
                                return -o1.getType().compareTo(o2.getType());
                            }
                        });
                    } else {
                        spinner2.setEnabled(false);
                    }
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                EventListItemModel listItem = (EventListItemModel)listView.getItemAtPosition(position);
                String urlStr = listItem.getDetailUrl();
                Uri uri = Uri.parse(urlStr);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        return rootView;
    }

    public ArrayList<EventListItemModel> fillOutList(List<EventListItemModel> lst) {
        JSONObject json = ((DetailActivity)getActivity()).getTab4JSON();
        JSONArray events = null;

        try {
            events = json.getJSONObject("resultsPage").getJSONObject("results").getJSONArray("event");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        for (int i = 0; i < 5 && i < events.length(); i++) {
            String name = "";
            String type = "";
            String venue = "";
            String localeDate = "";
            String localeTime = "";
            String id = "";
            String detailUrl = "";
            Date date = null;

            try {
                name = events.getJSONObject(i).getString("displayName");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                type = events.getJSONObject(i).getString("type");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                venue = events.getJSONObject(i).getJSONArray("performance").getJSONObject(0).getString("displayName");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                localeDate = events.getJSONObject(i).getJSONObject("start").getString("date");
                SimpleDateFormat month_date = new SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                date = sdf.parse(localeDate);
                localeDate = month_date.format(date);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            try {
                localeTime = events.getJSONObject(i).getJSONObject("start").getString("time");
            } catch (JSONException e) {
                e.printStackTrace();
                localeTime = "";
            }



            try {
                id = events.getJSONObject(i).getString("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                detailUrl = events.getJSONObject(i).getString("uri");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            lst.add(new EventListItemModel(name, type, venue, localeDate + ", " + (localeTime == null? "": localeTime), id, detailUrl, date));
        }
        return (ArrayList<EventListItemModel>) lst;
    }
}
