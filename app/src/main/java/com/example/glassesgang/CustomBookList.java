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
import android.widget.Filter;
import android.widget.Filterable;
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
public class CustomBookList extends ArrayAdapter<Book> implements Filterable {
    private ArrayList<Book> bookList;
    private ArrayList<Book> filteredBookList;
    private Context context;
    private String userType;  // "o" = owner ; "b" = borrower
    private ArrayList<String> statusFilterList;
    private BookStatusFilter bookStatusFilter;
    private String TAG = "Book properties";

    @Override
    public int getCount() {
        return filteredBookList.size();
    }

    @Override
    public Book getItem(int position) {
        return filteredBookList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public CustomBookList(Context context, ArrayList<Book> bookList, String userType) {
        super(context, 0, bookList);
        this.bookList = bookList;
        this.context = context;
        this.userType = userType;
        this.statusFilterList = new ArrayList<>();  // contains the statuses of books that must be displayed

        // when initialized books of all statuses can be displayed
        this.statusFilterList.add("AVAILABLE");
        this.statusFilterList.add("REQUESTED");
        this.statusFilterList.add("ACCEPTED");
        this.statusFilterList.add("BORROWED");

        this.filteredBookList = bookList;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.book_listing, parent, false);
        }

        Book book = filteredBookList.get(position);
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

    // if show is 1, it adds the status string passed to status filter
    // so books of that status will be displayed in the list view
    // if show is 0, it removers the status string passed from status filter
    // so books of that status will not be displayed in the list view
    public void updateFilter(String status, int show) {
        if (show == 1) {
            statusFilterList.add(status.toLowerCase());
        }
        else {
            statusFilterList.remove(status.toLowerCase());
        }
    }

    // must implement this method when implementing Filterable interface
    @Override
    public Filter getFilter() {
        if (bookStatusFilter == null) {
            bookStatusFilter = new BookStatusFilter();
        }
        return bookStatusFilter;
    }

    class BookStatusFilter extends Filter {

        // filter the books according to the statuses in the statusFilter
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            ArrayList<Book> filteredBooks = new ArrayList<>();
            for(Book book: bookList) {  // if book's status is in status filter, add it to the filtered book list
                if (statusFilterList.contains(book.getStatus().toLowerCase())) {
                    filteredBooks.add(book);
                }
            }
            results.values = filteredBooks;
            results.count = filteredBooks.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredBookList = (ArrayList<Book>) results.values;
            notifyDataSetChanged();
        }
    }
}

