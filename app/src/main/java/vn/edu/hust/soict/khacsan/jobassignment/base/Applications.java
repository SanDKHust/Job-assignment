package vn.edu.hust.soict.khacsan.jobassignment.base;

import android.app.Application;

import com.orhanobut.hawk.Hawk;

import vn.edu.hust.soict.khacsan.jobassignment.untils.Logger;


/**
 * Created by San on 03/05/2018.
 */

public class Applications extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Hawk.init(getApplicationContext()).build();
    }
    public void handleUncaughtException(Thread thread, Throwable e) {
        e.printStackTrace();
        Logger.Log("-----------------handleUncaughtException--------------------- " + e.getMessage());
    }
}
