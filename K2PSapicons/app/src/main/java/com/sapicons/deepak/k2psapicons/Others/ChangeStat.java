package com.sapicons.deepak.k2psapicons.Others;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sapicons.deepak.k2psapicons.Activities.SettingActivity;
import com.sapicons.deepak.k2psapicons.R;

public class ChangeStat extends AppCompatActivity {

    private Toolbar mToOlBAr;
    private TextInputLayout mStatus;
    private Button mSave;

    private DatabaseReference mStatusDatabse;
    private FirebaseUser mCurrent;
    private ProgressDialog mProgess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_stat);

        mCurrent = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrent.getUid();
        mStatusDatabse = FirebaseDatabase.getInstance().getReference().child("Users").child
                (current_uid);

        mToOlBAr =(Toolbar)findViewById(R.id.statlay);
        setSupportActionBar(mToOlBAr);
        getSupportActionBar().setTitle("K2P");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mStatus = (TextInputLayout)findViewById(R.id.status_input);
        mSave = (Button)findViewById(R.id.change_stat);

        String status_value = getIntent().getStringExtra("status_value");
        mStatus.getEditText().setText(status_value);

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgess = new ProgressDialog(ChangeStat.this);
                mProgess.setTitle("Status Update");
                mProgess.setMessage("Please wait while we update the status");
                mProgess.show();

               String statu1s = mStatus.getEditText().getText().toString();

                mStatusDatabse.child("status").setValue(statu1s).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            mProgess.dismiss();
                            Intent backIntet = new Intent(ChangeStat.this,SettingActivity.class);
                            startActivity(backIntet);
                            finish();

                        }else{
                            Toast.makeText(getApplicationContext(),"Some Error", Toast
                                    .LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });



    }
}
