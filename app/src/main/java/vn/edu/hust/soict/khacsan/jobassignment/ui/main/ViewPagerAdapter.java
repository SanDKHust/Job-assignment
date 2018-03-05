package vn.edu.hust.soict.khacsan.jobassignment.ui.main;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import vn.edu.hust.soict.khacsan.jobassignment.R;


/**
 * Created by San on 02/27/2018.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<String> mTitles;
    private List<Fragment> mFragments;
    public ViewPagerAdapter(FragmentManager fm,Context context) {
        super(fm);
        mFragments = new ArrayList<>();
        mTitles = new ArrayList<>();
    }

    public void addFrag(Fragment fragment,String title){
        mFragments.add(fragment);
        mTitles.add(title);
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
        // return null to display only the icon
        return null;
    }

}
