package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

    private Button btn_accept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_view_details);

        uid=getIntent().getStringExtra("UID");
        pickRef= FirebaseDatabase.getInstance().getReference().child("PickUpAddress").child(uid);
        dropRef=FirebaseDatabase.getInstance().getReference().child("DeliveryAddress").child(uid);
        userRef=FirebaseDatabase.getInstance().getReference().child("user").child(uid);
        otpRef=FirebaseDatabase.getInstance().getReference().child("OtpStatus").child(uid);

        pickUpLocation=findViewById(R.id.pickUp);
        dropLocation=findViewById(R.id.drop);
        deliveryTotal=findViewById(R.id.deliveryAmount);
        distance=findViewById(R.id.distance);
        btn_accept=findViewById(R.id.btn_accept);

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
                otpRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        OtpItem u= dataSnapshot.getValue(OtpItem.class);
                        HashMap<String, Object> otp=new HashMap<>();
                        otp.put("OTP", "YES");
                        otpRef.updateChildren(otp);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });


    }
}
