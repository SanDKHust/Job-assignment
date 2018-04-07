package vn.edu.hust.soict.khacsan.jobassignment.ui.search;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import vn.edu.hust.soict.khacsan.jobassignment.R;
import vn.edu.hust.soict.khacsan.jobassignment.model.Users;

/**
 * Created by San on 03/20/2018.
 */

public class UsersAdapter extends BaseQuickAdapter<Users,BaseViewHolder> {

    public UsersAdapter(int layoutResId, @Nullable List<Users> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Users item) {
        helper.setText(R.id.text_item_displayName,item.getName())
                .setText(R.id.text_item_status,item.getStatus());

        if(!item.getThumb_image().equals("default")){
                Picasso.with(mContext).load(item.getThumb_image())
                        .placeholder(R.drawable.default_avatar)
                        .error(R.drawable.ic_error)
                        .into((CircleImageView) helper.getView(R.id.profile_item_image));
        }
    }

}
