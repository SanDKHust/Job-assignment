package vn.edu.hust.soict.khacsan.jobassignment.ui.main;

import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import vn.edu.hust.soict.khacsan.jobassignment.R;
import vn.edu.hust.soict.khacsan.jobassignment.model.Group;


/**
 * Created by San on 03/13/2018.
 */

public class GroupsAdapter extends BaseQuickAdapter<Group,BaseViewHolder> {
    private ListenerActionPopupMenu listener;
    public GroupsAdapter(int layoutResId, @Nullable List<Group> data, ListenerActionPopupMenu listener) {
        super(layoutResId, data);
        this.listener = listener;
    }

    @Override
    protected void convert(BaseViewHolder helper, final Group item) {
        helper.setText(R.id.txt_group_Name,item.getName())
                .setText(R.id.icon_group,String.valueOf(item.getName().charAt(0)).toUpperCase());
        helper.getView(R.id.btnMoreAction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupMenu popup = new PopupMenu(mContext, view);
                popup.getMenuInflater().inflate(R.menu.popup_menu_item_group, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem itemMenu) {
                        switch (itemMenu.getItemId()){
                            case R.id.popup_action_edit:{
                                listener.onEdit(item);
                                break;
                            }
                            case R.id.popup_action_leave:{
                                listener.onLeave(item.getId());
                                break;
                            }
                            case R.id.popup_action_info:{
                                listener.onInfo(item);
                                break;
                            }
                            case R.id.popup_action_delete:{
                                listener.onDelete(item);
                                break;
                            }
                            case R.id.popup_action_add_member:{
                                listener.onAddMember(item.getId(),item.getMembers().size());
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

    public interface ListenerActionPopupMenu {
        void onEdit(Group group);
        void onAddMember(String idGroup,int size);
        void onDelete(Group group);
        void onInfo(Group group);
        void onLeave(String idGroup);
    }

    public interface ListenerActionPopupMenuSearch {
        void onRequestAdd(Group group);
        void onInfo(Group group);
    }

}