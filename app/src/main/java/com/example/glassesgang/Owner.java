package com.example.glassesgang;

import android.content.Context;

import java.util.ArrayList;


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

