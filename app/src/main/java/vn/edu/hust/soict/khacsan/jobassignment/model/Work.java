package vn.edu.hust.soict.khacsan.jobassignment.model;

import java.util.Date;
import java.util.List;

/**
 * Created by San on 03/27/2018.
 */

public class Work {
    String name,description;
    boolean status;
    List<String> member;
    Date deadline;
    //List<Comments> comments;

    public Work() {
    }


    public Work(String name, String description, boolean status, List<String> member, Date deadline) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.member = member;
        this.deadline = deadline;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<String> getMember() {
        return member;
    }

    public void setMember(List<String> member) {
        this.member = member;
    }
}
