package vn.edu.hust.soict.khacsan.jobassignment.ui.chat;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import vn.edu.hust.soict.khacsan.jobassignment.R;
import vn.edu.hust.soict.khacsan.jobassignment.model.Users;
import vn.edu.hust.soict.khacsan.jobassignment.ui.main.UsersAdapter;

import static vn.edu.hust.soict.khacsan.jobassignment.untils.Constant.GROUPID;

public class TeamWorkingActivity extends AppCompatActivity implements View.OnClickListener{

    private RecyclerView mRcVListCVNhom,mRVListUsers;
    private FloatingActionButton mFabAddCVNhom;
    private DialogPlus mDialogCreateCV = null;
    private EditText mEdtDeadline,mEdtName,mEdtDescription;
    private List<Users> mListUsers;
    private List<Users> mListPositionSelect;
    private UsersAdapterSelect adapter;
    private DatabaseReference mDRListMember,mDatabaseReferenceUsers;
    private String mGroupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_working);
        Intent intent = getIntent();
        if(intent == null) return;
        mGroupId = intent.getStringExtra(GROUPID);
        mDRListMember = FirebaseDatabase.getInstance().getReference().child("groups").child(mGroupId).child("members");

        mDatabaseReferenceUsers = FirebaseDatabase.getInstance().getReference().child("users");
        mRcVListCVNhom = findViewById(R.id.list_cv_nhom);
        mFabAddCVNhom = findViewById(R.id.fab_btn_add_cv);
        mFabAddCVNhom.setOnClickListener(this);

        setupListMembers();
    }

    private void setupListMembers() {
        mListUsers = new ArrayList<>();
        mListPositionSelect = new ArrayList<>();

        adapter = new UsersAdapterSelect(R.layout.item_users_select,mListUsers);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CheckBox checkBox = view.findViewById(R.id.checkbox_item_users_select);
                Users user = (Users) adapter.getItem(position);

                if(checkBox.isChecked()){
                    mListPositionSelect.remove(user);
                    checkBox.setChecked(false);
                }else {
                    mListPositionSelect.add(user);
                    checkBox.setChecked(true);
                }

            }
        });

        mDRListMember.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String id = dataSnapshot.getValue(String.class);
                if(id != null){
                    mDatabaseReferenceUsers.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Users user = dataSnapshot.getValue(Users.class);
                            if(user != null){
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
            case R.id.edt_deadline_cv : {
                final Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        calendar.set(i,i1,i2);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
                        mEdtDeadline.setText(dateFormat.format(calendar.getTime()));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
                datePickerDialog.show();
                break;
            }
        }
    }


    private void setupDialog() {
        if(mDialogCreateCV == null){

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
                    String name = mEdtName.getText().toString(),description = mEdtDescription.getText().toString();


                    mDialogCreateCV.dismiss();
                }
            });

            mRVListUsers.setAdapter(adapter);
        }
        mDialogCreateCV.show();

    }
}
