package com.aile.photos;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JW on 15. 12. 9..
 */
public class Adapter extends FragmentStatePagerAdapter {
    private static final String LOG_TAG1 = "Adapter";
    private static final String LOG_TAG2 = Common.LOG_TAG_STRING;

    private final List<Fragment> mFragments = new ArrayList<>();
    private final List<String> mFragmentTitles = new ArrayList<>();

    public Adapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment, String date) {
        mFragments.add(fragment);
        mFragmentTitles.add(date); // 제목 넘기는 부분, 14, 15, 16, 17
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return mFragmentTitles.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
