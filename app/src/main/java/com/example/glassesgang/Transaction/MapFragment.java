package com.example.glassesgang.Transaction;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import android.Manifest;
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

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private static final int zoomAmount = 13;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        //initialize map fragment
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        //async map
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                checkLocationPermission(googleMap);

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

    private boolean checkLocationPermission() {

        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }
        else {
            requestPermissions(Manifest.permission.ACCESS_FINE_LOCATION, 2);
        }
    }

    private void goToCurrentLocation(GoogleMap map) {
        //zoom to current location if has location permissions on
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null) {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), zoomAmount));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude())) //sets center of map to users location
                    .zoom(zoomAmount) //sets the zoom
                    .bearing(90) //sets the orientation of the camera
                    .tilt(40) //sets tilt of camera to 30 degrees
                    .build();
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }

    }

}