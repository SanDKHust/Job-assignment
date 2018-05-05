package vn.edu.hust.soict.khacsan.jobassignment.ui.chat;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.guanaj.easyswipemenulibrary.EasySwipeMenuLayout;

import java.util.List;

import vn.edu.hust.soict.khacsan.jobassignment.R;
import vn.edu.hust.soict.khacsan.jobassignment.model.Work;

public class WorkPersonAdapter extends BaseQuickAdapter<Work,BaseViewHolder> {

    public WorkPersonAdapter(int layoutResId, @Nullable List<Work> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final Work item) {
        helper.setText(R.id.item_name_work, item.getName())
                .setText(R.id.item_description_work, item.getDescription())
                .setText(R.id.item_deadline_work, item.getDeadline());
    }
}
