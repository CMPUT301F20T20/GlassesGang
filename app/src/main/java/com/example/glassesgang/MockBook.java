package com.example.glassesgang;

import java.io.Serializable;

/**
 * just a mock class for Book to check
 * how to use Serializable and to test
 * BookProfileActivity.
 * Will remove in the future
 * - Cholete
 */
public class MockBook implements Serializable {

    public String getAuthor() {
        return "mockAuthor";
    }

    public String getISBN() {
        return "111111111";
    }

    public String getTitle() {
        return "mockTitle";
    }

    public String getStatus() {
        return "status";
    }
}
