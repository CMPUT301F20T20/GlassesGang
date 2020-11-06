package com.example.glassesgang.Requests;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.glassesgang.R;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 */
public class RequestHandlingFragment extends Fragment {

    private OnFragmentInteractionListener listener;
    private ListView requestListView;
    private ArrayAdapter<Request> requestAdapter;
    private ArrayList<Request> requestList;

    public interface OnFragmentInteractionListener {
        void onDeclineRequest();
        void onAcceptRequest();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_list, container, false);

        requestListView = view.findViewById(R.id.request_listview);

        //need to get requests from db here. using dummy for now
        requestList = new ArrayList<>();
        requestList.add(new Request("temp bookId", "temp email"));

        requestAdapter = new RequestListAdapter(this.getContext(), requestList);
        requestListView.setAdapter(requestAdapter);

        return view;
    }
}