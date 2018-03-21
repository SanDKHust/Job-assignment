package vn.edu.hust.soict.khacsan.jobassignment.ui.login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.hawk.Hawk;
import com.valdesekamdem.library.mdtoast.MDToast;

import vn.edu.hust.soict.khacsan.jobassignment.R;
import vn.edu.hust.soict.khacsan.jobassignment.base.BaseActivity;
import vn.edu.hust.soict.khacsan.jobassignment.ui.main.MainActivity;
import vn.edu.hust.soict.khacsan.jobassignment.ui.signup.RegisterActivity;

public class LoginActivity extends BaseActivity {

    private FloatingActionButton mButtonSignup;
    private Button mButtonLogin;
    private EditText mEdtEmail, mEdtPass;
    private CheckBox mCbRememberPass;
    private TextView mTvForgotPass;
    private FirebaseAuth mAuth;
    private boolean isRememberPass = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        if(Hawk.contains("isRememberPass")){
            isRememberPass = Hawk.get("isRememberPass");
            mCbRememberPass.setChecked(isRememberPass);
        }
        if(Hawk.contains("password")) mEdtPass.setText((String)Hawk.get("password"));
        if(Hawk.contains("email")) mEdtEmail.setText((String)Hawk.get("email"));

        mAuth = FirebaseAuth.getInstance();

        mButtonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEdtEmail.getText().toString().trim();
                String pass = mEdtPass.getText().toString().trim();
                if(email.isEmpty() || pass.isEmpty()){
                    MDToast.makeText(LoginActivity.this, "Vui lòng nhập đủ thông tin!",
                            MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                }else {
                    showDialog();
                    loginUser(email, pass);
                }
            }
        });
        mCbRememberPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) isRememberPass = b;
                else Hawk.deleteAll();
            }
        });
        mTvForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });
    }

    private void resetPassword() {

    }

    private void loginUser(final String email, final String pass) {
        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                hideDialog();
                if(task.isSuccessful()){
                    if(isRememberPass){
                        Hawk.put("isRememberPass",true);
                        Hawk.put("email",email);
                        Hawk.put("password",pass);
                    }
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }else {
                    MDToast.makeText(LoginActivity.this, "Lỗi đăng nhập, vui lòng kiểm tra lại email và mật khẩu!",
                            MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                }
            }
        });
    }

    private void initView() {
        mButtonLogin = findViewById(R.id.btn_login);
        mButtonSignup = findViewById(R.id.fab_button_signup);
        mEdtEmail = findViewById(R.id.edt_email_login);
        mEdtPass = findViewById(R.id.edt_password_login);
        mCbRememberPass = findViewById(R.id.checkbox_remember_pass);
        mTvForgotPass = findViewById(R.id.tv_fogot_pass);
    }

    @Override
    public void onBackPressed() {
        if(isShowDialog()){
            hideDialog();
            mAuth.signOut();
        }
        super.onBackPressed();
    }
}
