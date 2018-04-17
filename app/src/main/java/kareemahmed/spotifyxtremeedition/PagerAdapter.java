package kareemahmed.spotifyxtremeedition;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {
    private static int numItems = 2;

    public PagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
            public int getCount () {
                // Show 3 total pages.
                return numItems;
            }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return new RootFragment();
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return PlaylistFragment.newInstance();
            default:
                return null;
        }
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }
    }
