package vn.edu.hust.soict.khacsan.jobassignment.model;

import java.util.ArrayList;

/**
 * Created by San on 03/20/2018.
 */

public class Users {
    private String thumb_image,name,status,email,id;
    private ArrayList<String> groups;

    public Users() {
    }

    public Users(String thumb_image, String name, String status, String email, String id, ArrayList<String> groups) {
        this.thumb_image = thumb_image;
        this.name = name;
        this.status = status;
        this.email = email;
        this.id = id;
        this.groups = groups;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<String> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<String> groups) {
        this.groups = groups;
    }
}
