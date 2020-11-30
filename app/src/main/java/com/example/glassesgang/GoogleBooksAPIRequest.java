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

// Received ISBN from Barcode Scanner. Send to GoogleBooks to obtain book information.
class GoogleBooksAPIRequest extends AsyncTask<String, Object, JSONObject> {

    String APIkey;
    String ISBN;
    private EditText titleEditText;
    private EditText authorEditText;
    private EditText isbnEditText;
    HttpURLConnection connection;

    public GoogleBooksAPIRequest(EditText titleEditText, EditText authorEditText, EditText isbnEditText) {
        this.titleEditText = titleEditText;
        this.authorEditText = authorEditText;
        this.isbnEditText = isbnEditText;
    }


    @Override
    protected JSONObject doInBackground(String... isbns) {

        Log.w("HEY", "Starting");

        APIkey = "AIzaSyCMW6VuEqmkolbOPDLhOaYwBstzg-Udhp4";
        ISBN = isbns[0];

        // Stop if cancelled
        if(isCancelled()){
            Log.w(getClass().getName(), "Cancelled");
            return null;
        }

        //String apiUrlString = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbns[0];
        String apiUrlString = "https://www.googleapis.com/books/v1/volumes?";

        try{
            Log.w(getClass().getName(), "Attempting request");
            Uri uri = Uri.parse(apiUrlString).buildUpon()
                    .appendQueryParameter("q", ISBN)
                    .appendQueryParameter("key", APIkey)
                    .appendQueryParameter("maxResult", "5")
                    .build();

            connection = null;

            // Build Connection.
            try{
                Log.w(getClass().getName(), "Attempting to connect");
                URL requestURL = new URL(uri.toString());
                connection = (HttpURLConnection) requestURL.openConnection();
                connection.setRequestMethod("GET");
                connection.setReadTimeout(5000); // 5 seconds
                connection.setConnectTimeout(5000); // 5 seconds
                connection.connect();

            } catch (MalformedURLException e) {
                Log.w(getClass().getName(), "MalformedURLException");
                // Impossible: The only two URLs used in the app are taken from string resources.
                e.printStackTrace();
            } catch (ProtocolException e) {
                Log.w(getClass().getName(), "ProtocolException");
                // Impossible: "GET" is a perfectly valid request method.
                e.printStackTrace();
            } catch (IOException e) {
                Log.w(getClass().getName(), "IOException");
                e.printStackTrace();
            }

            int responseCode = connection.getResponseCode();
            if(responseCode != 200){
                Log.w(getClass().getName(), "GoogleBooksAPI request failed. Response Code: " + responseCode);
                //connection.disconnect();
                return null;
            }

            // Read data from response.
            StringBuilder builder = new StringBuilder();
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = responseReader.readLine();

            while (line != null){
                line = responseReader.readLine();
                builder.append(line + "\n");
            }
            if (builder.length() == 0) {
                return null;
            }

            String responseString = builder.toString();
            Log.d(getClass().getName(), "Response String: " + responseString);
            JSONObject responseJson = new JSONObject(responseString);
            // Close connection and return response code.
            //connection.disconnect();
            return responseJson;

        } catch (SocketTimeoutException e) {
            Log.w(getClass().getName(), "Connection timed out. Returning null");
            return null;
        } catch(IOException e){
            Log.d(getClass().getName(), "IOException when connecting to Google Books API.");
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            Log.d(getClass().getName(), "JSONException when connecting to Google Books API.");
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                Log.w(getClass().getName(), "DISCONNECTING");
                connection.disconnect();
            }
        }

    }

    @Override
    protected void onPostExecute(JSONObject responseJson) {
        super.onPostExecute(responseJson);
        try {
            // Get the JSONArray of book items.
            JSONArray itemsArray = responseJson.getJSONArray("items");

            // Initialize iterator and results fields.
            int i = 0;
            String title = null;
            String authors = null;

            // Look for results in the items array, exiting when both the title and author
            // are found or when all items have been checked.
            while (i < itemsArray.length() || (title == null && authors != null)) {
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
            if (title != null){
                titleEditText.setText(title);
                authorEditText.setText(authors);
                isbnEditText.setText(ISBN);
            } else {
                // If none are found, update the UI to show failed results.
                titleEditText.setText("ERROR");
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
        /*
        System.out.println("JSON");
        if(isCancelled()){
            // Request was cancelled due to no network connection.
            //showNetworkDialog();
            System.out.println("Cancelled");
        } else if(responseJson == null){
            //showSimpleDialog(getResources().getString(R.string.dialog_null_response));
            System.out.println("Null");
        }
        else{
            // All went well. Do something with your new JSONObject.
            System.out.println("OK");
        }
         */
    }
}

