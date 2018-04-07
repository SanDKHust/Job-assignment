package vn.edu.hust.soict.khacsan.jobassignment.ui.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vn.edu.hust.soict.khacsan.jobassignment.R;

/**
 * Created by San on 02/27/2018.
 */

public class FragmentPersonal extends Fragment {

    public FragmentPersonal() {
        // Required empty public constructor
    }

    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_personal, container, false);
        ViewPager viewPager = layout.findViewById(R.id.viewpager_table_work);
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        pagerAdapter.addFrag(new FragmentTableWork());
        pagerAdapter.addFrag(new FragmentTableWork());
        pagerAdapter.addFrag(new FragmentTableWork());
        viewPager.setAdapter(pagerAdapter);
        return layout;
    }
}
