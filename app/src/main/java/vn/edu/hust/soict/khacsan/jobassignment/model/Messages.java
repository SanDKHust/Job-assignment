package vn.edu.hust.soict.khacsan.jobassignment.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by San on 03/13/2018.
 */

public class Messages implements MultiItemEntity{
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    private int itemType;

    private String massage, type,idUserSend;
    private long time;
    private boolean seen;

    public Messages() {
    }

    public Messages( String massage, String idUserSend, String type, long time, boolean seen) {
        this.massage = massage;
        this.type = type;
        this.time = time;
        this.seen = seen;
        this.idUserSend = idUserSend;
    }

    public String getIdUserSend() {
        return idUserSend;
    }

    public void setIdUserSend(String idUserSend) {
        this.idUserSend = idUserSend;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
