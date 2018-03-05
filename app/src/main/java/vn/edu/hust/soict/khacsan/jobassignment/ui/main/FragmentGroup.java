package vn.edu.hust.soict.khacsan.jobassignment.ui.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bvapp.arcmenulibrary.ArcMenu;
import com.bvapp.arcmenulibrary.widget.FloatingActionButton;

import vn.edu.hust.soict.khacsan.jobassignment.R;
import vn.edu.hust.soict.khacsan.jobassignment.ui.chat.ChatActivity;

/**
 * Created by San on 02/27/2018.
 */

public class FragmentGroup extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_group, container, false);
        SwipeRefreshLayout refresh = layout.findViewById(R.id.swipeRefreshLayout_group);
        refresh.setOnRefreshListener(this);
        ArcMenu menu = layout.findViewById(R.id.arcMenu);
        setupArcMenu(menu);
        return layout;
    }

    private void setupArcMenu(ArcMenu menu) {
        menu.showTooltip(true);
        menu.setToolTipBackColor(Color.WHITE);
        menu.setToolTipCorner(4f);
        menu.setToolTipPadding(8f);
        menu.setToolTipSide(ArcMenu.TOOLTIP_UP);
        menu.setToolTipTextColor(Color.BLACK);
        menu.setAnim(300, 300, ArcMenu.ANIM_MIDDLE_TO_RIGHT, ArcMenu.ANIM_MIDDLE_TO_RIGHT,
                ArcMenu.ANIM_INTERPOLATOR_ACCELERATE_DECLERATE, ArcMenu.ANIM_INTERPOLATOR_ACCELERATE_DECLERATE);

        FloatingActionButton floatButtonGroupAdd = new FloatingActionButton(getContext());  // Use internal FAB as child
        floatButtonGroupAdd.setSize(FloatingActionButton.SIZE_MINI); // set initial size for child, it will create fab first
        floatButtonGroupAdd.setIcon(R.drawable.ic_group_add); // It will set fab icon from your resources which related to 'ITEM_DRAWABLES'
        floatButtonGroupAdd.setBackgroundColor(getResources().getColor(R.color.colorPrimary)); // it will set fab child's color
        menu.setChildSize(floatButtonGroupAdd.getIntrinsicHeight()); // set absolout child size for menu
        menu.addItem(floatButtonGroupAdd, "Group add", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //You can access child click in here
            }
        });

        FloatingActionButton floatButtonSearch = new FloatingActionButton(getContext());  // Use internal FAB as child
        floatButtonSearch.setSize(FloatingActionButton.SIZE_MINI); // set initial size for child, it will create fab first
        floatButtonSearch.setIcon(R.drawable.ic_search); // It will set fab icon from your resources which related to 'ITEM_DRAWABLES'
        floatButtonSearch.setBackgroundColor(getResources().getColor(R.color.colorPrimary)); // it will set fab child's color
        menu.setChildSize(floatButtonSearch.getIntrinsicHeight()); // set absolout child size for menu
        menu.addItem(floatButtonSearch, "Search", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //You can access child click in here
                startActivity(new Intent(getContext(), ChatActivity.class));
            }
        });

    }

    @Override
    public void onRefresh() {

    }
}
