package com.sapicons.deepak.k2psapicons.Others;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.sapicons.deepak.k2psapicons.Fragments.FavoFragment;
import com.sapicons.deepak.k2psapicons.Fragments.HomeFragment;
import com.sapicons.deepak.k2psapicons.Fragments.MessageFragment;
import com.sapicons.deepak.k2psapicons.Fragments.PostFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {

    int mNoOfTabs;
    public PagerAdapter(FragmentManager fm, int NumberOfTabs) {
        super(fm);
        this.mNoOfTabs = NumberOfTabs;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {

        switch (position){

            case 0:
                HomeFragment tab1 = new HomeFragment();
                return tab1;

            case 1:
                PostFragment tab2 = new PostFragment();
                return tab2;

            case 2:
                FavoFragment tab3 = new FavoFragment();
                return tab3;
            case 3:
                MessageFragment tab4 = new MessageFragment();
                return tab4;
            default:
                return null;

        }

    }

    @Override
    public int getCount() {
        return mNoOfTabs;
    }
}
