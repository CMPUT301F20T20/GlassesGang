package com.example.glassesgang.Transaction;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.glassesgang.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MapFragment extends Fragment {

    public static final int REQUEST_LOCATION_CODE = 99;
    private static final int zoomAmount = 15;
    private com.google.android.gms.maps.model.LatLng givenLocation;
    private String userType;

    public MapFragment(String givenUserType) {
        userType = givenUserType;
    }

    public MapFragment(String givenUserType, LatLng latLng) {
        givenLocation = new com.google.android.gms.maps.model.LatLng(latLng.getLatitude(), latLng.getLongitude());
        userType = givenUserType; //must be borrower to receive location
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
                if (userType.equals("o")) {
                    if (givenLocation != null) goToLocation(googleMap, givenLocation);
                    else goToLocation(googleMap, null);
                    //after map is loaded
                    googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(com.google.android.gms.maps.model.LatLng latLng) {
                            //Creating Marker and setting its position
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(latLng);

                            //Give marker a title
                            markerOptions.title(latLng.latitude + " : " + latLng.longitude); //debug, replace with more meaningful title later

                            //Clear previously clicked position
                            googleMap.clear();

                            //Zoom to marker location
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomAmount));

                            //Add marker to the map
                            googleMap.addMarker(markerOptions);

                            //send to transaction fragment to be added into request
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("location", new LatLng(latLng));
                            getParentFragmentManager().setFragmentResult("location", bundle);
                        }
                    });
                } else if (userType.equals("b") && givenLocation != null)
                    goToLocation(googleMap, givenLocation);
            }
        });

        return view;
    }

    private void goToLocation(GoogleMap map, @Nullable com.google.android.gms.maps.model.LatLng givenLocation) {
        if (givenLocation == null) {
            //user is owner, zoom to current location if has location permissions on
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();

            if (ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[] { Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION },
                        REQUEST_LOCATION_CODE);
            }
            else {
                Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
                if (location != null) {
                    com.google.android.gms.maps.model.LatLng mapLocation = new com.google.android.gms.maps.model.LatLng(location.getLatitude(), location.getLongitude());
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(mapLocation, zoomAmount));
                    MarkerOptions marker = new MarkerOptions();
                    marker.title(mapLocation.latitude + " : " + mapLocation.longitude);
                    map.addMarker(marker);
                }
            }
        }
        else {
            //user is borrower, givenLocation exists
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(givenLocation, zoomAmount));
        }
    }

}