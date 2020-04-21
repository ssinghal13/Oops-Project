package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

//import com.example.foodapp.Model.user;
import com.example.foodapp.Model.OtpItem;
import com.example.foodapp.ViewHolder.Otptry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class OrderSuccessful extends AppCompatActivity {

    private DatabaseReference otpRef;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_successful);

        uid = getIntent().getStringExtra("UID");
        otpRef= FirebaseDatabase.getInstance().getReference("OtpStatus").child(uid);


        otpRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                OtpItem otpItem= dataSnapshot.getValue(OtpItem.class);
                assert otpItem != null;
                if(otpItem.getOtp().equals("YES")){
//                    Toast.makeText(OrderSuccessful.this, "OTP Updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(OrderSuccessful.this, Otptry.class));
                        finish();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
