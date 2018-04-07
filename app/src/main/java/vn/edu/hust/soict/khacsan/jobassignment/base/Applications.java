package vn.edu.hust.soict.khacsan.jobassignment.base;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.hawk.Hawk;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import vn.edu.hust.soict.khacsan.jobassignment.untils.Logger;


/**
 * Created by San on 03/05/2018.
 */

public class Applications extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Hawk.init(getApplicationContext()).build();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this,Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(true);
        Picasso.setSingletonInstance(built);
    }
    public void handleUncaughtException(Thread thread, Throwable e) {
        e.printStackTrace();
        Logger.Log("-----------------handleUncaughtException--------------------- " + e.getMessage());
    }
}
