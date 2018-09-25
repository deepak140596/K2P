package com.sapicons.deepak.k2psapicons.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.sapicons.deepak.k2psapicons.Activities.ExpandActivity;
import com.sapicons.deepak.k2psapicons.Others.Ad;
import com.sapicons.deepak.k2psapicons.Others.AnimationUtil;
import com.sapicons.deepak.k2psapicons.Others.Local;
import com.sapicons.deepak.k2psapicons.Others.LocalAdapter;
import com.sapicons.deepak.k2psapicons.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    private RecyclerView mallUsers, mLocalAds;
    private FirebaseRecyclerAdapter<Ad, HomeFragment.UsersViewHolder1> firebaseRecyclerAdapter;
    private LocalAdapter mAdapter;
    private List<Local> mLocals;
    private ProgressBar mProgress;
    private DatabaseReference mDatabase1;
    private DatabaseReference mFav, mRootRef, s;
    int previousPosition = 0;
    public String puid;
    private SparseBooleanArray itemStateArray = new SparseBooleanArray();


    //search funcationality
    private FirebaseRecyclerAdapter<Ad, HomeFragment.UsersViewHolder1> searchAdapter;
    List<String> suggestList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;


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


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View homeView = inflater.inflate(R.layout.fragment_home, parent, false);


        mFav = FirebaseDatabase.getInstance().getReference().child("favourites");

        FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (mCurrentUser != null) {
            puid = mCurrentUser.getUid();
        }


        mDatabase1 = FirebaseDatabase.getInstance().getReference().child("AdvertData");

        mDatabase1.keepSynced(true);




        mallUsers = homeView.findViewById(R.id.recy1);
        mallUsers.setHasFixedSize(true);
        mallUsers.setLayoutManager(new LinearLayoutManager(getActivity()));


        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mallUsers.setLayoutManager(layoutManager);
        mallUsers.setNestedScrollingEnabled(false);

        mLocalAds = homeView.findViewById(R.id.recyHorizontal);
        mLocalAds.setHasFixedSize(true);
        mLocalAds.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));

        final LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true);
        layoutManager2.setStackFromEnd(true);

        mLocalAds.setLayoutManager(layoutManager2);
        mLocalAds.setNestedScrollingEnabled(false);


        mLocals = new ArrayList<>();


        materialSearchBar = homeView.findViewById(R.id.searchBar);
        materialSearchBar.setHint("Search Ad");
        loadSuggest();
        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                List<String> sugest = new ArrayList<String>();
                try {
                    for (String search : suggestList) {
                        if (search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                            sugest.add(search);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                materialSearchBar.setLastSuggestions(sugest);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if (!enabled)
                    mallUsers.setAdapter(firebaseRecyclerAdapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {

                startSearch(text);

            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });


        try {
            s = FirebaseDatabase.getInstance().getReference().child("Users").child(puid);
            s.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String state = dataSnapshot.child("state").getValue().toString();
                mRootRef = FirebaseDatabase.getInstance().getReference().child("States").child(state);

                mRootRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                            Local local = postSnapshot.getValue(Local.class);

                            mLocals.add(local);
                        }
                        mAdapter = new LocalAdapter(getContext(), mLocals);
                        mLocalAds.setAdapter(mAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getContext(),databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        } catch (Exception e) {
            Log.d("lol","lolwa");
        }








        FirebaseRecyclerOptions<Ad> firebaseRecyclerOptions = new FirebaseRecyclerOptions
                .Builder<Ad>()
                .setQuery(mDatabase1, Ad.class)
                .build();


        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Ad, UsersViewHolder1>
                (firebaseRecyclerOptions) {


            @Override
            protected void onBindViewHolder(@NonNull final UsersViewHolder1 Holder, final int position, @NonNull
                    Ad ad) {
                Holder.setTitle(ad.getTitle());
                Holder.setPrice(ad.getPrice());
                Holder.setCategory(ad.getCategory());
                Holder.setImage(ad.getImage(), getContext());
                Holder.setTime(ad.getTime());


                String user_id = getRef(position).getKey();
                final String kk = user_id.toString();

                mFav.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            if (dataSnapshot.child(puid).hasChild(kk)) {
                                Holder.mFavouritesBlack.setVisibility(View.GONE);
                                Holder.mFavouritesYellow.setVisibility(View.VISIBLE);

                            } else {
                                Holder.mFavouritesBlack.setVisibility(View.VISIBLE);
                                Holder.mFavouritesYellow.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                Holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent mew = new Intent(getActivity(), ExpandActivity.class);
                        mew.putExtra("user_id", kk);
                        startActivity(mew);

                    }
                });

                final int adapterPosition = Holder.getAdapterPosition();


                Holder.mFavouritesBlack.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {

                        mFav.child(puid).child(kk).child("fav_status").setValue("Added as fav").addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(), "Added to Fav", Toast.LENGTH_SHORT).show();

                                if (!itemStateArray.get(adapterPosition, false))
                                    Holder.mFavouritesBlack.setVisibility(View.GONE);
                                Holder.mFavouritesYellow.setVisibility(View.VISIBLE);


                            }
                        });
                        return true;
                    }
                });
                Holder.mFavouritesYellow.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        mFav.child(puid).child(kk).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(), "Removed from Fav", Toast.LENGTH_SHORT).show();
                                if (itemStateArray.get(adapterPosition, false))
                                    Holder.mFavouritesBlack.setVisibility(View.VISIBLE);
                                Holder.mFavouritesYellow.setVisibility(View.GONE);


                            }
                        });
                        return true;
                    }
                });

                if (position < previousPosition) {
                    AnimationUtil.animate(Holder, true);

                } else {

                }
                previousPosition = position;

            }

            @Override
            public UsersViewHolder1 onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout
                                .user_ad_layout, parent,
                        false);

                return new UsersViewHolder1(view);
            }
        };
        mallUsers.setAdapter(firebaseRecyclerAdapter);


        return homeView;


    }

    // start search and load suggestions methods
    private void startSearch(CharSequence text) {
        final String searchText = text.toString().toUpperCase();
        Query query1 = mDatabase1.orderByChild("title").startAt(searchText).endAt(searchText + "\uf8ff");
        FirebaseRecyclerOptions<Ad> firebaseRecyclerOptions2 = new FirebaseRecyclerOptions
                .Builder<Ad>()
                .setQuery(query1, Ad.class)
                .build();

        searchAdapter = new FirebaseRecyclerAdapter<Ad, UsersViewHolder1>(firebaseRecyclerOptions2) {
            @Override
            protected void onBindViewHolder(@NonNull UsersViewHolder1 holder, int position, @NonNull Ad ad1) {
                holder.setTitle(ad1.getTitle());
                holder.setPrice(ad1.getPrice());
                holder.setCategory(ad1.getCategory());
                holder.setImage(ad1.getImage(), getContext());
                holder.setTime(ad1.getTime());


                String user_id = getRef(position).getKey();
                final String kk = user_id.toString();

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent mew = new Intent(getActivity(), ExpandActivity.class);
                        mew.putExtra("user_id", kk);
                        startActivity(mew);

                    }
                });


            }

            @NonNull
            @Override
            public UsersViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout
                                .user_ad_layout, parent,
                        false);

                return new UsersViewHolder1(view1);
            }
        };
        searchAdapter.startListening();
        mallUsers.setAdapter(searchAdapter);


    }


    private void loadSuggest() {
        mDatabase1.orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postdataSnapshot : dataSnapshot.getChildren()) {
                    Ad item = postdataSnapshot.getValue(Ad.class);
                    if (item != null) {
                        suggestList.add(item.getTitle().toUpperCase());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    class UsersViewHolder1 extends RecyclerView.ViewHolder {

        View mView;

        private ImageButton mFavouritesBlack, mFavouritesYellow;


        public UsersViewHolder1(View itemView) {
            super(itemView);

            mView = itemView;
            mFavouritesBlack = mView.findViewById(R.id.ad_start_fav);
            mFavouritesYellow = mView.findViewById(R.id.ad_start_fav1);


        }


        public void setTitle(String title) {
            TextView userNameView = mView.findViewById(R.id.ad_title);
            userNameView.setText(title);
        }

        public void setPrice(String price) {
            TextView userNameView = mView.findViewById(R.id.ad_price);
            userNameView.setText(price);

        }

        public void setCategory(String category) {
            TextView userNameView = mView.findViewById(R.id.ad_cat);
            userNameView.setText(category);
        }


        public void setImage(String image, Context ctx) {
            ImageView userImageView = mView.findViewById(R.id.ad_Img);
            Picasso.with(ctx).load(image).placeholder(R.mipmap.defaultimg).into(userImageView);

        }

        public void setTime(String time) {
            TextView userTime = mView.findViewById(R.id.date);
            userTime.setText(time);
        }


    }


}