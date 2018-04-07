package vn.edu.hust.soict.khacsan.jobassignment.ui.main;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import vn.edu.hust.soict.khacsan.jobassignment.R;
import vn.edu.hust.soict.khacsan.jobassignment.ui.login.LoginActivity;
import vn.edu.hust.soict.khacsan.jobassignment.ui.search.SearchActivity;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private ViewPagerAdapter mPagerAdapter;
    private MenuItem prevMenuItem;
    BottomNavigationView mNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mToolbar = findViewById(R.id.toolbar_main);
        mViewPager = findViewById(R.id.viewpager_main);
        mNavigation = findViewById(R.id.navigation_menu_main);
        setSupportActionBar(mToolbar);

        setupFragment();
        mNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) mNavigation.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

        findViewById(R.id.search_toolbar_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
            }
        });



//        loadFragment(new FragmentPersonal());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_personal:
                    mViewPager.setCurrentItem(0);
//                    loadFragment(new FragmentPersonal());
                    return true;
                case R.id.navigation_groups:

                    mViewPager.setCurrentItem(1);
                    ((FragmentGroup)((ViewPagerAdapter)mViewPager.getAdapter()).getItem(1)).updateUi();
//                    loadFragment(new FragmentGroup());
                    return true;
                case R.id.navigation_profile:
                    mViewPager.setCurrentItem(2);
                    ((FragmentProfile)((ViewPagerAdapter)mViewPager.getAdapter()).getItem(2)).updateUI(mAuth.getCurrentUser());
//                    loadFragment(new FragmentProfile());
                    return true;
            }
            return false;
        }


    };

    private void setupFragment(){
        mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mPagerAdapter.addFrag(new FragmentPersonal());
        mPagerAdapter.addFrag(new FragmentGroup());
        mPagerAdapter.addFrag(new FragmentProfile());
        mViewPager.setAdapter(mPagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else
                {
                    mNavigation.getMenu().getItem(0).setChecked(false);
                }

                if(position == 0){
                    mNavigation.setSelectedItemId(R.id.navigation_personal);
                }else if(position == 1){
                    mNavigation.setSelectedItemId(R.id.navigation_groups);
                }else mNavigation.setSelectedItemId(R.id.navigation_profile);

                mNavigation.getMenu().getItem(position).setChecked(true);
                prevMenuItem = mNavigation.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

//    private void loadFragment(Fragment fragment) {
//        // load fragment
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.frame_container, fragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
//    }


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
        }else if (currentUser.getPhotoUrl() != null) {
            Picasso.with(getApplicationContext()).load(currentUser.getPhotoUrl())
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.drawable.ic_account_circle)
                    .error(R.drawable.ic_error)
                    .into((CircleImageView)findViewById(R.id.circle_img_actionbar));
        }
        super.onStart();
    }
}
