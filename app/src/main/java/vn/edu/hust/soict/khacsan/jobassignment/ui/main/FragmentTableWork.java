package vn.edu.hust.soict.khacsan.jobassignment.ui.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vn.edu.hust.soict.khacsan.jobassignment.R;

public class FragmentTableWork extends Fragment{

    public FragmentTableWork() {
        // Required empty public constructor
    }

    public static FragmentTableWork newInstance(String param1, String param2) {
        FragmentTableWork fragment = new FragmentTableWork();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_table_work, container, false);
        return layout;
    }
}
