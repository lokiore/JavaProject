package com.example.rishabh;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by lokiore on 8/4/18.
 */

public class HomePagerAdapter extends FragmentPagerAdapter {

    public HomePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new NewsFeedFragment();
        } else if (position == 1){
            return new MessageFragment();
        }else {
            return new ProfileFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
