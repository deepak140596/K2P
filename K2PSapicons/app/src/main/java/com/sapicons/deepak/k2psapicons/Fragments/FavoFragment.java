package com.sapicons.deepak.k2psapicons.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sapicons.deepak.k2psapicons.Activities.ExpandActivity;
import com.sapicons.deepak.k2psapicons.Others.fav;
import com.sapicons.deepak.k2psapicons.R;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavoFragment extends Fragment {

    private RecyclerView mFavUsers;
    private FirebaseRecyclerAdapter<fav, FavoFragment.HolderViewFav> firebaseRecyclerAdapter;
    private DatabaseReference mDatabase1;
    private DatabaseReference mFav;
    public String puid;
    private String title, price, image;

    @Override
    public void onStart() {
        super.onStart();

        firebaseRecyclerAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (firebaseRecyclerAdapter != null) {
            firebaseRecyclerAdapter.stopListening();
        }
    }


    public FavoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View favView = inflater.inflate(R.layout.fragment_favo, container, false);


        FirebaseAuth mCurrentUser = FirebaseAuth.getInstance();

        if (mCurrentUser != null) {
            puid = mCurrentUser.getUid();
        }

        mFav = FirebaseDatabase.getInstance().getReference().child("favourites").child(puid);
        mFav.keepSynced(true);
        mDatabase1 = FirebaseDatabase.getInstance().getReference().child("AdvertData");


        mFavUsers = (RecyclerView) favView.findViewById(R.id.fav_recycler);
        mFavUsers.setHasFixedSize(true);
        mFavUsers.setLayoutManager(new LinearLayoutManager(getActivity()));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mFavUsers.setLayoutManager(layoutManager);


        FirebaseRecyclerOptions<fav> firebaseRecyclerOptions = new FirebaseRecyclerOptions
                .Builder<fav>()
                .setQuery(mFav, fav.class)
                .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<fav, FavoFragment.HolderViewFav>
                (firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull final FavoFragment.HolderViewFav holder, int i, @NonNull
                    fav favo) {

                holder.setFav(favo.getFav_status());


                final String list_key = getRef(i).getKey();
                if (list_key != null) {
                    mDatabase1.child(list_key).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            title = dataSnapshot.child("title").getValue().toString();
                            price = dataSnapshot.child("price").getValue().toString();
                            image = dataSnapshot.child("image").getValue().toString();

                            holder.setTitle(title);
                            holder.setPrice(price);
                            holder.setImage(image, getContext());


                            holder.mView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent mew = new Intent(getActivity(), ExpandActivity.class);
                                    mew.putExtra("user_id", list_key);
                                    startActivity(mew);

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                holder.mFavR.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mFav.child(list_key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getContext(), "Removed from favourites", Toast.LENGTH_SHORT).show();
                                holder.mFavR.setImageDrawable(getContext().getDrawable(R.drawable.ic_star_border_black_24dp));

                            }
                        });
                    }
                });


            }

            @Override
            public FavoFragment.HolderViewFav onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout
                                .user_fav_ad, parent,
                        false);

                return new HolderViewFav(view);
            }
        };
        mFavUsers.setAdapter(firebaseRecyclerAdapter);


        return favView;
    }

    public static class HolderViewFav extends RecyclerView.ViewHolder {

        View mView;
        ImageView mFavR;

        public HolderViewFav(View itemView) {
            super(itemView);


            mView = itemView;
            mFavR = mView.findViewById(R.id.fav_ad_start_fav);
        }

        public void setFav(String fav_status) {
            TextView userNameView = (TextView) mView.findViewById(R.id.fav_fav);
            userNameView.setText(fav_status);
        }

        public void setTitle(String title) {
            TextView userName = mView.findViewById(R.id.fav_title);
            userName.setText(title);
        }

        public void setPrice(String price) {
            TextView userPrice = mView.findViewById(R.id.fav_price);
            userPrice.setText(price);
        }

        public void setImage(String image, Context ctx) {

            ImageView userImageView = mView.findViewById(R.id.fav_Img);
            Picasso.with(ctx).load(image).placeholder(R.mipmap.defaultimg).into(userImageView);


        }
    }

}
