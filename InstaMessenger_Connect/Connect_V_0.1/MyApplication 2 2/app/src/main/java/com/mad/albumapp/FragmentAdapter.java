package com.mad.albumapp;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Sriharish on 11/29/2015.
 */
public class FragmentAdapter extends FragmentPagerAdapter
{
        int i =0;
        final int PAGE_COUNT = 3;
        // Tab Titles
        private String tabtitles[] = new String[] { "USER PROFILE", "ALBUM", "MESSAGES" };
        Context context;

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {

                switch (position) {
                    case 0:

                        UserProfileFragment userProfileFragment = new UserProfileFragment();
                        return userProfileFragment;

                    /*FragmentTab2 fragmenttab2 = new FragmentTab2();
                    return fragmenttab2;
*/
                    // Open FragmentTab3.java
                    case 1:

                        AlbumFragment albumFragment = new AlbumFragment();
                        return albumFragment;
                    /*
                    FragmentTab3 fragmenttab3 = new FragmentTab3();*/
                    case 2:
                        MessageFragment messageFragment = new MessageFragment();
                        return messageFragment;

            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabtitles[position];
        }
}