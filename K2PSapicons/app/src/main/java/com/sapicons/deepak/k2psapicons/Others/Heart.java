package com.sapicons.deepak.k2psapicons.Others;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Heart {

    private static final String TAG = "Heart";


    private static final DecelerateInterpolator DECCELERATE_INTERPOLATOR = new DecelerateInterpolator();
    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();

    public ImageView starBlack, starYellow;

    public Heart(ImageView heartWhite, ImageView heartRed) {
        this.starBlack = heartWhite;
        this.starYellow = heartRed;

    }

    DatabaseReference mFav = FirebaseDatabase.getInstance().getReference().child("favourites");




    public void toggleLike(){
        Log.d(TAG, "toggleLike: toggling heart.");

        AnimatorSet animationSet =  new AnimatorSet();


        if(starYellow.getVisibility() == View.VISIBLE){
            Log.d(TAG, "toggleLike: toggling red heart off.");
            starYellow.setScaleX(0.1f);
            starYellow.setScaleY(0.1f);
            FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
            String puid = mCurrentUser.getUid();
            Ad ad = new Ad();
            String kk = ad.getId();
            mFav.child(puid).child(kk).child("fav_status").setValue("Added as " +
                    "fav");


            ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(starYellow, "scaleY", 1f, 0f);
            scaleDownY.setDuration(300);
            scaleDownY.setInterpolator(ACCELERATE_INTERPOLATOR);

            ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(starYellow, "scaleX", 1f, 0f);
            scaleDownX.setDuration(300);
            scaleDownX.setInterpolator(ACCELERATE_INTERPOLATOR);

            starYellow.setVisibility(View.GONE);
            starBlack.setVisibility(View.VISIBLE);

            animationSet.playTogether(scaleDownY, scaleDownX);
        }

        else if(starYellow.getVisibility() == View.GONE){
            Log.d(TAG, "toggleLike: toggling red heart on.");
            starYellow.setScaleX(0.1f);
            starYellow.setScaleY(0.1f);
            FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
            String puid = mCurrentUser.getUid();
            Ad ad = new Ad();
            String kk = ad.getId();
                    mFav.child(puid).child(kk).child("fav_status").setValue("Added as " +
                    "fav");

            ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(starYellow, "scaleY", 0.1f, 1f);
            scaleDownY.setDuration(300);
            scaleDownY.setInterpolator(DECCELERATE_INTERPOLATOR);

            ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(starYellow, "scaleX", 0.1f, 1f);
            scaleDownX.setDuration(300);
            scaleDownX.setInterpolator(DECCELERATE_INTERPOLATOR);

            starYellow.setVisibility(View.VISIBLE);
            starBlack.setVisibility(View.GONE);

            animationSet.playTogether(scaleDownY, scaleDownX);
        }

        animationSet.start();

    }
}
