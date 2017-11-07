package com.myscheduler.Login;
public class UserProfile {
    private String name, email, phone, userID, profilePicUrl, DOB, addr,inst;

    public UserProfile()
    {
        //for DataSnapShot()
    }

    public UserProfile(String name, String email, String phone, String userID, String profilePicUrl) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.userID = userID;
        this.profilePicUrl = profilePicUrl;
    }

    public UserProfile(String name, String email, String phone, String userID, String profilePicUrl, String DOB, String addr, String inst) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.userID = userID;
        this.profilePicUrl = profilePicUrl;
        this.DOB = DOB;
        this.addr = addr;
        this.inst = inst;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getInst() {
        return inst;
    }

    public void setInst(String inst) {
        this.inst = inst;
    }
}
/*

class UserProfile2 implements Serializable {

    String name;
    String email;
    String userID;


    UserProfile2(String name, String email)
    {
        this.name=name;
        this.email=email;
    }

    public UserProfile2(String name, String email, String userID) {
        this.name = name;
        this.email = email;
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUserID() {
        return userID;
    }

    @Override
    public String toString() {
        return "UserProfile2{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", userID='" + userID + '\'' +
                '}';
    }
}
*/