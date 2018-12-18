package com.zengyicalvin.homework9;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class Tab3Venue extends Fragment {
    private OnFragmentInteractionListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab3venue, container, false);
        JSONObject json = ((DetailActivity)getActivity()).getTab3JSON();
        fillContent(rootView, json);
        return rootView;
    }

    public void fillContent(View rootView, JSONObject json) {
        TextView venue_name = (TextView) rootView.findViewById(R.id.venue_name);
        TextView venue_addr = (TextView) rootView.findViewById(R.id.venue_addr);
        TextView venue_city = (TextView) rootView.findViewById(R.id.venue_city);
        TextView venue_phone = (TextView) rootView.findViewById(R.id.venue_phone);
        TextView venue_hour = (TextView) rootView.findViewById(R.id.venue_hour);
        TextView venue_grules = (TextView) rootView.findViewById(R.id.venue_grules);
        TextView venue_crules = (TextView) rootView.findViewById(R.id.venue_crules);

        try {
            venue_name.setText(json.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            venue_addr.setText(json.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("address").getString("line1"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            venue_city.setText(json.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("city").getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            venue_phone.setText(json.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("boxOfficeInfo").getString("phoneNumberDetail"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            venue_hour.setText(json.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("boxOfficeInfo").getString("openHoursDetail"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            venue_grules.setText(json.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("generalInfo").getString("generalRule"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            venue_crules.setText(json.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("generalInfo").getString("childRule"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Fragment childFragment = new MapFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.child_fragment_container, childFragment).commit();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void messageFromParentFragment(Uri uri);
    }
}
