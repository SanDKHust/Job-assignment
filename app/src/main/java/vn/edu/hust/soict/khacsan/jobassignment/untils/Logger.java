package vn.edu.hust.soict.khacsan.jobassignment.untils;

import android.util.Log;

import vn.edu.hust.soict.khacsan.jobassignment.BuildConfig;


/**
 * Created by Mr Ha on 10/17/16.
 *
 * @author Mr. Ha
 */
public class Logger {

    public static void Log(String msg) {
        if (BuildConfig.DEBUG) {
            Log.i("SAN : ", "------ : " + msg);
        }
    }
}
