package com.example.foodapp.Model;

import android.view.LayoutInflater;

public class RiderInfoItem {
    double Latitude;
    double Longitude;
    String User_ID;
    double RiderRadius;

    public RiderInfoItem() {
    }

    public RiderInfoItem(double latitude, double longitude, String user_ID, double riderRadius){
        Latitude=latitude;
        Longitude=longitude;
        User_ID=user_ID;
        RiderRadius=riderRadius;
    }

    public double getRiderRadius() {
        return RiderRadius;
    }


    public void setRiderRadius(double riderRadius) {
        RiderRadius = riderRadius;
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
}
