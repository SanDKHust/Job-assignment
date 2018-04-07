package vn.edu.hust.soict.khacsan.jobassignment.ui.search;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.ArrayList;

import vn.edu.hust.soict.khacsan.jobassignment.R;
import vn.edu.hust.soict.khacsan.jobassignment.model.Users;
import vn.edu.hust.soict.khacsan.jobassignment.untils.EmailUntils;

import static vn.edu.hust.soict.khacsan.jobassignment.untils.Constant.GROUPID;
import static vn.edu.hust.soict.khacsan.jobassignment.untils.Constant.SIZE;

public class FragmentSearchUser extends Fragment implements BaseQuickAdapter.OnItemClickListener,BaseQuickAdapter.OnItemLongClickListener{
    private RecyclerView recyclerView;
    private TextView mTextNotification;
    private UsersAdapter adapter;
    private ArrayList<Users> mListUsers;
    private DatabaseReference mDatabaseReferenceGroups,mDatabaseReferenceUsers;
    private String mGroupID;
    private int mSize;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_search_user, container, false);
        recyclerView = layout.findViewById(R.id.list_search_fr_user);
        mTextNotification = layout.findViewById(R.id.text_notification_fr_user);
        Bundle args = this.getArguments();

        if( args != null) {
            mSize = args.getInt(SIZE,0);
            mGroupID = args.getString(GROUPID);
        }


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new UsersAdapter(R.layout.item_user,null);
        adapter.setOnItemClickListener(FragmentSearchUser.this);
        adapter.setOnItemLongClickListener(FragmentSearchUser.this);
        recyclerView.setAdapter(adapter);

        mDatabaseReferenceGroups  = FirebaseDatabase.getInstance().getReference().child("groups");
        mDatabaseReferenceUsers = FirebaseDatabase.getInstance().getReference().child("users");

        return layout;
    }

    public void Search(final String searchText) {
        mListUsers = new ArrayList<>();

        if (EmailUntils.validate(searchText)) {
            mDatabaseReferenceUsers.limitToFirst(50).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String email = snapshot.child("email").getValue(String.class);
                        if (email != null && email.toLowerCase().equals(searchText.toLowerCase())) {
                            Users user = snapshot.getValue(Users.class);
                            if (user != null) {
                                user.setId(snapshot.getKey());
                                mListUsers.add(user);
                            }
                        }
                    }
                    adapter.setNewData(mListUsers);
                    if (mListUsers.isEmpty()) {
                        mTextNotification.setText("Không tìm thấy kết quả nào cho \"" + searchText + "\"");
                        mTextNotification.setVisibility(View.VISIBLE);
                    } else {
                        mTextNotification.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            mDatabaseReferenceUsers.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String name = snapshot.child("name").getValue(String.class);
                        if (name != null && name.toLowerCase().contains(searchText.toLowerCase())) {
                            Users user = snapshot.getValue(Users.class);
                            if (user != null) {
                                user.setId(snapshot.getKey());
                                mListUsers.add(user);
                            }
                        }
                    }
                    adapter.setNewData(mListUsers);
                    if (mListUsers.isEmpty()) {
                        mTextNotification.setText("Không tìm thấy kết quả nào cho \"" + searchText + "\"");
                        mTextNotification.setVisibility(View.VISIBLE);
                    } else {
                        mTextNotification.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public boolean onItemLongClick(final BaseQuickAdapter adapter, View view, final int position) {
        if(mGroupID == null) return false;
        final PopupMenu popup = new PopupMenu(getContext(), view);
        popup.getMenuInflater().inflate(R.menu.popup_item_user_search, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.action_add_to_group){
                    final Users user = (Users) adapter.getItem(position);
                    if(user != null) mDatabaseReferenceGroups.child(mGroupID).child("members").child(String.valueOf(mSize))
                            .setValue(user.getId()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mDatabaseReferenceUsers.child(user.getId()).child("groups")
                                    .child(user.getGroups() != null ? String.valueOf(user.getGroups().size()) : "0")
                                    .setValue(mGroupID).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    MDToast.makeText(getContext(), "Successful",
                                            MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                    adapter.remove(position);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    MDToast.makeText(getContext(), "Error: "+e.getMessage(),
                                            MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            MDToast.makeText(getContext(), "Error: "+e.getMessage(),
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
