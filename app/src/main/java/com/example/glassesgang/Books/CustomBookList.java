package com.example.glassesgang.Books;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.glassesgang.Books.Book;
import com.example.glassesgang.R;

import java.util.ArrayList;

public class CustomBookList extends ArrayAdapter<Book> {
    private ArrayList<Book> bookList;
    private Context context;
    private String userType;  // "o" = owner ; "b" = borrower

    public CustomBookList(Context context, ArrayList<Book> bookList, String userType) {
        super(context, 0, bookList);
        this.bookList = bookList;
        this.context = context;
        this.userType = userType;
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
        TextView borrowerOwnerTextView = view.findViewById(R.id.book_borrower_owner);
        TextView statusTextView = view.findViewById(R.id.book_temp_status);   // implementing status as a text view for now, might change to image view in the future


        titleTexView.setText(book.getTitle());
        authorTexView.setText(book.getAuthor());
        isbnTextView.setText(book.getISBN());
        statusTextView.setText(book.getStatus());

        // if user is an owner, display the borrower of a book
        // if user is a borrower, display the owner of a book
        if (userType.equals("o")) {
            String borrower = book.getBorrower();
            if (borrower == null || borrower == "") {
                borrowerOwnerTextView.setText("Borrower: None");
            }
            else {
                borrowerOwnerTextView.setText("Borrower: " + borrower);
            }
        } else {
            String owner = book.getOwner();
            if (owner != null) {
                borrowerOwnerTextView.setText("Owner: " + owner);
            }
        }
        

        return view;
    }

}
