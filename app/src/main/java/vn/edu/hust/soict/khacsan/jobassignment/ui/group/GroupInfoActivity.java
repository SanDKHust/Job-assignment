package vn.edu.hust.soict.khacsan.jobassignment.ui.group;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import vn.edu.hust.soict.khacsan.jobassignment.R;
import vn.edu.hust.soict.khacsan.jobassignment.model.Group;
import vn.edu.hust.soict.khacsan.jobassignment.model.Users;
import vn.edu.hust.soict.khacsan.jobassignment.ui.search.UsersAdapter;

import static vn.edu.hust.soict.khacsan.jobassignment.ui.main.FragmentGroup.GROUP;

public class GroupInfoActivity extends AppCompatActivity {
    private Group group;
    private RecyclerView listMember;
    private TextView mTxtName, mTxtDate,mTxtIconGroup;
    private UsersAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);
        initView();
        Intent intent = getIntent();
        if(intent == null) return;

        group = intent.getParcelableExtra(GROUP);
        mTxtName.setText(group.getName());
        mTxtDate.setText(group.getDate());
        mTxtIconGroup.setText(String.valueOf(group.getName().charAt(0)).toUpperCase());
        updateData();

    }

    private void updateData() {
        adapter = new UsersAdapter(R.layout.item_user,null);
        FirebaseDatabase.getInstance().getReference().child("groups").child(group.getId()).child("members")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot d: dataSnapshot.getChildren()){
                            final String userId = d.getValue(String.class);
                            if(userId != null) FirebaseDatabase.getInstance().getReference().child("users").child(userId)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            Users users = dataSnapshot.getValue(Users.class);
                                            if (users != null) {
                                                users.setId(userId);
                                            }
                                            if(users != null && users.getId().trim().equals(group.getAdmin().trim())){
                                                ((TextView) findViewById(R.id.text_item_displayName)).setText(users.getName());
                                                ((TextView) findViewById(R.id.text_item_status)).setText(users.getStatus());
                                                Picasso.with(getApplicationContext())
                                                        .load(users.getThumb_image())
                                                        .placeholder(R.drawable.default_avatar)
                                                        .error(R.drawable.ic_error)
                                                        .into((CircleImageView) findViewById(R.id.profile_item_image));
                                            }else if (users != null) {
                                                adapter.addData(users);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        listMember.setAdapter(adapter);
    }

    private void initView() {
        listMember = findViewById(R.id.list_member_group_info);
        mTxtName = findViewById(R.id.text_name_group_info);
        mTxtDate = findViewById(R.id.text_creat_date_group_info);
        mTxtIconGroup = findViewById(R.id.icon_group_info);

        listMember.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        listMember.setHasFixedSize(true);

    }
}
