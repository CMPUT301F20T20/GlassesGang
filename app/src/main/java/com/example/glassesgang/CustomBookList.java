package com.example.glassesgang;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Book List Adapter for List View
 */
public class CustomBookList extends ArrayAdapter<Book> {
    private ArrayList<Book> bookList;
    private Context context;
    private String userType;  // "o" = owner ; "b" = borrower
    private String TAG = "Book properties";

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
        ImageView bookImage = view.findViewById(R.id.login_book_image_view);

        setBookImage(book, bookImage);

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

    private void setBookImage(Book book, ImageView bookImage) {
        String bookImageUrl = book.getImageUrl();
        if (bookImageUrl != null && bookImageUrl != "") {
            int SDK_INT = android.os.Build.VERSION.SDK_INT;

            if (SDK_INT > 8) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                URL url;
                try {
                    url = new URL(bookImageUrl);
                } catch (MalformedURLException e) {
                    Log.d(TAG, "URL not valid " + bookImageUrl);
                    return;
                }

                try {
                    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    bookImage.setImageBitmap(bmp);
                } catch (IOException e) {
                    Toast.makeText(
                            getContext(),
                            "There was a problem fetching the image for the book " + book.getTitle(),
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        }
    }

}
