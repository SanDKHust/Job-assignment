package vn.edu.hust.soict.khacsan.jobassignment.ui.signup;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;

import com.google.firebase.database.FirebaseDatabase;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.HashMap;

import vn.edu.hust.soict.khacsan.jobassignment.R;
import vn.edu.hust.soict.khacsan.jobassignment.base.BaseActivity;
import vn.edu.hust.soict.khacsan.jobassignment.ui.main.MainActivity;
import vn.edu.hust.soict.khacsan.jobassignment.untils.Logger;

public class RegisterActivity extends BaseActivity {

    private EditText mEdtName, mEdtEmail, mEdtPass, mEdtPassRepeat;
    private ImageButton mBtnShowPass, mBtnShowPassRepeat;
    private FloatingActionButton mFbBtnClose;
    private Button mBtnGo;
    private FirebaseAuth mAuth;
    private boolean isShowPass = false, isShowPassRepeat = false;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();

        mAuth = FirebaseAuth.getInstance();

        mBtnGo.setOnClickListener(btnGoClick);
        mFbBtnClose.setOnClickListener(btnCloseClick);
        mBtnShowPass.setOnClickListener(btnShowPassClick);
        mBtnShowPassRepeat.setOnClickListener(btnShowPassRepeatClick);
    }

    private View.OnClickListener btnShowPassClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(!isShowPass){
                mEdtPass.setTransformationMethod(null);
                mBtnShowPass.setImageResource(R.drawable.ic_invisibility);
                isShowPass = true;
            }else {
                isShowPass = false;
                mEdtPass.setTransformationMethod(new PasswordTransformationMethod());
                mBtnShowPass.setImageResource(R.drawable.ic_visibility);
            }
        }
    };

    private View.OnClickListener btnShowPassRepeatClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(!isShowPassRepeat){
                mEdtPassRepeat.setTransformationMethod(null);
                mBtnShowPassRepeat.setImageResource(R.drawable.ic_invisibility);
                isShowPassRepeat = true;
            }else {
                isShowPassRepeat = false;
                mEdtPassRepeat.setTransformationMethod(new PasswordTransformationMethod());
                mBtnShowPassRepeat.setImageResource(R.drawable.ic_visibility);
            }
        }
    };
    private View.OnClickListener btnCloseClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

    private View.OnClickListener btnGoClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(isShowPassRepeat){
                isShowPassRepeat = false;
                mEdtPassRepeat.setTransformationMethod(new PasswordTransformationMethod());
                mBtnShowPassRepeat.setImageResource(R.drawable.ic_visibility);
            }
            if(isShowPass){
                isShowPass = false;
                mEdtPass.setTransformationMethod(new PasswordTransformationMethod());
                mBtnShowPass.setImageResource(R.drawable.ic_visibility);
            }
            String email = mEdtEmail.getText().toString().trim();
            String name = mEdtName.getText().toString().trim();
            String pass = mEdtPass.getText().toString().trim();
            String passRepeat = mEdtPassRepeat.getText().toString().trim();
            if(email.isEmpty() || name.isEmpty() || pass.isEmpty() || passRepeat.isEmpty()){
                MDToast.makeText(RegisterActivity.this, "Vui lòng nhập đủ thông tin!",
                        MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
            }else {
                if(pass.equals(passRepeat)){
                    if (pass.length() >= 6) {
                        showDialog();
                        registerUser(email, name, pass);
                    }else {
                        MDToast.makeText(RegisterActivity.this, "Lỗi mật khẩu phải có ít nhất 6 kí tự!",
                                MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                    }
                }else {
                    MDToast.makeText(RegisterActivity.this, "Lỗi mật khẩu không khớp nhau!",
                            MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                }
            }
        }
    };

    private void registerUser(final String email, final String name, final String pass) {
        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = current_user.getUid();

                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(name).build();
                    current_user.updateProfile(profileUpdates);

                    mDatabase = FirebaseDatabase.getInstance();
                    mDatabaseReference = mDatabase.getReference().child("users").child(uid);

                    HashMap<String,String> map = new HashMap<>();
                    //map.put("name",name);
                    map.put("status","Hi there I'm using app");
                    map.put("image","default");
                    map.put("thumb_image","default");

                    mDatabaseReference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            hideDialog();
                            if(task.isSuccessful()){
                                Intent intentMain = new Intent(RegisterActivity.this, MainActivity.class);
                                intentMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                                MDToast.makeText(RegisterActivity.this, "Successful!",
                                        MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                startActivity(intentMain);
                                finish();
                            }else {
                                FirebaseAuthException e = (FirebaseAuthException)task.getException();
                                Logger.Log(e.getErrorCode() + ": " +e.getMessage());
                                MDToast.makeText(RegisterActivity.this, "Error: "+e.getMessage(),
                                        MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                            }
                        }
                    });

                }else {
                    hideDialog();
                    FirebaseAuthException e = (FirebaseAuthException)task.getException();
                    Logger.Log(e.getErrorCode() + ": " +e.getMessage());
                    MDToast.makeText(RegisterActivity.this, "Error: "+e.getMessage(),
                            MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                }
            }
        });
    }

    private void initViews() {
        mEdtEmail = findViewById(R.id.et_email);
        mEdtName = findViewById(R.id.et_username);
        mEdtPass = findViewById(R.id.et_password);
        mEdtPassRepeat = findViewById(R.id.et_repeatpassword);

        mBtnShowPass = findViewById(R.id.btn_visibility_pass);
        mBtnShowPassRepeat = findViewById(R.id.btn_visibility_repeat_pass);

        mFbBtnClose = findViewById(R.id.fab_close_register);
        mBtnGo = findViewById(R.id.bt_register);
    }
}
