package vn.edu.hust.soict.khacsan.jobassignment.ui.main;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.List;

import vn.edu.hust.soict.khacsan.jobassignment.R;
import vn.edu.hust.soict.khacsan.jobassignment.model.Group;


/**
 * Created by San on 03/13/2018.
 */

public class ListGroupsAdapter extends BaseQuickAdapter<Group,BaseViewHolder> {

    public ListGroupsAdapter(int layoutResId, @Nullable List<Group> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Group item) {
        helper.setText(R.id.txt_group_Name,item.getName());
        helper.getView(R.id.btnMoreAction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupMenu popup = new PopupMenu(mContext, view);
                popup.getMenuInflater().inflate(R.menu.popup_menu_item_group, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.popup_action_edit:{
                                break;
                            }
                            case R.id.popup_action_leave:{
                                break;
                            }
                            case R.id.popup_action_info:{
                                break;
                            }
                            case R.id.popup_action_delete:{
                                break;
                            }
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });
    }

}