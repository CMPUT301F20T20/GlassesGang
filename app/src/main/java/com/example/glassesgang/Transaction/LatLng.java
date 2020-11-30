package com.example.glassesgang.Transaction;

import android.os.Parcel;
import android.os.Parcelable;

public class LatLng implements Parcelable {
    private Double latitude;
    private Double longitude;

    public LatLng() {}

    public LatLng(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LatLng(com.google.android.gms.maps.model.LatLng location) {
        this.latitude = location.latitude;
        this.longitude = location.longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    //custom parcelling part because google's model cannot do it
    public LatLng(Parcel in){
        String[] data = new String[2];

        in.readStringArray(data);
        this.latitude = Double.parseDouble(data[0]);
        this.longitude = Double.parseDouble(data[1]);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public LatLng createFromParcel(Parcel in) {
            return new LatLng(in);
        }

        public LatLng[] newArray(int size) {
            return new LatLng[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                this.latitude.toString(), this.longitude.toString()
        });
    }
}
