package vn.edu.hust.soict.khacsan.jobassignment.ui.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

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
import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import vn.edu.hust.soict.khacsan.jobassignment.R;
import vn.edu.hust.soict.khacsan.jobassignment.model.Messages;

import static vn.edu.hust.soict.khacsan.jobassignment.untils.Constant.GROUPID;

/**
 * Created by San on 03/04/2018.
 */

public class ChatActivity extends AppCompatActivity implements View.OnClickListener,BaseQuickAdapter.OnItemClickListener {
    private FirebaseUser mCurrentUser;
    private FirebaseAuth mAuth;
    private RecyclerView mRecyclerViewChat;
    private MessageAdapter adapter;
    private EditText mEdtWriteChat;
    private ImageButton mImgBtnSend;
    private DatabaseReference mDatabaseReferenceGroup;
    private DatabaseReference mDatabaseReferenceUser;
    private String mIdGroup;
    private LinearLayoutManager mLinearLayoutManager;
    private List<Messages> mListMessage;
    private View mFooterViewEndListChat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initViews();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Intent intent = getIntent();
        if (intent == null) return;
        mIdGroup = intent.getStringExtra(GROUPID);

        mDatabaseReferenceGroup = FirebaseDatabase.getInstance().getReference().child("groups").child(mIdGroup);
        mDatabaseReferenceUser = FirebaseDatabase.getInstance().getReference().child("users");

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        mListMessage = new ArrayList<>();
        adapter = new MessageAdapter(mListMessage);
        mRecyclerViewChat.setAdapter(adapter);
        mRecyclerViewChat.getLayoutManager().scrollToPosition(0);
        adapter.setOnItemClickListener(this);

        setupLoadMore();

        setupUI();
        mEdtWriteChat.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) mLinearLayoutManager.scrollToPosition(0);
            }
        });
    }

    private void setupLoadMore() {
        CustomLoadMoreView loadMoreView = new CustomLoadMoreView();

        adapter.setLoadMoreView(loadMoreView);
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                List<Messages> listMess = adapter.getData();
                final long key = listMess.get(listMess.size() - 1).getTime();
                mDatabaseReferenceGroup.child("chat").limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Messages message = null;
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            message = postSnapshot.getValue(Messages.class);
                        }

                        if (message != null && message.getTime() == key) {
                            adapter.addFooterView(mFooterViewEndListChat);
                            adapter.loadMoreEnd();
                        }
                        else
                            mDatabaseReferenceGroup.child("chat").orderByChild("time").endAt(key - 1).limitToLast(20)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            List<Messages> messagesList = new ArrayList<>();
                                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                                Messages messages = postSnapshot.getValue(Messages.class);
                                                if (messages != null) {
                                                    if (messages.getIdUserSend().equals(mCurrentUser.getUid()))
                                                        messages.setItemType(Messages.RIGHT);
                                                    else messages.setItemType(Messages.LEFT);
                                                    messagesList.add(messages);
                                                }
                                            }
                                            Collections.reverse(messagesList);
                                            adapter.addData(messagesList);
                                            adapter.loadMoreComplete();
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            adapter.loadMoreFail();
                                        }
                                    });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        adapter.loadMoreFail();
                    }
                });
            }
        }, mRecyclerViewChat);
    }


    private void setupUI() {
        mDatabaseReferenceGroup.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild("chat")) {
                    FirebaseDatabase.getInstance().getReference("groups/" + mIdGroup + "/chat");
                }
                if (dataSnapshot.hasChild("name")) {
                    String title = dataSnapshot.child("name").getValue(String.class);
                    if (title != null) setTitle(title);
                }
                if(dataSnapshot.hasChild("date")){
                    String date = dataSnapshot.child("date").getValue(String.class);
                    if (date != null) ((TextView)mFooterViewEndListChat.findViewById(R.id.text_ngay_tao_nhom)).setText(date);
                }
                mDatabaseReferenceGroup.child("chat").limitToLast(20).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Messages messages = dataSnapshot.getValue(Messages.class);
                        if (messages != null) {
                            if (messages.getIdUserSend().equals(mCurrentUser.getUid()))
                                messages.setItemType(Messages.RIGHT);
                            else messages.setItemType(Messages.LEFT);
                            adapter.addData(0, messages);
                            mLinearLayoutManager.scrollToPosition(0);
                        }
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

        mLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLinearLayoutManager.setReverseLayout(true);
        mRecyclerViewChat.setLayoutManager(mLinearLayoutManager);

        mFooterViewEndListChat = LayoutInflater.from(getApplicationContext()).inflate(R.layout.foorer_view_end_chat,null);
        mImgBtnSend.setOnClickListener(this);

        mEdtWriteChat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mEdtWriteChat.getText().toString().trim().isEmpty()) {
                    mImgBtnSend.setClickable(false);
                   // mImgBtnSend.setI(Color.parseColor("#FFADB5B7"));
                }
                else {
                    mImgBtnSend.setClickable(true);
                    //mImgBtnSend.setBackgroundColor(Color.parseColor("#1b8efa"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        Messages messages = new Messages(mEdtWriteChat.getText().toString(), mCurrentUser.getUid(), "text", System.currentTimeMillis(), false);
        mDatabaseReferenceGroup.child("chat").push().setValue(messages)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mLinearLayoutManager.scrollToPosition(0);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        MDToast.makeText(ChatActivity.this, "Error: " + e.getMessage(),
                                MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                    }
                });
        mEdtWriteChat.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chat_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }else if(id == R.id.action_cv_nhom){
            Intent intent = new Intent(ChatActivity.this,TeamWorkingActivity.class);
            intent.putExtra(GROUPID,mIdGroup);
            startActivity(intent);
        } else if(id == R.id.action_cv_caNhan){

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Messages mess = (Messages) adapter.getItem(position);
        if(mess != null && mess.getItemType() == Messages.RIGHT){
            View v = view.findViewById(R.id.tm_message_right);
            if(v.getVisibility() == View.VISIBLE)
                v.setVisibility(View.GONE);
            else v.setVisibility(View.VISIBLE);
        }else {
            View v = view.findViewById(R.id.tm_message_left);
            if(v.getVisibility() == View.VISIBLE)
                v.setVisibility(View.GONE);
            else v.setVisibility(View.VISIBLE);
        }

    }
}
