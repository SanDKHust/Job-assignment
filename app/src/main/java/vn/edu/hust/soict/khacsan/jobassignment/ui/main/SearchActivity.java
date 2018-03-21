package vn.edu.hust.soict.khacsan.jobassignment.ui.main;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.ArrayList;

import vn.edu.hust.soict.khacsan.jobassignment.R;
import vn.edu.hust.soict.khacsan.jobassignment.model.Users;
import vn.edu.hust.soict.khacsan.jobassignment.untils.EmailUntils;

import static vn.edu.hust.soict.khacsan.jobassignment.untils.Constant.GROUPID;
import static vn.edu.hust.soict.khacsan.jobassignment.untils.Constant.SIZE;

public class SearchActivity extends AppCompatActivity implements BaseQuickAdapter.OnItemClickListener,BaseQuickAdapter.OnItemLongClickListener{
    private MaterialSearchView searchView;
//    private Toolbar mTopToolbar;
    private DatabaseReference mDatabaseReferenceUsers;
    private DatabaseReference mDatabaseReferenceGroups;
    private RecyclerView recyclerView;
    private TextView mTextNotification;
    private  UsersAdapter adapter;
    private ArrayList<Users> mListUsers;
    private String mGroupID ="";
    private int mSize;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchView = findViewById(R.id.search_view);
        recyclerView = findViewById(R.id.list_search);
//        mTopToolbar = findViewById(R.id.toolbar);
        mTextNotification = findViewById(R.id.text_notification);


        Intent intent = getIntent();
        if(intent != null){
            mGroupID = intent.getStringExtra(GROUPID);

            mSize = intent.getIntExtra(SIZE,0);
            Log.i("HAHA", "onCreate: "+mGroupID+" "+mSize);
        }
//        setSupportActionBar(mTopToolbar);
//        mTopToolbar.setTitle("Search");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UsersAdapter(R.layout.item_user,null);
        recyclerView.setAdapter(adapter);
        mDatabaseReferenceUsers = FirebaseDatabase.getInstance().getReference().child("users");
        mDatabaseReferenceGroups  = FirebaseDatabase.getInstance().getReference().child("groups");

        searchView.setFocusable(true);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (searchView.isFocusable()) {
                    searchView.clearFocus();
                }
                Search(query);
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

    private void Search(final String searchText) {
        mListUsers = new ArrayList<>();

        if(EmailUntils.validate(searchText)){
            mDatabaseReferenceUsers.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String email = snapshot.child("email").getValue(String.class);
                        if (email != null && email.toLowerCase().equals(searchText.toLowerCase())) {
                            Users user = snapshot.getValue(Users.class);
                            user.setId(snapshot.getKey());
                            mListUsers.add(user);
                        }
                    }
                    if(mListUsers.isEmpty()){
                        mTextNotification.setText("Không tìm thấy kết quả nào cho \""+searchText+"\"");
                        mTextNotification.setVisibility(View.VISIBLE);
                    }else {
                        mTextNotification.setVisibility(View.GONE);
                        adapter.setNewData(mListUsers);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else {
            mDatabaseReferenceUsers.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String name = snapshot.child("name").getValue(String.class);
                        if (name != null && name.toLowerCase().contains(searchText.toLowerCase())) {
                            Users user = snapshot.getValue(Users.class);
                            user.setId(snapshot.getKey());
                            mListUsers.add(user);
                        }
                    }
                    if(mListUsers.isEmpty()){
                        mTextNotification.setText("Không tìm thấy kết quả nào cho \""+searchText+"\"");
                        mTextNotification.setVisibility(View.VISIBLE);
                    }else {
                        mTextNotification.setVisibility(View.GONE);
                        adapter.setNewData(mListUsers);
                        adapter.setOnItemClickListener(SearchActivity.this);
                        adapter.setOnItemLongClickListener(SearchActivity.this);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

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

    @Override
    protected void onStart() {
        super.onStart();
        //adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //adapter.stopListening();
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public boolean onItemLongClick(final BaseQuickAdapter adapter, View view, final int position) {
        final PopupMenu popup = new PopupMenu(getApplicationContext(), view);
        popup.getMenuInflater().inflate(R.menu.popup_item_user_search, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.action_add_to_group){
                    final Users user = (Users) adapter.getItem(position);
                    Log.i("HAHA", "onMenuItemClick: "+mGroupID);
                    mDatabaseReferenceGroups.child(mGroupID).child("members").child(String.valueOf(mSize))
                            .setValue(user.getId()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mDatabaseReferenceUsers.child(user.getId()).child("groups")
                                    .child(user.getGroups() != null ? String.valueOf(user.getGroups().size()) : "0")
                                    .setValue(mGroupID).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    MDToast.makeText(getApplicationContext(), "Successful",
                                            MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                    adapter.remove(position);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    MDToast.makeText(getApplicationContext(), "Error: "+e.getMessage(),
                                            MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            MDToast.makeText(getApplicationContext(), "Error: "+e.getMessage(),
                                    MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                        }
                    });
                }
                return true;
            }
        });
        popup.show();
        return true;
    }
}
