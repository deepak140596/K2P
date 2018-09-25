package com.sapicons.deepak.k2psapicons.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sapicons.deepak.k2psapicons.Others.ChangeStat;
import com.sapicons.deepak.k2psapicons.R;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity {
    private DatabaseReference mUserRef;
    private FirebaseUser mCurrentUser;

    public static final int GALLERY_PICK = 1;

    //Ztorage
    private StorageReference mImageStorage;

    private CircleImageView mCircle;
    private TextView mName;
    private TextView mStatus;
    private ImageButton mStatChan;
    private ImageButton mImage, mChange;
    private ProgressDialog mProgess;
    private TextView mMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mCircle =  findViewById(R.id.use_set_img);
        mName = findViewById(R.id.usr_set_dis_name);
        mMail = findViewById(R.id.set_email);
        mStatus =  findViewById(R.id.set_stat);
        mStatChan =  findViewById(R.id.use_set_sta);
        mImage = findViewById(R.id.usr_set_chgImg);
        mChange = findViewById(R.id.setting_change_info);

        mChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingActivity.this,EditActivity.class));
            }
        });

        mImageStorage = FirebaseStorage.getInstance().getReference();


        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uuid = mCurrentUser.getUid();


        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uuid);

        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                // String thumbimage = dataSnapshot.child("thumbimage").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();

                mName.setText(name);
                mMail.setText(email);
                mStatus.setText(status);

                if (!image.equals("defaultimg")) {
                    Picasso.with(SettingActivity.this).load(image).placeholder(R.mipmap.defaultimg).into
                            (mCircle);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mStatChan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String status_value = mStatus.getText().toString();

                Intent stat = new Intent(SettingActivity.this, ChangeStat.class);
                stat.putExtra("status_value", status_value);
                startActivity(stat);
                finish();
            }
        });

        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);
                // start picker to get image for cropping and then use the image in cropping activity
               /* CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(SettingActivity.this);*/
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GALLERY_PICK&&resultCode==RESULT_OK){
            Uri imageuri = data.getData();
            CropImage.activity(imageuri).setAspectRatio(1,1).start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode==RESULT_OK){

                mProgess = new ProgressDialog(SettingActivity.this);
                mProgess.setTitle("Uploading Image....");
                mProgess.setMessage("Please wait while we update the image");
                mProgess.setCanceledOnTouchOutside(false);
                mProgess.show();
                Uri resultUri=result.getUri();

                final String current_user_id = mCurrentUser.getUid();

                StorageReference filePath= mImageStorage.child("profile_images").child
                        (current_user_id+".jpg");
                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()){
                            mProgess.dismiss();
                            Toast.makeText(SettingActivity.this,
                                    "Profile picture updated successfully, plese wait while we " +
                                            "display it", Toast
                                            .LENGTH_SHORT)
                                    .show();


                            mImageStorage.child("profile_images").child(current_user_id+".jpg")
                                    .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                            {

                                @Override
                                public void onSuccess(Uri uri) {

                                    String downloadUrl = uri.toString();

                                    mUserRef.child("image").setValue(downloadUrl)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        mProgess.dismiss();
                                                        Toast.makeText(SettingActivity.this,
                                                                "Ad picture uploaded completely.",
                                                                Toast
                                                                        .LENGTH_SHORT)
                                                                .show();
                                                    }
                                                }
                                            });


                                }
                            });

                        }else{
                            Toast.makeText(SettingActivity.this,"Unsuccessful", Toast
                                    .LENGTH_SHORT)
                                    .show();
                        }
                    }
                });

            }else if (resultCode==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
            }


        }
    }
    public static String random(){
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(10);
        char tempChar;
        for (int i=0;i<randomLength;i++){
            tempChar=(char)(generator.nextInt(96)+32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }
}
