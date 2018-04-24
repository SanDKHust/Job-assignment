package vn.edu.hust.soict.khacsan.jobassignment.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by San on 03/27/2018.
 */

public class Work {
    String name,description,id;
    boolean status;
    List<String> members;
    String deadline,dateCreated;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Work() {
        members =new ArrayList<>();
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Work(String name, String description, boolean status, String deadline) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.deadline = deadline;
        members =new ArrayList<>();
    }

    public void addMember(String id){
        members.add(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
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

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }
}
