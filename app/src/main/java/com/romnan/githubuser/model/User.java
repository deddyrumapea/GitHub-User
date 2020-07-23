package com.romnan.githubuser.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
    private int id;
    private String avatar;
    private String name;
    private String username;
    private String location;
    private String company;
    private int repositories;
    private int followers;
    private int following;

    public User(int id, String avatar, String name, String username, String location, String company,
                int repositories, int followers, int following) {
        this.id = id;
        this.avatar = avatar;
        this.name = name;
        this.username = username;
        this.location = location;
        this.company = company;
        this.repositories = repositories;
        this.followers = followers;
        this.following = following;
    }

    protected User(Parcel in) {
        id = in.readInt();
        avatar = in.readString();
        name = in.readString();
        username = in.readString();
        location = in.readString();
        company = in.readString();
        repositories = in.readInt();
        followers = in.readInt();
        following = in.readInt();
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public int getRepositories() {
        return repositories;
    }

    public void setRepositories(int repositories) {
        this.repositories = repositories;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(avatar);
        dest.writeString(name);
        dest.writeString(username);
        dest.writeString(location);
        dest.writeString(company);
        dest.writeInt(repositories);
        dest.writeInt(followers);
        dest.writeInt(following);
    }
}
