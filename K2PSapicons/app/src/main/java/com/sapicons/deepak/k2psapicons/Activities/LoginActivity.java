package com.sapicons.deepak.k2psapicons.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sapicons.deepak.k2psapicons.Others.forgot_passwd;
import com.sapicons.deepak.k2psapicons.R;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class LoginActivity extends AppCompatActivity {

    private TextView mLogReg;
    private TextView mFroget;
    private Toolbar mToOlBAr;

    private TextInputLayout mLoginMail;
    private TextInputLayout mLogPass;
    CircularProgressButton mLogBtn;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();


        /*mToOlBAr = (Toolbar) findViewById(R.id.rlog_toolbar);
        setSupportActionBar(mToOlBAr);
        getSupportActionBar().setTitle("Login");
*/

        mFroget = (TextView) findViewById(R.id.forgo5);
        mLogReg = (TextView) findViewById(R.id.logReg);
        mLoginMail = (TextInputLayout) findViewById(R.id.log_email);
        mLogPass = (TextInputLayout) findViewById(R.id.log_pass);
        mLogBtn = (CircularProgressButton) findViewById(R.id.log_btn);

        mFroget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent j = new Intent(LoginActivity.this, forgot_passwd.class);
                startActivity(j);
                finish();

                Toast.makeText(getApplicationContext(), "Reset Password", Toast.LENGTH_SHORT).show();

            }
        });

        mLogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String Email = mLoginMail.getEditText().getText().toString();
                String Password = mLogPass.getEditText().getText().toString();

                if (!TextUtils.isEmpty(Email) && !TextUtils.isEmpty(Password)) {
                    mLogBtn.startAnimation();


                    LOginUser(Email, Password);
                } else {


                    Toast.makeText(getApplicationContext(), "Please fill the fields carefully",
                            Toast.LENGTH_SHORT).show();
                }


            }
        });


        mLogReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent r = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(r);
                finish();

                Toast.makeText(getApplicationContext(), "Please Register", Toast.LENGTH_SHORT).show();

            }
        });


    }


    private void LOginUser(String Email, String Password) {

        mAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    mLogBtn.stopAnimation();

                    checkEmailVerification();
                } else {

                    mLogBtn.revertAnimation();
                    Toast.makeText(getApplicationContext(), "Please register before logging in",
                            Toast
                                    .LENGTH_SHORT).show();
                }

            }
        });


    }

    private void checkEmailVerification() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Boolean checkMail = firebaseUser.isEmailVerified();

        if (checkMail) {

            mLogBtn.startAnimation();

            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(mainIntent);
            finish();

        } else {
            Toast.makeText(this, "Please verify the Email before login", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
        }

    }


}