package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.foodapp.Model.OrderInfoItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RiderTryActivity extends AppCompatActivity {

    private DatabaseReference deliveryRef;
    double latitude, longitude, lat, longi;
    String mobNumber, user_ID;
    public double dist;
    public double distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_try);

        deliveryRef= FirebaseDatabase.getInstance().getReference("DeliveryAddress");
        lat=getIntent().getDoubleExtra("Latitude", 0.0);
        longi=getIntent().getDoubleExtra("Longitude", 0.0);
        user_ID=getIntent().getStringExtra("UID");



        deliveryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snap:dataSnapshot.getChildren()){


                    OrderInfoItem orderInfo= snap.getValue(OrderInfoItem.class);
                    assert orderInfo != null;
                    latitude = orderInfo.getLatitude();
                    longitude = orderInfo.getLongitude();
                    mobNumber = orderInfo.getPhone_Number();
                    user_ID = orderInfo.getUser_ID();
                    distance=Math.sqrt((latitude-lat)*(latitude-lat)+(longitude-longi)*(longitude-longi));

//                    final int R = 6371; // Radius of the earth
//
//                    double latDistance = Math.toRadians(latitude - lat);
//                    double lonDistance = Math.toRadians(longitude - longi);
//                    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
//                            + Math.cos(Math.toRadians(lat))* Math.cos(Math.toRadians(latitude))
//                            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
//                    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
//                    double dist = R * c;
//
//                    dist = Math.pow(dist, 2);
//                    distance = Math.sqrt(dist);

                    Log.d("Distance = ", Double.toString(distance));
//                    Log.d("Coordinates = ",Double.toString(latitude)+","+Double.toString(longitude));
                    Log.d("User_ID = ", user_ID);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
