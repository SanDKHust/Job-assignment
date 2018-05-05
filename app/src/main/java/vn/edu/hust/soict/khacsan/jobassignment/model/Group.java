package vn.edu.hust.soict.khacsan.jobassignment.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

/**
 * Created by San on 03/13/2018.
 */
@IgnoreExtraProperties
public class Group implements Parcelable {
    private String id, name,admin,date;
    private ArrayList<String> members = new ArrayList<>();

    public Group(){
    }

    public Group(String name, String admin, String date) {
        this.name = name;
        this.admin = admin;
        this.date = date;
    }

    protected Group(Parcel in) {
        id = in.readString();
        name = in.readString();
        admin = in.readString();
        date = in.readString();
        members = in.createStringArrayList();
    }

    public static final Creator<Group> CREATOR = new Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(admin);
        parcel.writeString(date);
        parcel.writeStringList(members);
    }
}