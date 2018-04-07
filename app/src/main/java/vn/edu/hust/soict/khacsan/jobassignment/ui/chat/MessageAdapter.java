package vn.edu.hust.soict.khacsan.jobassignment.ui.chat;

import android.graphics.Bitmap;
import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import vn.edu.hust.soict.khacsan.jobassignment.R;
import vn.edu.hust.soict.khacsan.jobassignment.model.Messages;

import static vn.edu.hust.soict.khacsan.jobassignment.untils.DataFormatUntils.getDate;

/**
 * Created by San on 03/13/2018.
 */

public class MessageAdapter extends BaseMultiItemQuickAdapter<Messages,BaseViewHolder> {
    private DatabaseReference mUserReference;
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public MessageAdapter(List<Messages> data) {
        super(data);
        addItemType(Messages.LEFT, R.layout.item_message_left);
        addItemType(Messages.RIGHT, R.layout.item_message_right);
        mUserReference = FirebaseDatabase.getInstance().getReference().child("users");
        ///mUserReference.keepSynced(true);
    }


    @Override
    protected void convert(final BaseViewHolder helper, final Messages item) {
        switch (helper.getItemViewType()) {
            case Messages.LEFT: {
                helper.setText(R.id.text_message_left,item.getMassage())
                        .setText(R.id.tm_message_left,getDate(item.getTime()));
                final CircleImageView imageView = helper.getView(R.id.image_message_left);
                mUserReference.child(item.getIdUserSend()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                       final String img = dataSnapshot.child("thumb_image").getValue(String.class);
                       final String name = dataSnapshot.child("name").getValue(String.class);
                       if(name!= null) helper.setText(R.id.name_message_user,name);
                       if(img != null && !img.trim().equals("default")){
                           Picasso.with(mContext).load(img)
                                   .networkPolicy(NetworkPolicy.OFFLINE)
                                   .placeholder(R.drawable.ic_account_circle)
                                   .error(R.drawable.ic_error)
                                   .into(imageView, new Callback() {
                                       @Override
                                       public void onSuccess() {
                                       }

                                       @Override
                                       public void onError() {
                                           Picasso.with(mContext).load(img)
                                                   .placeholder(R.drawable.ic_account_circle)
                                                   .error(R.drawable.ic_error)
                                                   .into(imageView);
                                       }
                                   });
                       }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                helper.getView(R.id.tm_message_left).setVisibility(View.GONE);
                break;
            }
            case Messages.RIGHT: {
                helper.setText(R.id.text_message_right,item.getMassage())
                        .setText(R.id.tm_message_right,getDate(item.getTime()));
                helper.getView(R.id.tm_message_right).setVisibility(View.GONE);
                break;
            }
        }
    }
}
