package com.sapicons.deepak.k2psapicons.Fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.sapicons.deepak.k2psapicons.Activities.ChatActivity;
import com.sapicons.deepak.k2psapicons.Others.Chat;
import com.sapicons.deepak.k2psapicons.Others.GetTimeAgo;
import com.sapicons.deepak.k2psapicons.R;
import com.squareup.picasso.Picasso;


/**
 * created by Divyansh
 */
public class MessageFragment extends Fragment {

    private FirebaseRecyclerAdapter<Chat, MessageFragment.ConvViewHolder> firebaseRecyclerAdapter;
    protected String adId1, mChatUser, mChatUserName;


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

    public MessageFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View mMainView = inflater.inflate(R.layout.fragment_message, container, false);


        RecyclerView mConvList = mMainView.findViewById(R.id.recycler_message);
        registerForContextMenu(mConvList);

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final String mCurrent_user_id;
        mCurrent_user_id = mAuth.getCurrentUser().getUid();


        final DatabaseReference mConvDatabase = FirebaseDatabase.getInstance().getReference().child("Chats").child(mCurrent_user_id);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mConvList.setLayoutManager(linearLayoutManager);


        Query conversationQuery = mConvDatabase.orderByChild("timestamp");
        FirebaseRecyclerOptions<Chat> firebaseRecyclerOptions = new FirebaseRecyclerOptions
                .Builder<Chat>()
                .setQuery(conversationQuery, Chat.class)
                .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Chat, ConvViewHolder>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull final ConvViewHolder holder, int i, @NonNull final Chat model) {
                holder.setTitle(model.getTitle());
                holder.setPrice(model.getPrice());
                holder.setImage(model.getImage(), getContext());
                holder.setName(model.getChatUser());
                final String ad_id = model.getAds();
                final String chat_user = model.getChatUserId();
                final String chat_name = model.getChatUser();

                if (ad_id!= null) {
                    DatabaseReference mMessage = FirebaseDatabase.getInstance().getReference().child("messages").child(mCurrent_user_id);
                    Query msgQuery = mMessage.child(chat_user).child(ad_id).limitToLast(1);
                    msgQuery.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            String data = dataSnapshot.child("message").getValue().toString();
                            String time = dataSnapshot.child("time").getValue().toString();
                            holder.setMessage(data, model.isSeen());
                            holder.setTime(time);
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else {
                    Log.d("Chat_excep", "The null pointer still exists");
                }

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent chatIntent = new Intent(getContext(), ChatActivity.class);
                        chatIntent.putExtra("ad_id", ad_id);
                        chatIntent.putExtra("user_id", chat_user);
                        chatIntent.putExtra("user_name", chat_name);
                        startActivity(chatIntent);

                    }
                });


            }

            @NonNull
            @Override
            public ConvViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout
                                .message_single_layout0, parent,
                        false);

                return new ConvViewHolder(view);
            }
        };
        mConvList.setAdapter(firebaseRecyclerAdapter);

        return mMainView;
    }

    public class ConvViewHolder extends RecyclerView.ViewHolder {

        View mView;


        public ConvViewHolder(View itemView) {
            super(itemView);

            mView = itemView;


        }

        public void setMessage(String data, boolean isSeen) {
            TextView userStatusView = mView.findViewById(R.id.message_messages);
            userStatusView.setText(data);

            if (!isSeen) {
                userStatusView.setTypeface(userStatusView.getTypeface(), Typeface.BOLD);
            } else {
                userStatusView.setTypeface(userStatusView.getTypeface(), Typeface.NORMAL);
            }
        }

        public void setName(String name) {
            TextView userNameView = mView.findViewById(R.id.user_single_name0);
            userNameView.setText(name);
        }


        public void setTime(String time) {
            GetTimeAgo getTimeAgo = new GetTimeAgo();
            long sentTime = Long.parseLong(time);
            String sent_Time = getTimeAgo.getTimeAgo(sentTime, getContext());
            TextView mSentTime = mView.findViewById(R.id.time_ago0);
            mSentTime.setText(sent_Time);
        }

        public void setTitle(String title) {
            TextView userNameView = mView.findViewById(R.id.messsage_ad_title);
            userNameView.setText(title);
        }

        public void setPrice(String price) {
            TextView userNameView = mView.findViewById(R.id.message_ad_price);
            userNameView.setText(price);
        }

        public void setImage(String image, Context ctx) {
            ImageView userImageView = mView.findViewById(R.id.sing_Img0);
            Picasso.with(ctx).load(image).placeholder(R.mipmap.defaultimg).into(userImageView);

        }
    }


}