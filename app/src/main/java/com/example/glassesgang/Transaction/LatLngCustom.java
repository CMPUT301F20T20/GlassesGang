/*
Custom LatLng object with public constructor
Used because Google Maps API LatLng lacks a public constructor and cannot be stored in the database
 */
package com.example.glassesgang.Transaction;

public class LatLng {
    private double latitude;
    private double longitude;

    public LatLng(){}

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
