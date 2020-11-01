package com.example.glassesgang.Requests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RequestList {
    public static final List<Request> ITEMS = new ArrayList<Request>();


    private static void addRequest(Request item) {
        ITEMS.add(item);
    }

    private static void deleteRequest(int position) {
        ITEMS.remove(position);
    }
    
}