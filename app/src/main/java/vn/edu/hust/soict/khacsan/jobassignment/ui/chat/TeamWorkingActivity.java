package vn.edu.hust.soict.khacsan.jobassignment.ui.chat;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import vn.edu.hust.soict.khacsan.jobassignment.R;
import vn.edu.hust.soict.khacsan.jobassignment.model.Users;
import vn.edu.hust.soict.khacsan.jobassignment.model.Work;

import static vn.edu.hust.soict.khacsan.jobassignment.untils.Constant.GROUPID;
import static vn.edu.hust.soict.khacsan.jobassignment.untils.Constant.USERID;

public class TeamWorkingActivity extends AppCompatActivity implements View.OnClickListener, WorkAdapter.catchEventItemWork {

    private RecyclerView mRcVListCVNhom, mRVListUsers;
    private FloatingActionButton mFabAddCVNhom;
    private DialogPlus mDialogCreateCV = null;
    private EditText mEdtDeadline, mEdtName, mEdtDescription;
    private List<Users> mListUsers;
    private List<Users> mListUserSelect;
    private UsersAdapterSelect adapter;
    private DatabaseReference mDRListMember, mDatabaseReferenceUsers, mDatabaseReferenceGroup;
    private String mGroupId, mUserId;
    private BaseQuickAdapter mWorkAdapter;
    private TextView mTextViewNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_working);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        Intent intent = getIntent();
        if (intent == null) return;

        mDatabaseReferenceUsers = FirebaseDatabase.getInstance().getReference().child("users");
        mRcVListCVNhom = findViewById(R.id.list_cv_nhom);
        mFabAddCVNhom = findViewById(R.id.fab_btn_add_cv);
        mTextViewNotification = findViewById(R.id.text_show_chua_co_cv);

        mRcVListCVNhom.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mFabAddCVNhom.setOnClickListener(this);
        if (intent.hasExtra(GROUPID)) {
            mGroupId = intent.getStringExtra(GROUPID);
            mDRListMember = FirebaseDatabase.getInstance().getReference().child("groups").child(mGroupId).child("members");
            mDatabaseReferenceGroup = FirebaseDatabase.getInstance().getReference().child("groups").child(mGroupId);
        } else return;

        if (intent.hasExtra(USERID)) {
            mUserId = intent.getStringExtra(USERID);
            setupListWorkPersonal();
            mFabAddCVNhom.setVisibility(View.GONE);
            setTitle("Công việc cá nhân");
        } else {
            setTitle("Công việc nhóm");
            setupListWork();
            setupListMembers();
        }
    }

    private void setupListWorkPersonal() {
        mWorkAdapter = new WorkPersonAdapter(R.layout.item_work_person,null);
        mDatabaseReferenceGroup.child("works").limitToLast(50).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Work work = dataSnapshot.getValue(Work.class);
                if (work != null && work.getMembers().contains(mUserId)) {
                    mWorkAdapter.addData(work);
                }
                if (mWorkAdapter.getData().size() != 0) {
                    mTextViewNotification.setVisibility(View.GONE);
                } else mTextViewNotification.setVisibility(View.VISIBLE);
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

        mRcVListCVNhom.setAdapter(mWorkAdapter);

    }


    private void setupListWork() {
        mWorkAdapter = new WorkAdapter(R.layout.item_work, null,this);
        mDatabaseReferenceGroup.child("works").limitToLast(50).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Work work = dataSnapshot.getValue(Work.class);
                if (work != null)
                    mWorkAdapter.addData(work);
                if (mWorkAdapter.getData().size() != 0) {
                    mTextViewNotification.setVisibility(View.GONE);
                } else mTextViewNotification.setVisibility(View.VISIBLE);

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

        mRcVListCVNhom.setAdapter(mWorkAdapter);

    }

    private void setupListMembers() {
        mListUsers = new ArrayList<>();
        mListUserSelect = new ArrayList<>();

        adapter = new UsersAdapterSelect(R.layout.item_users_select, mListUsers);

        mDRListMember.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final String id = dataSnapshot.getValue(String.class);
                if (id != null) {
                    mDatabaseReferenceUsers.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Users user = dataSnapshot.getValue(Users.class);
                            if (user != null) {
                                user.setId(id);
                                adapter.addData(user);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
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

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CheckBox checkBox = view.findViewById(R.id.checkbox_item_users_select);
                Users user = (Users) adapter.getItem(position);
                if (checkBox.isChecked()) {
                    mListUserSelect.remove(user);
                    checkBox.setChecked(false);
                } else {
                    mListUserSelect.add(user);
                    checkBox.setChecked(true);
                }

            }
        });
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.fab_btn_add_cv: {
                setupDialog();
                break;
            }
            case R.id.TextInputLayout_deadline_cv:
            case R.id.edt_deadline_cv: {
                final Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        calendar.set(i, i1, i2);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        mEdtDeadline.setText(dateFormat.format(calendar.getTime()));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
                datePickerDialog.show();
                break;
            }
        }
    }


    private void setupDialog() {
        if (mDialogCreateCV == null) {
            mDialogCreateCV = DialogPlus.newDialog(TeamWorkingActivity.this)
                    .setContentHolder(new ViewHolder(R.layout.custom_dialog_create_work))
                    .setCancelable(true)
                    .setPadding(16, 16, 16, 16)
                    .setGravity(Gravity.CENTER)
                    .create();
            mDialogCreateCV.findViewById(R.id.TextInputLayout_deadline_cv).setOnClickListener(this);
            mEdtDeadline = (EditText) mDialogCreateCV.findViewById(R.id.edt_deadline_cv);
            mEdtDeadline.setOnClickListener(this);
            mEdtDescription = (EditText) mDialogCreateCV.findViewById(R.id.edt_description_cv);
            mEdtName = (EditText) mDialogCreateCV.findViewById(R.id.edt_nhap_ten_cv);
            mRVListUsers = (RecyclerView) mDialogCreateCV.findViewById(R.id.list_member_select);
            mRVListUsers.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

            mDialogCreateCV.findViewById(R.id.btn_cancel_cv).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDialogCreateCV.dismiss();
                }
            });

            mDialogCreateCV.findViewById(R.id.btn_save_cv).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name = mEdtName.getText().toString().trim(),
                            description = mEdtDescription.getText().toString().trim(),
                            deadline = mEdtDeadline.getText().toString().trim();

                    if (name.equals("") || description.equals("")) {
                        MDToast.makeText(TeamWorkingActivity.this, "Vui lòng nhập đủ thông tin",
                                MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                    } else {
                        Work work = new Work(name, description, false, deadline);
                        for (Users user : mListUserSelect) work.addMember(user.getId());

                        work.setDateCreated(DateFormat.getDateTimeInstance().format(new Date()));
                        mDatabaseReferenceGroup.child("works").push().setValue(work)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        MDToast.makeText(TeamWorkingActivity.this, "Successful!",
                                                MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        MDToast.makeText(TeamWorkingActivity.this, "Error: " + e.getMessage(),
                                                MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                                    }
                                });
                        mDialogCreateCV.dismiss();
                    }
                }
            });


            mRVListUsers.setAdapter(adapter);
        }
        mDialogCreateCV.show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        if (mWorkAdapter.getData().size() != 0) {
            mTextViewNotification.setVisibility(View.GONE);
        } else mTextViewNotification.setVisibility(View.VISIBLE);
        super.onResume();
    }

    @Override
    public void onItemClick(Work work, int position) {
        Log.d("HAHA", "onItemClick: "+position);
    }

    @Override
    public void onClickButtonDelete(Work work, int position) {
        Log.d("HAHA", "onClickButtonDelete: "+position);
    }
}
