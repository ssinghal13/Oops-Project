package com.example.foodapp.Model;

public class OrderItem
{
    String Phone_Number;
    double Latitude, Longitude;
    String cartTotal;

    public OrderItem()
    {

    }
    public OrderItem(String Phone_Number, double Latitude, double Longitude, String cartTotal)
    {
        this.Phone_Number = Phone_Number;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.cartTotal = cartTotal;
    }

    public String getPhone_Number() {
        return Phone_Number;
    }

    public void setPhone_Number(String phone_number) {
        Phone_Number = phone_number;
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

    public String getCartTotal() {
        return cartTotal;
    }

    public void setCartTotal(String cartTotal) {
        this.cartTotal = cartTotal;
    }

}
