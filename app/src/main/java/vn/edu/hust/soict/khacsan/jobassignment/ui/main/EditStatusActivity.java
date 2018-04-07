package vn.edu.hust.soict.khacsan.jobassignment.ui.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TabWidget;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.valdesekamdem.library.mdtoast.MDToast;

import dmax.dialog.SpotsDialog;
import vn.edu.hust.soict.khacsan.jobassignment.R;
import vn.edu.hust.soict.khacsan.jobassignment.ui.signup.RegisterActivity;

import static vn.edu.hust.soict.khacsan.jobassignment.untils.Constant.STATUS;

public class EditStatusActivity extends AppCompatActivity {
    private EditText mEdtStatus;
    private TextView mTvLengthStatus;

    private DatabaseReference mDatabaseReference;
    private FirebaseUser mCurrentUser;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_status);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        setTitle("Update status");

        mEdtStatus = findViewById(R.id.edt_edit_status);
        mTvLengthStatus = findViewById(R.id.tv_length_status);


        Intent intent = getIntent();
        if(intent != null){
            String status = intent.getStringExtra(STATUS);
            mEdtStatus.setText(status);
        }

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = mCurrentUser.getUid();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(uid);

        dialog = new SpotsDialog(EditStatusActivity.this, R.style.CustomDialogUpdateStatus);

        mEdtStatus.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mTvLengthStatus.setText(charSequence.length()+"/256");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_status_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_post_menu){
            dialog.show();
            mDatabaseReference.child("status").setValue(mEdtStatus.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("result",(mEdtStatus.getText().toString()));
                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();
                    }else {
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_CANCELED, returnIntent);
                        finish();
                        MDToast.makeText(EditStatusActivity.this, "Error: "+task.getException().getMessage(),
                                MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                    }
                    dialog.dismiss();
                }
            });

        }else if(item.getItemId() == android.R.id.home){
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
        super.onBackPressed();
    }
}
