package com.example.glassesgang;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.health.SystemHealthManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;

import static androidx.core.content.ContextCompat.getSystemService;

/**
 * Queries google books API and displays results on view
 */
public class GoogleBooksAPIRequest extends AsyncTask<String,Void,String>{

    // Variables for the search input field, and results TextViews
    private EditText titleEditText;
    private EditText authorEditText;
    private EditText isbnEditText;
    private String ISBN;

    // Class name for Log tag
    private static final String LOG_TAG = "GoogleBooks";

    // Constructor providing a reference to the views in MainActivity
    public GoogleBooksAPIRequest(EditText titleEditText, EditText authorEditText, EditText isbnEditText) {
        this.titleEditText = titleEditText;
        this.authorEditText = authorEditText;
        this.isbnEditText = isbnEditText;
    }


    /**
     * Makes the Books API call off of the UI thread.
     *
     * @param params String array containing the search data.
     * @return Returns the JSON string from the Books API or
     *         null if the connection failed.
     */
    @Override
    protected String doInBackground(String... params) {

        // Get the search string
        ISBN = params[0];


        // Set up variables for the try block that need to be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String bookJSONString = null;

        // Attempt to query the Books API.
        try {
            Log.w(LOG_TAG, "Attempting to query");

            // Base URI for the Books API.
            final String BOOK_BASE_URL =  "https://www.googleapis.com/books/v1/volumes?";

            Uri builtURI = Uri.parse(BOOK_BASE_URL).buildUpon()
                    .appendQueryParameter("q", ISBN)
                    .appendQueryParameter("maxResult", "10")
                    .build();

            URL requestURL = new URL(builtURI.toString());

            // Open the network connection.
            Log.w(LOG_TAG, "Attempting to connect");
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Get the InputStream.
            InputStream inputStream = urlConnection.getInputStream();

            // Read the response string into a StringBuilder.
            StringBuilder builder = new StringBuilder();

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line + "\n");
            }

            if (builder.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            bookJSONString = builder.toString();

            // Catch errors.
        } catch (IOException e) {
            Log.w(LOG_TAG, "IOException");
            e.printStackTrace();

            // Close the connections.
        } finally {
            Log.w(LOG_TAG, "Disconnecting");
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

        // Return the raw response.
        Log.w(LOG_TAG, "Returning JSON: " + bookJSONString);
        return bookJSONString;
    }

    /**
     * Handles the results on the UI thread. Gets the information from
     * the JSON and updates the Views.
     *
     * @param s Result from the doInBackground method containing the raw JSON response,
     *          or null if it failed.
     */
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            Log.w(LOG_TAG, "Converting response");
            // Convert the response into a JSON object.
            JSONObject jsonObject = new JSONObject(s);
            // Get the JSONArray of book items.
            JSONArray itemsArray = jsonObject.getJSONArray("items");

            // Initialize iterator and results fields.
            int i = 0;
            String title = null;
            String authors = null;

            // Look for results in the items array, exiting when both the title and author
            // are found or when all items have been checked.
            while (i < itemsArray.length() || (authors == null && title == null)) {
                // Get the current item information.
                JSONObject book = itemsArray.getJSONObject(i);
                JSONObject volumeInfo = book.getJSONObject("volumeInfo");

                // Try to get the author and title from the current item,
                // catch if either field is empty and move on.
                try {
                    title = volumeInfo.getString("title");
                    authors = volumeInfo.getString("authors");
                } catch (Exception e){
                    e.printStackTrace();
                }

                // Move to the next item.
                i++;
            }

            // If both are found, display the result.
            if (title != null && authors != null){
                titleEditText.setText(title);
                authorEditText.setText(authors);
                isbnEditText.setText(ISBN);
            } else {
                // If none are found, update the UI to show failed results.
                titleEditText.setText("");
                authorEditText.setText("");
                isbnEditText.setText("");
            }

        } catch (Exception e){
            // If onPostExecute does not receive a proper JSON string,
            // update the UI to show failed results.
            titleEditText.setText("");
            authorEditText.setText("");
            isbnEditText.setText("");
            e.printStackTrace();
        }
    }
}