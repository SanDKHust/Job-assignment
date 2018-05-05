package vn.edu.hust.soict.khacsan.jobassignment.untils;

import android.util.Log;

import vn.edu.hust.soict.khacsan.jobassignment.BuildConfig;


public class Logger {

    public static void Log(String msg) {
        if (BuildConfig.DEBUG) {
            Log.i("SAN : ", "------ : " + msg);
        }
    }
}
