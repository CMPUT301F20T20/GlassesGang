package com.example.glassesgang.Transaction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This is a class that keeps track of a list of request objects
 */
public class RequestList {
    private List<Request> requests = new ArrayList<>();

    /**
     * This adds a request to the list if the request does not exist
     * Throws an IllegalAccessException if request already exists in the RequestList
     *
     * @param request This is a candidate request to add
     */
    public void add(Request request) {
        if (requests.contains(request)) {
            throw new IllegalArgumentException("request already exists");
        }
        requests.add(request);
    }

    /**
     * This removes a request to the list if the request does exist
     * Throws an IllegalAccessException if request does not exists in the RequestList
     *
     * @param request This is a candidate request to add
     */
    public void delete(Request request) {
        if (requests.contains(request)) {
            requests.remove(request);
        }
        else throw new IllegalArgumentException("request does not exist in list");
    }

    /**
     * This checks whether a given request exists in the RequestList
     *
     * @param request This is a candidate request to add
     * @return  Return true if request exists in RequestList;
     *          Return false if request does not exist in RequestList
     */
    public boolean hasRequest(Request request) {
        if (requests.contains(request)) return true;
        return false;
    }

    /**
     * This returns the size of the RequestList, denoting how many requests it contains
     *
     * @return Return integer of the number of requests within RequestList
     */
    public int countRequests() {
        return requests.size();
    }

    /**
     * This returns a sorted list of requests
     *
     * @return Return the sorted list
     */
    public List<Request> getRequests() {
        List<Request> list = requests;
        Collections.sort(list);
        return list;
    }
}

