package com.sapicons.deepak.k2psapicons.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.sapicons.deepak.k2psapicons.Others.ConnectionDetect;
import com.sapicons.deepak.k2psapicons.R;

import java.util.HashMap;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class RegisterActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private TextInputLayout mDisNam, mEmailid, mPasswd, mPhone,mLoc;
    CircularProgressButton mRegBtn;
    private Toolbar mToolBAr;
    private TextView mCode, mState;
    private DatabaseReference mDatabase;
    ConnectionDetect cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        cd = new ConnectionDetect(this);

        mToolBAr = (Toolbar) findViewById(R.id.reg_toolbar);
        setSupportActionBar(mToolBAr);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolBAr.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mToolBAr.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent n = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(n);
                finish();
            }
        });


        Spinner mySpinner = (Spinner) findViewById(R.id.spinner1);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(RegisterActivity.this, android.R
                .layout.simple_expandable_list_item_1, getResources().getStringArray(R.array
                .Gende));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);

        mRegBtn = (CircularProgressButton) findViewById(R.id.reg_btn);
        mDisNam = (TextInputLayout) findViewById(R.id.reg_dis_name);
        mEmailid = (TextInputLayout) findViewById(R.id.reg_id);
        mPasswd = (TextInputLayout) findViewById(R.id.reg_pass);
        mPhone = (TextInputLayout) findViewById(R.id.reg_phone);
        mLoc = findViewById(R.id.reg_loc);


        mCode = (TextView) findViewById(R.id.reg_phon);
        mState=findViewById(R.id.reg_state);

        mState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = stateName();
            }
        });


        mRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String display_name = mDisNam.getEditText().getText().toString();
                String email = mEmailid.getEditText().getText().toString();
                String password = mPasswd.getEditText().getText().toString();
                String p = mCode.getText().toString();
                String ph = mPhone.getEditText().getText().toString();
                String phone = p + ph;
                String locality =  mLoc.getEditText().getText().toString();
                String state = mState.getText().toString();

                if (!display_name.equals("") && !email.equals("") && (!password.equals("")&&password
                        .length()>6) && !phone.equals("")&&!locality.equals("")) {
                    mRegBtn.startAnimation();

                    registerUser(display_name, email, password, phone, locality, state);
                } else {if(password.length()<6){
                   shortPassword();
                }



                    Toast.makeText(getApplicationContext(), "Please fill the fields carefully",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });



    }

    private String stateName() {
        String nm = "";
        new MaterialDialog.Builder(RegisterActivity.this)
                .title("Choose State:")
                .items(R.array.states)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        Toast.makeText(getApplicationContext(),text, Toast.LENGTH_LONG).show();
                        mState.setText(text);
                    }
                })
                .show();
        return nm;
    }

    private void shortPassword() {
        if (mPasswd.getEditText().getText().toString().length()<6)
            mPasswd.setError("You must have atleast 6 characters in the  password");
        return;
    }

    private void registerUser(final String display_name, final String email, final String password, final String phone, final String locality, final String state) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    if (cd.isConnected()){

                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                        String uid = currentUser.getUid();

                        String mtoken = FirebaseInstanceId.getInstance().getId();

                        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users")
                                .child(uid);
                        HashMap<String,String> userMap = new HashMap<>();
                        userMap.put("name", display_name);
                        userMap.put("email",email);
                        userMap.put("phone",phone);
                        userMap.put("token",mtoken);
                        userMap.put("image","defaultimg");
                        userMap.put("locality",locality);
                        userMap.put("area",locality+","+state);
                        userMap.put("state",state);
                        userMap.put("status","Hey!there.");
                        userMap.put("thumbnail","defaultimg");

                        mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    mRegBtn.startAnimation();
                                    SendEmailVerification();

                                    Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(i);
                                    finish();

                                }
                            }
                        });


                    }
                    else {if (!cd.isConnected())
                        Toast.makeText(getApplicationContext(),"You don't have Internet " +
                                "Connectivity", Toast.LENGTH_LONG).show();
                    }


                } else

                {

                    mRegBtn.revertAnimation();
                    Toast.makeText(getApplicationContext(), "You Registered UnSuccessfully",
                            Toast.LENGTH_SHORT).show();

                }


            }


        });

    }




    private void SendEmailVerification() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Check Your Email for " +
                                "Verification", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        RegisterActivity.super.onBackPressed();
                    }
                }).create().show();

    }


}
