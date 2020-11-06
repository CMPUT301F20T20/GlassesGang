package com.example.glassesgang.Requests;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.glassesgang.R;

import javax.annotation.Nullable;

public class RequestListAdapter extends ArrayAdapter<Request> {

    private ArrayList<Request> requests;
    private Context context;

    public RequestListAdapter(Context context, ArrayList<Request> requests) {
        super(context, 0, requests);
        this.requests = requests;
        this.context = context;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.fragment_request, parent, false);
        }

        Request request = requests.get(position);

        TextView requestEmail = view.findViewById(R.id.request_email);
        requestEmail.setText(request.getEmail());

        Button declineButton = view.findViewById(R.id.request_decline_button);
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete request
            }
        });
        Button acceptButton = view.findViewById(R.id.request_accept_button);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //accept request by
            }
        });

        return view;
    }

}