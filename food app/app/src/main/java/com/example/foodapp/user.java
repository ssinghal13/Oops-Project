package com.example.foodapp;

public class user {
    public String fullname, username, phonenumber, email, role, customertype;

    public user(){

    }

    public user(String fullname, String username, String phonenumber, String email, String role, String customertype) {
        this.fullname = fullname;
        this.username = username;
        this.phonenumber = phonenumber;
        this.email = email;
        this.role = role;
        this.customertype = customertype;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCustomertype() {
        return customertype;
    }

    public void setCustomertype(String customertype) {
        this.customertype = customertype;
    }
}
