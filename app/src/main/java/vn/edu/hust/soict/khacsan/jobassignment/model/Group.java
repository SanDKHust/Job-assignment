package vn.edu.hust.soict.khacsan.jobassignment.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

/**
 * Created by San on 03/13/2018.
 */
@IgnoreExtraProperties
public class Group{
    private String id, name,admin,date;
    private ArrayList<String> members = new ArrayList<>();

    public Group(){
    }

    public Group(String name, String admin, String date) {
        this.name = name;
        this.admin = admin;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getMembers() {
        return members;
    }

    public void setMember(ArrayList<String> members) {
        this.members = members;
    }

    public void addMember(String member){
        members.add(member);
    }

}