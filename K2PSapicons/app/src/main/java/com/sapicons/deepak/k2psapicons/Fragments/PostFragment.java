package com.sapicons.deepak.k2psapicons.Fragments;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sapicons.deepak.k2psapicons.R;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.DateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class PostFragment extends Fragment {

    private FirebaseUser mCurrentUser;

    private DatabaseReference mUserRef;

    private StorageReference mImageStorage;
    private Uri mImageUri= null;


    private DatabaseReference mDatabase, mDatabase2, mAdUser, mRootref;
    private FirebaseAuth mAuth;

    private ProgressDialog mProgess;
    public String push_id55;
    private TextView mCategory;
    private TextInputLayout mTitle, mDescription,mPrice;
    private TextView mFixed, mLoc, mPho;
    private ImageButton mButton1;
    private String uid, mDwar;




    public PostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
                    setRetainInstance(true);
        // Inflate the layout for this fragment
        View postView = inflater.inflate(R.layout.fragment_post, parent, false);

       mImageStorage = FirebaseStorage.getInstance().getReference();
       mDatabase = FirebaseDatabase.getInstance().getReference().child("AdvertData");

       mAuth = FirebaseAuth.getInstance();
        try {
             uid = mAuth.getCurrentUser().getUid();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mDatabase2 =FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        mRootref = FirebaseDatabase.getInstance().getReference();




        mCategory =     postView.findViewById(R.id.choose_category1);
        mPho =         postView.findViewById(R.id.pos_phone1);
        Button mPostAd=postView.findViewById(R.id.Post_ad1);
        Button mPreAd= postView.findViewById(R.id.pre_ad);
        mFixed =       postView.findViewById(R.id.post_fixed_frag1);
        mTitle =       postView.findViewById(R.id.post_frag_title1);
        mDescription = postView.findViewById(R.id.post_descri_frag1);
        mPrice =       postView.findViewById(R.id.post_fixed_amount);
        mLoc =         postView.findViewById(R.id.location_post_frag1);
        final TextView mLockkk = postView.findViewById(R.id.lockakakk);
        mButton1 =     postView.findViewById(R.id.add_btn);

        mDatabase2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String area51 = dataSnapshot.child("area").getValue().toString();
                mDwar = dataSnapshot.child("state").getValue().toString();
                mLockkk.setText(area51);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mProgess = new ProgressDialog(getContext());

        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setAspectRatio(1,1)
                        .start(getContext(), PostFragment.this);
            }
        });

        mPreAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                View mView = getLayoutInflater().inflate(R.layout.preview_ad,null);
                TextView mTit = (TextView)mView.findViewById(R.id.ad_title1);
                TextView mCAt = (TextView)mView.findViewById(R.id.ad_cat1);
                TextView mPric = (TextView)mView.findViewById(R.id.ad_price1);
                CircleImageView mCircle = (CircleImageView)mView.findViewById(R.id.ad_Img1);
                TextView mDes = (TextView)mView.findViewById(R.id.ad_des2);

                String titl = mTitle.getEditText().getText().toString();
                String cat = mCategory.getText().toString();

                String mrice = mPrice.getEditText().getText().toString();
                String fix = mFixed.getText().toString();
                String priceq =fix+"="+mrice;
                String desf = mDescription.getEditText().getText().toString();

                mTit.setText(titl);
                mCAt.setText(cat);
                mPric.setText(priceq);
                Picasso.with(getContext()).load(mImageUri).placeholder(R.mipmap.defaultimg).into
                        (mCircle);
                mDes.setText(desf);

                alertDialog.setView(mView);
                AlertDialog dialog = alertDialog.create();
                dialog.show();

            }
        });




        mImageStorage = FirebaseStorage.getInstance().getReference();

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uuid = mCurrentUser.getUid();

        mAdUser = FirebaseDatabase.getInstance().getReference().child("UserAd");

        mUserRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = mUserRef.child("Users").child(uuid);

        DatabaseReference adRef = mUserRef.child("AdvertData").child(uuid);





        adRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String email = dataSnapshot.child("email").getValue().toString();
                String Phone = dataSnapshot.child("phone").getValue().toString();

                mLoc.setText(email);
                mPho.setText(Phone);

                }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mPostAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               postAd();

            }
        });







        mCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = CategoryName();
            }
        });

        return postView;
        }

    private void shortDescription() {
        if (mDescription.getEditText().getText().toString().length()<100)
            mDescription.setError("You must have atleast 100 characters in the description");
        return;
    }

    private void postAd() {

        final String title = mTitle.getEditText().getText().toString();
        final String description = mDescription.getEditText().getText().toString();
        final String category = mCategory.getText().toString();
        final String mrice = mPrice.getEditText().getText().toString();
        String fix = mFixed.getText().toString();
        final String price = fix + "=" + mrice;


        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty
                (mrice)) {
            mProgess.setMessage("Uploading the data");
            mProgess.show();
            mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
            final String uuid = mCurrentUser.getUid();

            final String currentdate1 = DateFormat.getDateTimeInstance().format(new Date());

            StorageReference filepath = mImageStorage.child("ad_images").child(mImageUri.getLastPathSegment());
            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mImageStorage.child("ad_images").child(mImageUri.getLastPathSegment())
                            .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            final String downloadUrl = uri.toString();

                            DatabaseReference newPost = mDatabase.push();
                            push_id55 = newPost.getKey();

                            newPost.child("title").setValue(title);
                            newPost.child("description").setValue(description);
                            newPost.child("price").setValue(price);
                            newPost.child("category").setValue(category);
                            newPost.child("time").setValue(currentdate1);
                            newPost.child("id").setValue(uuid);
                            newPost.child("timestamp").setValue(ServerValue.TIMESTAMP);
                            newPost.child("image").setValue(downloadUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    DatabaseReference specificPoat = mRootref.child("States").child(mDwar).child(push_id55);
                                    specificPoat.child("title").setValue(title);
                                    specificPoat.child("ad").setValue(push_id55);
                                    specificPoat.child("image").setValue(downloadUrl);
                                    specificPoat.child("price").setValue(price);
                                    specificPoat.child("timestamp").setValue(ServerValue.TIMESTAMP).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            createPo();
                                        }
                                    });


                                }
                            });







                        }
                    });

                }
            });

        }else{
            Toast.makeText(getActivity(),"Please fill the fields carefully", Toast.LENGTH_SHORT).show();
        }

        }

    private void createPo() {
        final String currentdate4 = DateFormat.getDateTimeInstance().format(new Date());

        mAdUser.child(uid).child(push_id55).child("posted on").setValue(currentdate4).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getContext(),"Successfuly added your ad", Toast
                        .LENGTH_SHORT).show();

                mProgess.dismiss();
                mTitle.getEditText().setText(null);
                mDescription.getEditText().setText(null);
                mCategory.setText(R.string.category);
                mPrice.getEditText().setText(null);
                mButton1.setImageURI(null);
            }
        });
    }


    private String CategoryName() {
        String nm = "";
        new MaterialDialog.Builder(getActivity())
                .title("Choose Category:")
                .items(R.array.Category)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        Toast.makeText(getActivity(),text, Toast.LENGTH_LONG).show();
                        mCategory.setText(text);
                    }
                })
                .show();
        return nm;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                 mImageUri = result.getUri();
                mButton1.setImageURI(mImageUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

}
