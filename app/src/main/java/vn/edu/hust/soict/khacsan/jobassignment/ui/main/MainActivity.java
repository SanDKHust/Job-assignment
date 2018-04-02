package vn.edu.hust.soict.khacsan.jobassignment.ui.main;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import vn.edu.hust.soict.khacsan.jobassignment.R;
import vn.edu.hust.soict.khacsan.jobassignment.ui.login.LoginActivity;

import static vn.edu.hust.soict.khacsan.jobassignment.untils.Constant.PICK_FROM_GALLERY;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter mAdapterViewPager;
    private String[] mTabTitle;
    private Boolean isSelectTabGroup = false;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        innitView();
        setupViewPager();
        setupTabIcons();
    }

    private void innitView() {
        mTabTitle = getResources().getStringArray(R.array.tabs_title);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.viewpager);
        tabLayout =  findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(tabSelectedListener);
    }

    private TabLayout.OnTabSelectedListener tabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            int pos = tab.getPosition();
            tab.setText(mTabTitle[pos]);
            setTitle(mTabTitle[pos]);
            if(pos == 2) ((FragmentInfo)mAdapterViewPager.getItem(2)).updateUI(mAuth.getCurrentUser());
            if(pos == 1) if(!isSelectTabGroup){
                isSelectTabGroup = true;
                ((FragmentGroup)mAdapterViewPager.getItem(1)).updateUi();
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            tab.setText(null);
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    private void setupViewPager() {

        mAdapterViewPager = new ViewPagerAdapter(getSupportFragmentManager(),getApplicationContext());
        mAdapterViewPager.addFrag(new FragmentPerson(), mTabTitle[0]);
        mAdapterViewPager.addFrag(new FragmentGroup(), mTabTitle[1]);
        mAdapterViewPager.addFrag(new FragmentInfo(), mTabTitle[2]);

        viewPager.setAdapter(mAdapterViewPager);
        viewPager.setOffscreenPageLimit(3);

        tabLayout.getTabAt(0).setText(mTabTitle[0]);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }
    private void setupTabIcons() {
        int[] tabIcons = {
                R.drawable.ic_person,
                R.drawable.ic_group,
                R.drawable.ic_info
        };
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
        super.onStart();
    }
}
