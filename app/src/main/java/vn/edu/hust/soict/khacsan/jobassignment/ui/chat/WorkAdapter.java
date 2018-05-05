package vn.edu.hust.soict.khacsan.jobassignment.ui.chat;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.guanaj.easyswipemenulibrary.EasySwipeMenuLayout;

import java.util.List;

import vn.edu.hust.soict.khacsan.jobassignment.R;
import vn.edu.hust.soict.khacsan.jobassignment.model.Work;

public class WorkAdapter extends BaseQuickAdapter<Work,BaseViewHolder> {
    private catchEventItemWork listener;
    public WorkAdapter(int layoutResId, @Nullable List<Work> data,catchEventItemWork listener) {
        super(layoutResId, data);
        this.listener = listener;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final Work item) {
        helper.setText(R.id.item_name_work,item.getName())
                .setText(R.id.item_description_work,item.getDescription())
                .setText(R.id.item_deadline_work,item.getDeadline());
        helper.getView(R.id.menu_slide_delete_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClickButtonDelete(item,mData.indexOf(item));
                EasySwipeMenuLayout easySwipeMenuLayout = helper.getView(R.id.es);
                easySwipeMenuLayout.resetStatus();
            }
        });

        helper.getView(R.id.content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(item,mData.indexOf(item));
            }
        });
    }

    public interface catchEventItemWork{
        void onItemClick(Work work,int position);
        void onClickButtonDelete(Work work,int position);
    }
}
