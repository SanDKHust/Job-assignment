package vn.edu.hust.soict.khacsan.jobassignment.ui.chat;

import android.support.annotation.Nullable;
import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import vn.edu.hust.soict.khacsan.jobassignment.R;
import vn.edu.hust.soict.khacsan.jobassignment.model.Users;

public class UsersAdapterSelect extends BaseQuickAdapter<Users,BaseViewHolder> {


    public UsersAdapterSelect(int layoutResId, @Nullable List<Users> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Users item) {
        helper.setText(R.id.text_item_displayName_select,item.getName())
                .setText(R.id.text_item_status_select,item.getStatus());
        helper.getView(R.id.checkbox_item_users_select).setSelected(false);
        if(!item.getThumb_image().equals("default")){
            Picasso.with(mContext).load(item.getThumb_image())
                    .placeholder(R.drawable.default_avata)
                    .error(R.drawable.ic_error)
                    .into((CircleImageView) helper.getView(R.id.profile_item_image_select));
        }
    }
}
