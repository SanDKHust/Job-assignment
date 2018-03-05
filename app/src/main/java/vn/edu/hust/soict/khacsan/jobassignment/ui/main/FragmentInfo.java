package vn.edu.hust.soict.khacsan.jobassignment.ui.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vn.edu.hust.soict.khacsan.jobassignment.R;

/**
 * Created by San on 02/27/2018.
 */

public class FragmentInfo extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_info, container, false);
        return layout;
    }
}
