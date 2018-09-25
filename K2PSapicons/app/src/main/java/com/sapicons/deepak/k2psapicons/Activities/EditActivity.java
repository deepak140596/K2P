package com.sapicons.deepak.k2psapicons.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sapicons.deepak.k2psapicons.R;

import java.util.HashMap;
import java.util.Map;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class EditActivity extends AppCompatActivity {

    private EditText mName, mPhone, mLocality;
    CircularProgressButton mUpdateInfo;

    private DatabaseReference UserDatabase;
    private FirebaseAuth mAuth;


    private String mID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        mAuth = FirebaseAuth.getInstance();
        mID = mAuth.getCurrentUser().getUid();

        UserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mID);

        Toolbar mToolbar = findViewById(R.id.edit_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Edit Information");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mName = findViewById(R.id.edit_name);
        mPhone = findViewById(R.id.dove_love);
        mLocality = findViewById(R.id.edit_locality);
        mUpdateInfo = findViewById(R.id.button_edit);

        UserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name  = dataSnapshot.child("name").getValue().toString();
                String phone = dataSnapshot.child("phone").getValue().toString();
                String loc = dataSnapshot.child("area").getValue().toString();

                mName.setText(name);
                mPhone.setText(phone);
                mLocality.setText(loc);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateInfo();
                mUpdateInfo.startAnimation();
            }
        });
    }

    private void updateInfo() {
            String Name = mName.getText().toString();
            String Phone = mPhone.getText().toString();
            String Locli = mLocality.getText().toString();

            if (!TextUtils.isEmpty(Name)&&!TextUtils.isEmpty(Phone)&&!TextUtils.isEmpty(Locli)){
                Map editMap = new HashMap();
                editMap.put("name",Name);
                editMap.put("phone",Phone);
                editMap.put("area",Locli);

                UserDatabase.updateChildren(editMap).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(getApplicationContext(),"Successfully Updated data", Toast.LENGTH_SHORT).show();
                        mUpdateInfo.revertAnimation();
                                            }
                });

            }else {
                Toast.makeText(getApplicationContext(),"Please fill the fields carefully", Toast.LENGTH_SHORT).show();
                mUpdateInfo.revertAnimation();
            }
    }
}
