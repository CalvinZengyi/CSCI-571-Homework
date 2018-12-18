package com.zengyicalvin.homework9;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private OnFragmentInteractionListener mListener;
    private GoogleMap mMap;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_fragment, null, false);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        JSONObject locationObj = ((DetailActivity)getActivity()).getLatLng();
        double latitude = 34;
        double longitude = -118;

        try {
            latitude = Double.parseDouble(locationObj.getString("latitude"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            longitude = Double.parseDouble(locationObj.getString("longitude"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LatLng pos = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(pos).title("Marker in pos"));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        if (latitude == 34) {
            mMap.moveCamera(CameraUpdateFactory.zoomTo(5));
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
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
        void messageFromChildFragment(Uri uri);
    }
}