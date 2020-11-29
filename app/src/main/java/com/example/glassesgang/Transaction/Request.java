package com.example.glassesgang.Transaction;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

/**
 * This is a class that represents the Request object, allowing for comparison of requests by bookId
 */
public class Request implements Parcelable {
    private String bookId;
    private String borrowerEmail;
    private String ownerEmail;
    private String requestId;
    private LatLng location;

    /**
     * This is the constructor for a Request object
     *
     * @param bookId This is the name of the new Request object
     * @param borrowerEmail This is the email of the borrower making the request
     * @param borrowerEmail This is the email of the owner receiving the request
     */
    public Request(String bookId, String borrowerEmail, String ownerEmail) {
        this.bookId = bookId;
        this.borrowerEmail = borrowerEmail;
        this.ownerEmail = ownerEmail;
    }

    public Request() {}

    protected Request(Parcel in) {
        bookId = in.readString();
        borrowerEmail = in.readString();
        ownerEmail = in.readString();
        requestId = in.readString();
        location = in.readParcelable(LatLng.class.getClassLoader());
    }

    public static final Creator<Request> CREATOR = new Creator<Request>() {
        @Override
        public Request createFromParcel(Parcel in) {
            return new Request(in);
        }

        @Override
        public Request[] newArray(int size) {
            return new Request[size];
        }
    };

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public LatLng getLocation() {
        return location;
    }

    /**
     * This returns the String bookId, the bookId's name
     *
     * @return Returns the bookId name
     */
    public String getBookId() {
        return bookId;
    }

    /**
     * This returns the borrower email
     *
     * @return Returns the String borrowerEmail
     */
    public String getBorrowerEmail() {
        return this.borrowerEmail;
    }

    /**
     * This returns the owner email
     *
     * @return Returns the String ownerEmail
     */
    public String getOwnerEmail() {
        return this.ownerEmail;
    }

    /**
     * This returns the String requestId
     * @return
     */
    public String getRequestId() {
        return this.requestId;
    }

    public void setRequestId(String requestId) { this.requestId = requestId; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bookId);
        dest.writeString(borrowerEmail);
        dest.writeString(ownerEmail);
        dest.writeString(requestId);
        dest.writeParcelable(location, flags);
    }
}
