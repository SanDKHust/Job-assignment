package vn.edu.hust.soict.khacsan.jobassignment.ui.chat;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.guanaj.easyswipemenulibrary.EasySwipeMenuLayout;

import java.util.List;

import vn.edu.hust.soict.khacsan.jobassignment.R;
import vn.edu.hust.soict.khacsan.jobassignment.model.Work;

public class WorkAdapter extends BaseQuickAdapter<Work,BaseViewHolder> {
    private catchEventItemWork listener;
    private boolean isPerson;
    public WorkAdapter(int layoutResId, @Nullable List<Work> data,catchEventItemWork listener,boolean isPerson) {
        super(layoutResId, data);
        this.listener = listener;
        this.isPerson = isPerson;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final Work item) {
        helper.setText(R.id.item_name_work,item.getName())
                .setText(R.id.item_description_work,item.getDescription())
                .setText(R.id.item_deadline_work,item.getDeadline());

        View view = helper.getView(R.id.item_isCompleted_work);
        if(item.isStatus())
            view.setVisibility(View.VISIBLE);
        else view.setVisibility(View.GONE);

                ImageButton btnMenu = helper.getView(R.id.menu_slide_delete_right);
        if(isPerson)
           btnMenu.setImageResource(R.drawable.ic_check);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClickButtonDelete(item,mData.indexOf(item),isPerson);
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
        void onClickButtonDelete(Work work,int position,boolean isPerson);
    }
}
