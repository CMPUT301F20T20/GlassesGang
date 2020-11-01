package com.example.glassesgang.Requests;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.glassesgang.R;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Request}.
 */
public class RequestRecyclerViewAdapter extends RecyclerView.Adapter<RequestRecyclerViewAdapter.ViewHolder> {

    private final List<Request> mValues;

    public RequestRecyclerViewAdapter(List<Request> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_request, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mEmailView.setText(mValues.get(position).email);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mEmailView;
        public final Button mDeclineButton;
        public final Button mAcceptButton;
        public Request mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mEmailView = (TextView) view.findViewById(R.id.request_email);
            mDeclineButton = (Button) view.findViewById(R.id.request_decline_button);
            mDeclineButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //decline request
                }
            });
            mAcceptButton = (Button) view.findViewById(R.id.request_accept_button);
            mAcceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //accept request
                }
            });
        }
    }
}