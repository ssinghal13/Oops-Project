package com.example.foodapp;

public class user {
    public String fullName, Username, PhoneNumber, Email, Role, CustomerType;

    public user(){

    }

    public user(String fullName, String username, String phoneNumber, String email, String role, String customerType) {
        this.fullName = fullName;
        Username = username;
        PhoneNumber = phoneNumber;
        Email = email;
        Role = role;
        CustomerType = customerType;
    }
}
