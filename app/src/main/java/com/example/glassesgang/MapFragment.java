package com.example.glassesgang;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_map, container, false);

        //initialize map fragment
        SupportMapFragment supportMapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);

        //async map
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                //after map is loaded
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        //Creating Marker and setting its position
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLng);

                        //Give marker a title
                        markerOptions.title(latLng.latitude + " : " + latLng.longitude); //debug, replace with more meaningful title later

                        //Clear previously clicked position
                        googleMap.clear();

                        //Zoom to marker location
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

                        //Add marker to the map
                        googleMap.addMarker(markerOptions);
                    }
                });
            }
        });

        return view;
    }

}