package com.sapicons.deepak.k2psapicons.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sapicons.deepak.k2psapicons.Others.Ad;
import com.sapicons.deepak.k2psapicons.R;
import com.squareup.picasso.Picasso;


public class SpecificActivity extends AppCompatActivity {
    private RecyclerView mPosts;
    private DatabaseReference mRootref, mUserRef;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific);

        mAuth  =FirebaseAuth.getInstance();
        String uID = mAuth.getCurrentUser().getUid();

        mRootref = FirebaseDatabase.getInstance().getReference().child("AdvertData");
        mUserRef = FirebaseDatabase.getInstance().getReference().child("UserAd").child(uID);


        Toolbar mToolbar = findViewById(R.id.specific_layout);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Your Posts");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPosts = findViewById(R.id.recy_specific);
        mPosts.setHasFixedSize(true);
        mPosts.setLayoutManager(new LinearLayoutManager(this));

        final LinearLayoutManager layoutManager = new LinearLayoutManager(SpecificActivity.this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mPosts.setLayoutManager(layoutManager);





        FirebaseRecyclerOptions<Ad> firebaseRecyclerOptions = new FirebaseRecyclerOptions
                .Builder<Ad>()
                .setQuery(mUserRef, Ad.class)
                .build();

        FirebaseRecyclerAdapter<Ad, UserViewHolder32> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Ad, UserViewHolder32>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull final UserViewHolder32 holder, int i, @NonNull Ad model) {
                holder.setId(model.getId());
                final String list_key = getRef(i).getKey();
                if (list_key != null) {
                    mRootref.child(list_key).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                String title = dataSnapshot.child("title").getValue().toString();
                                String price = dataSnapshot.child("price").getValue().toString();
                                String image = dataSnapshot.child("image").getValue().toString();
                                String category = dataSnapshot.child("category").getValue().toString();
                                String time = dataSnapshot.child("time").getValue().toString();

                                holder.setTitle(title);
                                holder.setPrice(price);
                                holder.setImage(image, getApplicationContext());
                                holder.setCategory(category);
                                holder.setTime(time);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }


            }

            @NonNull
            @Override
            public UserViewHolder32 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout
                                .user_ad_layout, parent,
                        false);

                return new UserViewHolder32(view1);
            }
        };
        firebaseRecyclerAdapter.startListening();
        mPosts.setAdapter(firebaseRecyclerAdapter);
    }

    private static class UserViewHolder32 extends RecyclerView.ViewHolder{

        View mView;

        public UserViewHolder32(View itemView) {
            super(itemView);

            mView = itemView;
            ImageView l = mView.findViewById(R.id.ad_start_fav);
            l.setVisibility(View.INVISIBLE);
        }

        public void setId(String id) {

        }

        public void setTitle(String title) {
            TextView userNameView= mView.findViewById(R.id.ad_title);
            userNameView.setText(title);
        }

        public void setPrice(String price) {
            TextView userNameView=  mView.findViewById(R.id.ad_price);
            userNameView.setText(price);
        }


        public void setImage(String image, Context applicationContext) {
            ImageView userImageView = mView.findViewById(R.id.ad_Img);
            Picasso.with(applicationContext).load(image).placeholder(R.mipmap.defaultimg).into(userImageView);

        }

        public void setCategory(String category) {
            TextView userNameView= mView.findViewById(R.id.ad_cat);
            userNameView.setText(category);
        }

        public void setTime(String time) {
            TextView userTime = mView.findViewById(R.id.date);
            userTime.setText(time);
        }
    }


}
