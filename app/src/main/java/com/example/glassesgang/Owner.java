package com.example.glassesgang;

import android.content.Context;

import java.util.ArrayList;

/**
 * TODO: Currently, database stores attributes fields not objects, will implement Object storage for submission 4
 */
public class Owner extends User {
    public ArrayList<String> catalogue;

    public Owner(Context context) {
        super(context);
        this.catalogue = new ArrayList<String>();
    }

    public Owner() {
        super();
        this.catalogue = new ArrayList<>();
    }

    public Owner(String email) {
        super();
        this.catalogue = new ArrayList<>();
    }

    public ArrayList<String> getCatalogue() {
        return catalogue;
    }

    public void setCatalogue(ArrayList<String> catalogue) {
        this.catalogue = catalogue;
    }
}

