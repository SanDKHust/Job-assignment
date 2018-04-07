package vn.edu.hust.soict.khacsan.jobassignment.ui.search;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.miguelcatalan.materialsearchview.MaterialSearchView;


import vn.edu.hust.soict.khacsan.jobassignment.R;
import vn.edu.hust.soict.khacsan.jobassignment.ui.main.ViewPagerAdapter;

import static vn.edu.hust.soict.khacsan.jobassignment.untils.Constant.GROUPID;
import static vn.edu.hust.soict.khacsan.jobassignment.untils.Constant.SIZE;

public class SearchActivity extends AppCompatActivity {
    private MaterialSearchView searchView;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private ViewPagerAdapter mPagerAdapter;


    private String mGroupID ="";
    private int mSize;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchView = findViewById(R.id.search_view);
        mViewPager = findViewById(R.id.viewpager_search);
        mTabLayout = findViewById(R.id.tab_search);

        mTabLayout.setupWithViewPager(mViewPager);

        Intent intent = getIntent();
        if(intent != null){
            mGroupID = intent.getStringExtra(GROUPID);
            mSize = intent.getIntExtra(SIZE,0);
        }

        setupViewPager();
        setupSearchView();


    }

    private void setupSearchView() {
        searchView.setFocusable(true);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (searchView.isFocusable()) {
                    searchView.clearFocus();
                }
                if(mTabLayout.getSelectedTabPosition() == 0){
                    ((FragmentSearchUser)mPagerAdapter.getItem(0)).Search(query.trim());
                }else {
                    ((FragmentSearchGroup)mPagerAdapter.getItem(1)).Search(query.trim());
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                finish();
            }
        });
        searchView.showSearch(false);
        searchView.showSearch();

    }


    private void setupViewPager() {

        mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        FragmentSearchUser searchUser = new FragmentSearchUser();
        Bundle args = new Bundle();
        args.putString(GROUPID,mGroupID);
        args.putInt(SIZE,mSize);

        searchUser.setArguments(args);
        mPagerAdapter.addFrag(searchUser);

        FragmentSearchGroup searchGroup = new FragmentSearchGroup();

        mPagerAdapter.addFrag(searchGroup);


        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);

        mTabLayout.getTabAt(0).setText("Users");
        mTabLayout.getTabAt(1).setText("Groups");

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(tabSelectedListener);
    }


    private TabLayout.OnTabSelectedListener tabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            int pos = tab.getPosition();

        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };



//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
////        getMenuInflater().inflate(R.menu.menu_search, menu);
////
////        MenuItem item = menu.findItem(R.id.action_search);
////        searchView.setMenuItem(item);
////        searchView.performClick();
//        return true;
//    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen())
            searchView.closeSearch();
            super.onBackPressed();
    }

}
