package com.sapicons.deepak.k2psapicons.Others;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sapicons.deepak.k2psapicons.Activities.ExpandActivity;
import com.sapicons.deepak.k2psapicons.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class LocalAdapter extends RecyclerView.Adapter<LocalAdapter.HorizontalViewHolder> {

    private Context mContext;

    protected List< Local> mLocalList;

    public LocalAdapter(Context mContext, List<Local> mLocalList) {
        this.mContext = mContext;
        this.mLocalList = mLocalList;
    }

    @NonNull
    @Override
    public HorizontalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.local_ads, parent, false);
        return new HorizontalViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalViewHolder holder, int position) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        int previous_position = 0;

        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();


        String current_user_id = "";
        try {
            current_user_id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        } catch (Exception e) {
            Log.d("User-id","Unable to retrieve");
        }

        Local l = mLocalList.get(position);
        final String kk = l.getAd();

        Picasso.with(mContext)
                .load(l.getImage())
                .placeholder(R.mipmap.defaultimg)
                .into(holder.mImage);

        holder.mTitle.setText(l.getTitle());
        holder.mPrice.setText(l.getPrice());

        GetTimeAgo getTimeAgo = new GetTimeAgo();
        long sentTime = Long.parseLong(String.valueOf(l.getTimestamp()));
        String sent_Time = getTimeAgo.getTimeAgo(sentTime, mContext);
        holder.mDate.setText(sent_Time);

        if (position > previous_position) {
            AnimationUtil.animate(holder, true);

        } else {
            AnimationUtil.animate(holder, false);
        }
        previous_position = position;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mew = new Intent(mContext, ExpandActivity.class);
                mew.putExtra("user_id", kk);
                mContext.startActivity(mew);
            }
        });




    }

    @Override
    public int getItemCount() {
        return mLocalList.size();
    }

    public class HorizontalViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView mImage;
        private TextView mTitle, mPrice, mDate;

        public HorizontalViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mImage = itemView.findViewById(R.id.horizontal_image);
            mTitle = itemView.findViewById(R.id.horizontal_title);
            mPrice = itemView.findViewById(R.id.horizontal_price);
            mDate = itemView.findViewById(R.id.time_hor);
        }
    }
}
