package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodapp.Model.OrderInfoItem;
import com.example.foodapp.Model.OtpItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.foodapp.user;

import java.util.HashMap;

public class RiderViewDetailsActivity extends AppCompatActivity {

    private DatabaseReference pickRef;
    private DatabaseReference dropRef;
    private DatabaseReference userRef;
    private DatabaseReference otpRef;
    private String uid;

    private TextView pickUpLocation;
    private TextView dropLocation;
    private TextView deliveryTotal;
    private TextView distance;
//    private TextView pickUp_distance;
    String rider_uid;
    String riderNumber;
    String riderName;
    String userNumber;

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
        otpRef=FirebaseDatabase.getInstance().getReference().child("OtpStatus").child(uid);

        pickUpLocation=findViewById(R.id.pickUp);
        dropLocation=findViewById(R.id.drop);
        deliveryTotal=findViewById(R.id.deliveryAmount);
        distance=findViewById(R.id.distance);
        btn_accept=findViewById(R.id.btn_accept);

        ActivityCompat.requestPermissions(RiderViewDetailsActivity.this,
                new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);

        pickRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                OrderInfoItem orderInfoItem=dataSnapshot.getValue(OrderInfoItem.class);
                String loc_pickUp= orderInfoItem.getLocation().substring(0, 1).toUpperCase()+orderInfoItem.getLocation().substring(1);
                pickUpLocation.setText("Pick Up : "+loc_pickUp);
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
                userNumber=u.getPhoneNumber();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//        userNumber="7056595598";

        userRef.child(rider_uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user us=dataSnapshot.getValue(user.class);
                riderNumber= us.getPhoneNumber();
                riderName=us.getFullName();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        dropRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                OrderInfoItem orderInfoItem=dataSnapshot.getValue(OrderInfoItem.class);
                String loc_drop=orderInfoItem.getLocation().substring(0,1).toUpperCase()+orderInfoItem.getLocation().substring(1);;
                dropLocation.setText("Drop : "+ loc_drop);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RiderViewDetailsActivity.this, "Order Accepted", Toast.LENGTH_SHORT).show();


                String number=userNumber;
                String orderID=rider_uid.substring(0,7);
//                String message=String.format("Your Order id is %s. Deliver Person Details-> Name: %s, Mobile Number: %s"
//                        ,orderID,riderName, riderNumber);
                String message= "Your Order id is %s. Deliver Person Details-> Name: ";

                SmsManager mysmsManager= SmsManager.getDefault();
                mysmsManager.sendTextMessage(number,null,message,null,null);
//                otpRef.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        OtpItem u= dataSnapshot.getValue(OtpItem.class);
//                        HashMap<String, Object> otp=new HashMap<>();
//                        otp.put("OTP", "YES");
//                        otpRef.setValue(otp);
//
////                        String number="9792911817";
////                        String message= "Your Order id is %s. Deliver Person Details-> Name: ";
//
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
            }
        });


    }
}
