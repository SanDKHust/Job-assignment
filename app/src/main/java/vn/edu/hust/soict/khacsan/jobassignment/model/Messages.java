package vn.edu.hust.soict.khacsan.jobassignment.model;

/**
 * Created by San on 03/13/2018.
 */

public class Messages {
    private String massage, type;
    private long time;
    private boolean seen;

    public Messages(String massage, String type, long time, boolean seen) {
        this.massage = massage;
        this.type = type;
        this.time = time;
        this.seen = seen;
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
}
