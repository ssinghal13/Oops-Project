package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodapp.Model.CartItem;
import com.example.foodapp.Model.OrderInfoItem;
//import com.example.foodapp.Model.OtpItem;
import com.example.foodapp.Model.Rating;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.foodapp.user;

import java.util.HashMap;
import java.util.Random;

public class RiderViewDetailsActivity extends AppCompatActivity {

    private DatabaseReference pickRef;
    private DatabaseReference dropRef;
    private DatabaseReference userRef;
    private DatabaseReference otpRef;
    private DatabaseReference cartRef;
    private DatabaseReference ratRef;
    private String uid;

    private TextView pickUpLocation;
    private TextView dropLocation;
    private TextView deliveryTotal;
    private TextView distance;
    private TextView userRating;
    //    private TextView pickUp_distance;
    String rider_uid;
    String riderNumber;
    String riderName;
    String userNumber;
    String loc_pickUp;
    String loc_drop;
    String userName;
    Double deliveryAmount;
    String qty_small="Small : 0";
    String qty_medium="Medium Meal : 0";
    String qty_large="Large Meal : 0";


    private Button btn_accept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_view_details);

        uid=getIntent().getStringExtra("UID");
        rider_uid=getIntent().getStringExtra("RiderUID");
        pickRef= FirebaseDatabase.getInstance().getReference().child("PickUpAddress").child(uid);
        dropRef=FirebaseDatabase.getInstance().getReference().child("DeliveryAddress").child(uid);
        userRef=FirebaseDatabase.getInstance().getReference().child("user");
        cartRef= FirebaseDatabase.getInstance().getReference().child("Cart").child(uid).child("Products");


        pickUpLocation=findViewById(R.id.pickUp);
        dropLocation=findViewById(R.id.drop);
        deliveryTotal=findViewById(R.id.deliveryAmount);
        distance=findViewById(R.id.distance);
        btn_accept=findViewById(R.id.btn_accept);
        userRating=findViewById(R.id.userRating);

        ActivityCompat.requestPermissions(RiderViewDetailsActivity.this,
                new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);

        pickRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                OrderInfoItem orderInfoItem=dataSnapshot.getValue(OrderInfoItem.class);
                loc_pickUp= orderInfoItem.getLocation().substring(0, 1).toUpperCase()+orderInfoItem.getLocation().substring(1);
                pickUpLocation.setText("Pick Up : "+loc_pickUp);
                deliveryAmount=orderInfoItem.getCartTotal();
                deliveryTotal.setText(String.format("Delivery Total : %.2f INR", orderInfoItem.getCartTotal()));
                distance.setText(String.format("Total Distance : %.2f Km",orderInfoItem.getDistance()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        userRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user u=dataSnapshot.getValue(user.class);
                userName=u.getFullname();
                userNumber=u.getPhonenumber();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        userRef.child(rider_uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user us=dataSnapshot.getValue(user.class);
                riderNumber= us.getPhonenumber();
                riderName=us.getFullname();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        dropRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                OrderInfoItem orderInfoItem=dataSnapshot.getValue(OrderInfoItem.class);
                loc_drop=orderInfoItem.getLocation().substring(0,1).toUpperCase()+orderInfoItem.getLocation().substring(1);;
                dropLocation.setText("Drop : "+ loc_drop);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ratRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Rating rating=dataSnapshot.getValue(Rating.class);
                userRating.setText(String.format("Customer Rating : %.1f",rating.getRating()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snap:dataSnapshot.getChildren()){
                    CartItem cartItem=snap.getValue(CartItem.class);
                    assert cartItem != null;
                    if(cartItem.getName().equals("Small Meal")){
                        qty_small=String.format("Small Meal : %s", cartItem.getQuantity());
                    }
                    if(cartItem.getName().equals("Medium Meal")){
                        qty_medium=String.format("Medium Meal : %s", cartItem.getQuantity());
                    }
                    if(cartItem.getName().equals("Large Meal")){
                        qty_large=String.format("Large Meal : %s", cartItem.getQuantity());
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RiderViewDetailsActivity.this, "Order Accepted", Toast.LENGTH_SHORT).show();

//                int num = generator. nextInt(899999) + 100000;
                int i = new Random().nextInt(900000) + 100000;
                String orderID= rider_uid.substring(0,7);

                otpRef=FirebaseDatabase.getInstance().getReference().child("OtpStatus").child(uid);
                HashMap<String,Object> OTP=new HashMap<>();
                OTP.put("otp",String.valueOf(i));
                OTP.put("orderid",orderID);
                OTP.put("ridername",riderName);
                OTP.put("ridernumber",riderNumber);
                otpRef.updateChildren(OTP);

//                String message=String.format("Your Order id is %s. Deliver Person Details-> Name: %s, Mobile Number: %s"
//                        ,orderID,riderName, riderNumber);
                String number= userNumber;

                String message=String.format("Your OTP code is %s", String.valueOf(i));


                SmsManager mysmsManager= SmsManager.getDefault();
                mysmsManager.sendTextMessage(number,null,message,null,null);

                Intent intent=new Intent(RiderViewDetailsActivity.this, RiderOtpVerify.class);
                intent.putExtra("PickUp",loc_pickUp);
                intent.putExtra("Drop",loc_drop);
                intent.putExtra("Name", userName);
                intent.putExtra("Phone",userNumber);
                intent.putExtra("OrderID",orderID);
                intent.putExtra("UserID", uid);
                intent.putExtra("DeliveryAmount", deliveryAmount);
                intent.putExtra("Small",qty_small);
                intent.putExtra("Medium",qty_medium);
                intent.putExtra("Large",qty_large);
                startActivity(intent);
                finish();

            }
        });


    }
}
