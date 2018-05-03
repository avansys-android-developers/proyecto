package com.chaicopaillag.app.mageli.Activity;

import com.google.firebase.database.FirebaseDatabase;

public class FirebaseBD extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
