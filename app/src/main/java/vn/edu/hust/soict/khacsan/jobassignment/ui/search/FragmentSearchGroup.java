package vn.edu.hust.soict.khacsan.jobassignment.ui.search;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import vn.edu.hust.soict.khacsan.jobassignment.R;
import vn.edu.hust.soict.khacsan.jobassignment.model.Group;
import vn.edu.hust.soict.khacsan.jobassignment.ui.main.GroupsAdapter;

import static vn.edu.hust.soict.khacsan.jobassignment.untils.Constant.GROUPID;
import static vn.edu.hust.soict.khacsan.jobassignment.untils.Constant.SIZE;

public class FragmentSearchGroup extends Fragment implements GroupsAdapter.ListenerActionPopupMenu {
    private DatabaseReference mDatabaseReferenceGroups,mDatabaseReferenceUsers;
    private ArrayList<Group> mListGroups;
    private RecyclerView mRecyclerView;
    private TextView mTextNotification;
    private GroupsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_search_group, container, false);

        mRecyclerView = layout.findViewById(R.id.list_search_fr_group);
        mTextNotification = layout.findViewById(R.id.text_notification_fr_group);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        mRecyclerView.setHasFixedSize(true);
        adapter = new GroupsAdapter(R.layout.item_group,null,this);
        mRecyclerView.setAdapter(adapter);

        mDatabaseReferenceGroups  = FirebaseDatabase.getInstance().getReference().child("groups");
        mDatabaseReferenceUsers = FirebaseDatabase.getInstance().getReference().child("users");

        return layout;
    }

    public void  Search(final String searchText){
        mListGroups = new ArrayList<>();
        mDatabaseReferenceGroups.limitToFirst(50).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final Group group = snapshot.getValue(Group.class);

                    if(group != null && (group.getName().toLowerCase().contains(searchText.toLowerCase()) ||
                            group.getDate().toLowerCase().contains(searchText.toLowerCase()) )){
                        mListGroups.add(group);
                        continue;
                    }
                    if(group != null){
                        List<String> members = group.getMembers();
                        for (String member: members) {
                            mDatabaseReferenceUsers.child(member).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String name = dataSnapshot.child("name").getValue(String.class);
                                    if(name != null && name.contains(searchText)) mListGroups.add(group);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                }
                adapter.setNewData(mListGroups);
                if (mListGroups.isEmpty()) {
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

    @Override
    public void onEdit(Group group) {

    }

    @Override
    public void onAddMember(String idGroup, int size) {

    }

    @Override
    public void onDelete(Group group) {

    }

    @Override
    public void onInfo(Group group) {

    }

    @Override
    public void onLeave(String idGroup) {

    }
}
