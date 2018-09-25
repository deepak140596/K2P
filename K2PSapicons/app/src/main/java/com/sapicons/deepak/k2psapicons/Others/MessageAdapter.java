package com.sapicons.deepak.k2psapicons.Others;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sapicons.deepak.k2psapicons.R;

import java.util.List;
import java.util.Objects;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{
    protected List< Messages> mMessageList;

    public MessageAdapter(List<Messages> mMessageList) {
        this.mMessageList = mMessageList;
    }


    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.message_single_layout, parent, false);
        return new MessageViewHolder(v);
    }





    public class MessageViewHolder extends RecyclerView.ViewHolder{
        public TextView messageText,messageText1;

        public MessageViewHolder(View view) {
            super(view);
            messageText = view.findViewById(R.id.message_single_text);
            messageText1 = view.findViewById(R.id.message_single_text2);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder holder, int i) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

      String current_user_id = "";
        try {
            current_user_id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Messages c = mMessageList.get(i);
        String from_user = c.getFrom();
         if (from_user.equals(current_user_id)){
            holder.messageText.setVisibility(View.GONE);

          }else{
            holder.messageText1.setVisibility(View.GONE);



        }
        holder.messageText.setText(c.getMessage());
        holder.messageText1.setText(c.getMessage());

    }




    @Override
    public int getItemCount() {
        return mMessageList.size();
    }
}
