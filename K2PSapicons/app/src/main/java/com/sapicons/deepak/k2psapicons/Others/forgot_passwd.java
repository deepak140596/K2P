package com.sapicons.deepak.k2psapicons.Others;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.sapicons.deepak.k2psapicons.Activities.LoginActivity;
import com.sapicons.deepak.k2psapicons.R;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class forgot_passwd extends AppCompatActivity {

    private Toolbar mTOlBar;
    private TextInputLayout mTextInputLayout;
    private CircularProgressButton mButton;
    private boolean backPressToExit = false;
    private FirebaseAuth mAuth;
    private TextView mT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_passwd);

        mAuth = FirebaseAuth.getInstance();

        mTextInputLayout = (TextInputLayout) findViewById(R.id.editText3);
        mButton = (CircularProgressButton) findViewById(R.id.button3);
        mT = (TextView)findViewById(R.id.textVie2);

        mTOlBar = (Toolbar)findViewById(R.id.forgo_toolbar);
        setSupportActionBar(mTOlBar);
        getSupportActionBar().setTitle("K2P");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTOlBar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mTOlBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent n = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(n);
                finish();
            }
        });



        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mString = mTextInputLayout.getEditText().getText().toString();
                if (!TextUtils.isEmpty(mString)){
                    resetUserPassword(mString);
                }else{
                    Toast.makeText(forgot_passwd.this,"Please fill the email first", Toast
                            .LENGTH_SHORT).show();
                }
            }
        });

        mT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(forgot_passwd.this, LoginActivity.class);
                startActivity(k);
                finish();
            }
        });







    }


    private void resetUserPassword(String Email) {

        mAuth.sendPasswordResetEmail(Email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    


                    Toast.makeText(getApplicationContext(),"The reset password link has been " +
                            "sent to your email-id", Toast.LENGTH_LONG).show();

                    Intent k = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(k);
                    finish();


                }else{


                    Toast.makeText(getApplicationContext(),
                            "Email don't exist", Toast.LENGTH_SHORT).show();

                }

            }

        });


    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        forgot_passwd.super.onBackPressed();
                    }
                }).create().show();
    }


}


