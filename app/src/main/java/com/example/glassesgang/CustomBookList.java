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

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.book_listing, parent, false);
        }

        Book book = bookList.get(position);
        TextView titleTexView = view.findViewById(R.id.book_title);
        TextView authorTexView = view.findViewById(R.id.book_author);
        TextView isbnTextView = view.findViewById(R.id.book_isbn);
        TextView borrowerTextView = view.findViewById(R.id.book_borrower);


        titleTexView.setText(book.getTitle());
        authorTexView.setText(book.getAuthor());
        isbnTextView.setText(book.getISBN());

        String borrower = book.getBorrower();
        if (borrower != "") {
            borrowerTextView.setText("Borrower: " + book.getBorrower());
        }
        else {
            borrowerTextView.setText("Borrower: None");
        }


        return view;
    }

}
