package vn.edu.hust.soict.khacsan.jobassignment.ui.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import vn.edu.hust.soict.khacsan.jobassignment.R;

import static vn.edu.hust.soict.khacsan.jobassignment.untils.Constant.GROUPID;

/**
 * Created by San on 03/04/2018.
 */

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseUser mCurrentUser;
    private FirebaseAuth mAuth;
    private RecyclerView mRecyclerViewChat;
    private EditText mEdtWriteChat;
    private ImageButton mImgBtnSend;
    private DatabaseReference mDatabaseReferenceGroup;
    private String mIdGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initViews();
        Intent intent = getIntent();
        if(intent == null) return;
        mIdGroup = intent.getStringExtra(GROUPID);

        mDatabaseReferenceGroup = FirebaseDatabase.getInstance().getReference().child("groups").child(mIdGroup);
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        Log.i("HAHA", "onCreate: "+mIdGroup);

        setupUI();
    }

    private void setupUI() {
        mDatabaseReferenceGroup.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initViews() {
        mRecyclerViewChat = findViewById(R.id.recyclerChat);
        mEdtWriteChat = findViewById(R.id.editWriteMessage);
        mImgBtnSend = findViewById(R.id.btn_send);

        mImgBtnSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        mDatabaseReferenceGroup.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild("chat")){

                    mDatabaseReferenceGroup.setValue("chat");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
