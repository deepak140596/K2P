package com.sapicons.deepak.k2psapicons.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sapicons.deepak.k2psapicons.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ExpandActivity extends AppCompatActivity {

    private static final int REQUEST_CALL = 1;

    private ImageView mImage, mStar;
    private TextView mTitle, mCategory, mDescription, mPrice, mOw, mOl, mOd;
    private Toolbar mToolbar;
    private ProgressDialog mProgess;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    private ImageButton mCall, mChat, mDelete;
    String mPhone = "", mId = "", mName = "", m = "", name = "";


    private FirebaseUser mUser;
    private FirebaseStorage ImageStor;
    private DatabaseReference mUsersDatabase, mFavDatabase, mSpecific;
    private DatabaseReference mNumberDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expand);

        final String user_id = getIntent().getStringExtra("user_id");
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("AdvertData").child(user_id);
        mUsersDatabase.keepSynced(true);
        mNumberDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mFavDatabase = FirebaseDatabase.getInstance().getReference().child("favourites");
        final DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = mUser.getUid();


        mSpecific = FirebaseDatabase.getInstance().getReference().child("UserAd").child(uid);


       /* mToolbar = findViewById(R.id.expand_layout);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
*/
        mProgess = new ProgressDialog(ExpandActivity.this);

        mImage = findViewById(R.id.expand_img);
        mTitle = findViewById(R.id.expand_title);
        mOw = findViewById(R.id.expand_call_);
        mStar = findViewById(R.id.expand_star);
        mCategory = findViewById(R.id.expand_category);
        mDescription = findViewById(R.id.expand_description);
        mPrice = findViewById(R.id.expand_price);
        mCall = findViewById(R.id.expand_call);
        final TextView mPlace = findViewById(R.id.expand_place);
        mChat = findViewById(R.id.expand_chat);
        mDelete = findViewById(R.id.deleteBtn);
        mOd = findViewById(R.id.deleteText);
        mOl = findViewById(R.id.expand_chat_);
        mFavDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(uid).hasChild(user_id)) {
                    mStar.setImageDrawable(getDrawable(R.drawable.ic_star_black));
                } else {
                    mStar.setImageDrawable(getDrawable(R.drawable.ic_star_border_black_24dp));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        ImageStor = FirebaseStorage.getInstance();
        mCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                makePhoneCall();

            }
        });


        collapsingToolbarLayout = findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);


        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String Title = dataSnapshot.child("title").getValue().toString();
                    String Desription = dataSnapshot.child("description").getValue().toString();
                    String Category = dataSnapshot.child("category").getValue().toString();
                    String price = dataSnapshot.child("price").getValue().toString();
                    final String image = dataSnapshot.child("image").getValue().toString();
                    String id = null;
                    try {
                        id = dataSnapshot.child("id").getValue().toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new AlertDialog.Builder(ExpandActivity.this)
                                    .setTitle("Delete Post")
                                    .setMessage("Are you sure you want to delete?")
                                    .setNegativeButton(android.R.string.no, null)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface arg0, int arg1) {
                                            mProgess.setTitle("Deleting Your Post");
                                            mProgess.setMessage("Please wait while we delete your post");
                                            mProgess.show();

                                            StorageReference pref = ImageStor.getReferenceFromUrl(image);
                                            pref.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        try {
                                                            mFavDatabase.child(uid).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    mSpecific.child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {

                                                                            mUsersDatabase.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void aVoid) {
                                                                                    startActivity(new Intent(ExpandActivity.this, MainActivity.class));
                                                                                    Toast.makeText(getApplicationContext(), "Your ad was successfully deleted", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            });
                                                                        }
                                                                    });


                                                                }
                                                            });
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }
                                            });

                                        }
                                    }).create().show();
                        }
                    });

                    mTitle.setText(Title);
                    mDescription.setText(Desription);
                    mCategory.setText(Category);
                    mPrice.setText(price);
                    mId = id;
                    collapsingToolbarLayout.setTitle(Title);

                    if (uid.equals(mId)) {
                        Toast.makeText(ExpandActivity.this, "Your Own Post", Toast.LENGTH_LONG).show();
                        mCall.setVisibility(View.INVISIBLE);
                        mOw.setVisibility(View.INVISIBLE);
                        mChat.setVisibility(View.INVISIBLE);
                        mOl.setVisibility(View.INVISIBLE);
                        mDelete.setVisibility(View.VISIBLE);
                        mOd.setVisibility(View.VISIBLE);
                    } else {
                        mCall.setVisibility(View.VISIBLE);
                        mOw.setVisibility(View.VISIBLE);
                        mChat.setVisibility(View.VISIBLE);
                        mOl.setVisibility(View.VISIBLE);
                        mDelete.setVisibility(View.INVISIBLE);
                        mOd.setVisibility(View.INVISIBLE);
                    }

                    mNumberDatabase.child(mId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            mPhone = dataSnapshot.child("phone").getValue().toString();
                            mName = dataSnapshot.child("name").getValue().toString();
                            String area = dataSnapshot.child("area").getValue().toString();

                            mPlace.setText(area);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    mNumberDatabase.child(uid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            name = dataSnapshot.child("name").getValue().toString();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    mChat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent chatIntent = new Intent(ExpandActivity.this, ChatActivity.class);
                            chatIntent.putExtra("user_id", mId);
                            chatIntent.putExtra("user_name", mName);
                            chatIntent.putExtra("ad_id", user_id);
                            chatIntent.putExtra("cow", m);
                            startActivity(chatIntent);


                            mRootRef.child("Chat").child(uid).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    DatabaseReference user_message_push = mRootRef.child("Chat").child(uid).push();

                                    m = user_message_push.getKey();


                                    if (!dataSnapshot.hasChild(user_id)) {
                                        mRootRef.child("AdvertData").child(user_id).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    String image = dataSnapshot.child("image").getValue().toString();
                                                    String title = dataSnapshot.child("title").getValue().toString();
                                                    String price = dataSnapshot.child("price").getValue().toString();

                                                    Map chatAddMap = new HashMap();
                                                    chatAddMap.put("seen", false);
                                                    chatAddMap.put("timestamp", ServerValue.TIMESTAMP);
                                                    chatAddMap.put("image", image);
                                                    chatAddMap.put("title", title);
                                                    chatAddMap.put("price", price);
                                                    chatAddMap.put("chatUser", mName);
                                                    chatAddMap.put("ads", user_id);
                                                    chatAddMap.put("chatUserId", mId);


                                                    Map chatAddMap1 = new HashMap();
                                                    chatAddMap1.put("seen", false);
                                                    chatAddMap1.put("timestamp", ServerValue.TIMESTAMP);
                                                    chatAddMap1.put("image", image);
                                                    chatAddMap1.put("title", title);
                                                    chatAddMap1.put("price", price);
                                                    chatAddMap1.put("chatUser", name);
                                                    chatAddMap1.put("ads", user_id);
                                                    chatAddMap1.put("chatUserId", uid);


                                                    Map chatUserMap = new HashMap();
                                                    chatUserMap.put("Chats/" + uid + "/" + m, chatAddMap);
                                                    chatUserMap.put("Chats/" + mId + "/" + m, chatAddMap1);
                                                    mRootRef.updateChildren(chatUserMap, new DatabaseReference.CompletionListener() {
                                                        @Override
                                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                                            if (databaseError != null) {

                                                                Log.d("CHAT_LOG", databaseError.getMessage().toString());

                                                            }

                                                        }
                                                    });
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                        }
                    });


                    if (!image.equals("defaultimg"))
                        Picasso.with(ExpandActivity.this).load(image).placeholder(R.mipmap.defaultimg).into(mImage);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void makePhoneCall() {
        if (mPhone.length() > 0) {
            if (ContextCompat.checkSelfPermission(ExpandActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ExpandActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else {
                String dial = "tel:" + mPhone;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }

        } else {
            Toast.makeText(ExpandActivity.this, "No Phone Number provided", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
