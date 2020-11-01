package com.example.glassesgang;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CustomBookList extends ArrayAdapter<Book> {
    private ArrayList<Book> bookList;
    private Context context;

    public CustomBookList(Context context, ArrayList<Book> bookList) {
        super(context, 0, bookList);
        this.bookList = bookList;
        this.context = context;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.book_listing, parent,false);
        }
        Book book = bookList.get(position);
        TextView title = view.findViewById(R.id.book_title);
        TextView description = view.findViewById(R.id.book_description);
        TextView owner = view.findViewById(R.id.book_owner);
//        TextView status = view.findViewById(R.id.book_status);


//        TextView cityName = view.findViewById(R.id.city_text);
//        TextView provinceName = view.findViewById(R.id.province_text);
//        cityName.setText(city.getCityName());
//        provinceName.setText(city.getProvinceName());

        title.setText(book.getTitle());
        description.setText("book description?");
        owner.setText(book.getOwner());
//        status.setText(book.getStatus());


        return view;
    }



}
