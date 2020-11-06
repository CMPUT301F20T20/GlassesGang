package com.example.glassesgang;

import android.content.Context;

import java.util.ArrayList;

/**
 * TODO: Currently, database stores attributes fields not objects, will implement Object storage for submission 4
 */
public class Borrower extends User {
    public ArrayList<String> catalogue;

    public Borrower(Context context){
        super(context);
        this.catalogue = new ArrayList<String>();

    }

    public Borrower(){
        super();
        this.catalogue = new ArrayList<String>();
    }

    public Borrower(String email) {
        super(email);
        this.catalogue = new ArrayList<String>();
    }

    public ArrayList<String> getCatalogue() {
        return catalogue;
    }

    public void setCatalogue(ArrayList<String> catalogue) {
        this.catalogue = catalogue;
    }
}
