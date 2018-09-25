package com.sapicons.deepak.k2psapicons.Others;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.firebase.database.FirebaseDatabase;

public class TBD extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
