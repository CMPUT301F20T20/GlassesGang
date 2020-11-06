package com.example.glassesgang;

import android.content.Context;

import java.util.ArrayList;

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
