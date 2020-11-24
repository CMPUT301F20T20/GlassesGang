package com.example.glassesgang.Transaction;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.glassesgang.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Objects;

public class MapFragment extends Fragment {

    private static final int zoomAmount = 13;
    private OnMapInteractionListener listener;
    private LatLng givenLocation;
    private String userType;

    public MapFragment() {
        userType = "o";
    }

    public MapFragment(LatLng latLng) {
        givenLocation = latLng;
        userType = "b"; //must be borrower to receive location
    }

    public interface OnMapInteractionListener {
        void onMarkerSelected(LatLng latLng);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MapFragment.OnMapInteractionListener) {
            listener = (MapFragment.OnMapInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement OnMapInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        //initialize map fragment
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        //async map
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                if (hasLocationPermissions() && userType.equals("o")) {
                    goToLocation(googleMap, null);
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

                            //send to transaction fragment to be added into request
                            listener.onMarkerSelected(latLng);
                        }
                    });
                }
                else if (userType.equals("b") && givenLocation != null) goToLocation(googleMap, givenLocation);
            }
        });

        return view;
    }

    private boolean hasLocationPermissions() {
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        else {
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
            return false;
        }
    }

    private void goToLocation(GoogleMap map, @Nullable LatLng givenLocation) {
        if (givenLocation == null) {
            //user is owner, zoom to current location if has location permissions on
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();

            @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
            if (location != null) {
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), zoomAmount));

                /* //optional camera settings for zooming to a location //TODO: remove camera settings once team has chosen camera options
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(location.getLatitude(), location.getLongitude())) //sets center of map to users location
                        .zoom(zoomAmount) //sets the zoom
                        .bearing(90) //sets the orientation of the camera
                        .tilt(40) //sets tilt of camera to 30 degrees
                        .build();
                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                */
            }
        }
        else {
            //user is borrower, givenLocation exists
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(givenLocation, zoomAmount));
        }
    }

}