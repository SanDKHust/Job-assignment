package vn.edu.hust.soict.khacsan.jobassignment.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

/**
 * Created by San on 02/04/2018.
 */

public class BaseActivity extends AppCompatActivity {
    private ACProgressFlower mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mDialog = new ACProgressFlower.Builder(this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text("Loading...")
                .fadeColor(Color.DKGRAY).build();
        super.onCreate(savedInstanceState);
    }



    protected void showDialog() {
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
    }

    protected void hideDialog() {
        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    protected boolean isShowDialog(){
        return mDialog.isShowing();
    }

}
