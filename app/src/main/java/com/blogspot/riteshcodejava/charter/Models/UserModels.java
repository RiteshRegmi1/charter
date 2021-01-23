package com.blogspot.riteshcodejava.charter.Models;

public class UserModels{
    String uid, Name, PhoneNumber, ProfilePicture;

    public UserModels() {
    }

    public UserModels(String uid, String name, String phoneNumber, String profilePicture) {
        this.uid = uid;
        Name = name;
        PhoneNumber = phoneNumber;
        ProfilePicture = profilePicture;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getProfilePicture() {
        return ProfilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        ProfilePicture = profilePicture;
    }
}
