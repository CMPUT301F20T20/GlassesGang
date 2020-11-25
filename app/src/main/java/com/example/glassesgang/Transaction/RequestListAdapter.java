package com.example.glassesgang.Transaction;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.ContentFrameLayout;

import com.example.glassesgang.R;
import javax.annotation.Nullable;

public class RequestListAdapter extends ArrayAdapter<Request> {

    private ArrayList<Request> requests;
    private Context context;
    private OnRequestInteractionListener listener;

    public RequestListAdapter(Context context, ArrayList<Request> requests, OnRequestInteractionListener listener) {
        super(context, 0, requests);
        this.requests = requests;
        this.context = context;
        this.listener = listener;
    }

    public interface OnRequestInteractionListener {
        void OnDeclineRequest(Request request);
        void OnAcceptRequest(Request request);
    }


    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.fragment_request, parent, false);
        }

        Request request = requests.get(position);

        TextView requestBorrowerEmail = view.findViewById(R.id.request_email);
        requestBorrowerEmail.setText(request.getBorrowerEmail());

        Button declineButton = view.findViewById(R.id.request_decline_button);
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // transition to the transaction fragment in parent
                //TODO: transaction fragment, sending switch command to parent through interface
                listener.OnDeclineRequest(request);
            }
        });
        Button acceptButton = view.findViewById(R.id.request_accept_button);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnAcceptRequest(request);
            }
        });

        return view;
    }

}