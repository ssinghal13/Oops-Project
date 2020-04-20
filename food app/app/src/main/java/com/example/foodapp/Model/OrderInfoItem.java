package com.example.foodapp.Model;

public class OrderInfoItem {

        String Phone_Number;
        double Latitude, Longitude;
        String User_ID;
        double CartTotal;

    public OrderInfoItem() {
    }

    public String getPhone_Number() {
        return Phone_Number;
    }

    public void setPhone_Number(String phone_Number) {
        Phone_Number = phone_Number;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public String getUser_ID() {
        return User_ID;
    }

    public void setUser_ID(String user_ID) {
        User_ID = user_ID;
    }

    public double getCartTotal() {
        return CartTotal;
    }

    public void setCartTotal(double cartTotal) {
        this.CartTotal = cartTotal;
    }
}
