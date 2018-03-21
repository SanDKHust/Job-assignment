package vn.edu.hust.soict.khacsan.jobassignment.ui.main;


import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.bvapp.arcmenulibrary.ArcMenu;
import com.bvapp.arcmenulibrary.widget.FloatingActionButton;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.valdesekamdem.library.mdtoast.MDToast;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import vn.edu.hust.soict.khacsan.jobassignment.R;
import vn.edu.hust.soict.khacsan.jobassignment.model.Group;
import vn.edu.hust.soict.khacsan.jobassignment.ui.chat.ChatActivity;

import static vn.edu.hust.soict.khacsan.jobassignment.untils.Constant.GROUPID;
import static vn.edu.hust.soict.khacsan.jobassignment.untils.Constant.SIZE;


/**
 * Created by San on 02/27/2018.
 */

public class FragmentGroup extends Fragment implements SwipeRefreshLayout.OnRefreshListener,GroupsAdapter.ListenerActionPopupMenu,BaseQuickAdapter.OnItemClickListener {
    private GroupsAdapter adapter;
    private List<Group> listGroup;
    private DialogPlus mDialogCreateGroup;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;
    private AlertDialog dialog;
    private AVLoadingIndicatorView avi;
    private TextView mTxtMessage;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_group, container, false);

        listGroup = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        SwipeRefreshLayout refresh = layout.findViewById(R.id.swipeRefreshLayout_group);
        refresh.setOnRefreshListener(this);

        avi = layout.findViewById(R.id.loading_indicator);
        mTxtMessage = layout.findViewById(R.id.txt_message);
        mTxtMessage.setVisibility(View.GONE);

        ArcMenu menu = layout.findViewById(R.id.arcMenu);
        setupArcMenu(menu);

        RecyclerView recyclerListGroups = layout.findViewById(R.id.recycleListGroup);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerListGroups.setLayoutManager(layoutManager);
        recyclerListGroups.setHasFixedSize(true);
        adapter = new GroupsAdapter(R.layout.item_group, listGroup,this);
        adapter.setOnItemClickListener(FragmentGroup.this);
        recyclerListGroups.setAdapter(adapter);

        dialog = new SpotsDialog(getContext(), R.style.CustomDialogCreateGroup);

        return layout;
    }


    private void setupArcMenu(ArcMenu menu) {
        menu.showTooltip(true);
        menu.setToolTipBackColor(Color.WHITE);
        menu.setToolTipCorner(4f);
        menu.setToolTipPadding(8f);
        menu.setToolTipSide(ArcMenu.TOOLTIP_UP);
        menu.setToolTipTextColor(Color.BLACK);
        menu.setAnim(300, 300, ArcMenu.ANIM_MIDDLE_TO_RIGHT, ArcMenu.ANIM_MIDDLE_TO_RIGHT,
                ArcMenu.ANIM_INTERPOLATOR_ACCELERATE_DECLERATE, ArcMenu.ANIM_INTERPOLATOR_ACCELERATE_DECLERATE);

        FloatingActionButton floatButtonGroupAdd = new FloatingActionButton(getContext());  // Use internal FAB as child
        floatButtonGroupAdd.setSize(FloatingActionButton.SIZE_MINI); // set initial size for child, it will create fab first
        floatButtonGroupAdd.setIcon(R.drawable.ic_group_add); // It will set fab icon from your resources which related to 'ITEM_DRAWABLES'
        floatButtonGroupAdd.setBackgroundColor(getResources().getColor(R.color.colorPrimary)); // it will set fab child's color
        menu.setChildSize(floatButtonGroupAdd.getIntrinsicHeight()); // set absolout child size for menu
        menu.addItem(floatButtonGroupAdd, "Create group", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //You can access child click in here
                    setUpDialogCreateGoup();

            }
        });

        FloatingActionButton floatButtonSearch = new FloatingActionButton(getContext());  // Use internal FAB as child
        floatButtonSearch.setSize(FloatingActionButton.SIZE_MINI); // set initial size for child, it will create fab first
        floatButtonSearch.setIcon(R.drawable.ic_search); // It will set fab icon from your resources which related to 'ITEM_DRAWABLES'
        floatButtonSearch.setBackgroundColor(getResources().getColor(R.color.colorPrimary)); // it will set fab child's color
        menu.setChildSize(floatButtonSearch.getIntrinsicHeight()); // set absolout child size for menu
        menu.addItem(floatButtonSearch, "Search", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //You can access child click in here

            }
        });

    }

    private void setUpDialogCreateGoup() {
        mDialogCreateGroup = DialogPlus.newDialog(getContext())
                .setContentHolder(new ViewHolder(R.layout.dialog_create_group))
                .setCancelable(true)
                .setPadding(16, 16, 16, 16)
                .setGravity(Gravity.CENTER)
                .create();
        final EditText edtName = (EditText) mDialogCreateGroup.findViewById(R.id.edt_nhap_ten_nhom);

        mDialogCreateGroup.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDialogCreateGroup.isShowing()) mDialogCreateGroup.dismiss();
            }
        });

        mDialogCreateGroup.findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("groups").push();
                final Group group = new Group();
                group.setAdmin(mCurrentUser.getUid());
                group.setId(mDatabaseReference.getKey());
                group.setName(edtName.getText().toString().trim());
                group.addMember(mCurrentUser.getUid());

                Map<String,String> map = new HashMap<>();
                map.put("name",group.getName());
                map.put("admin",group.getAdmin());

                mDatabaseReference.setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mDatabaseReference.child("members").child("0").setValue(group.getAdmin())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                MDToast.makeText(getContext(), "Successful",
                                        MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference()
                                        .child("users").child(mCurrentUser.getUid()).child("groups");
                                dbReference.child(String.valueOf(adapter.getData().size())).setValue(group.getId());

                                mDialogCreateGroup.dismiss();
                                dialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialog.dismiss();
                                MDToast.makeText(getContext(), "Error: "+e.getMessage(),
                                        MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        MDToast.makeText(getContext(), "Error: "+e.getMessage(),
                                MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                    }
                });
            }
        });
        mDialogCreateGroup.show();
    }

    @Override
    public void onRefresh() {

    }

    public void updateUi(){
        avi.show();
        final DatabaseReference dbReferenceInfoGroup =FirebaseDatabase.getInstance().getReference().child("groups");
        final DatabaseReference dbReferenceListGroups = FirebaseDatabase.getInstance().getReference().child("users").child(mCurrentUser.getUid());

        dbReferenceListGroups.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("groups")){
                    mTxtMessage.setVisibility(View.GONE);
                    dbReferenceListGroups.child("groups").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            final String id = dataSnapshot.getValue(String.class);
                            dbReferenceInfoGroup.child(id).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Group group = dataSnapshot.getValue(Group.class);
                                    group.setId(id);
                                    List<Group> groups = adapter.getData();
                                    if(groups != null){
                                        int i = 0;
                                        for(; i < groups.size(); i++){
                                            if(groups.get(i).getId().equals(id)){
                                                adapter.setData(i,group);
                                                break;
                                            }
                                        }
                                        if(i >= groups.size()) adapter.addData(group);
                                    }else {
                                        adapter.addData(group);
                                    }
                                    avi.hide();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }else {
                    mTxtMessage.setVisibility(View.VISIBLE);
                    avi.hide();
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
    public void onAddMember(String idGroup,int size) {
        Intent intent = new Intent(getContext(),SearchActivity.class);
        intent.putExtra(GROUPID,idGroup);
        intent.putExtra(SIZE,size);
        startActivity(intent);
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

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(getContext(),ChatActivity.class);
        intent.putExtra(GROUPID,((Group)adapter.getData().get(position)).getId());
        startActivity(intent);
    }
}
