package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RiderViewDetailsActivity extends AppCompatActivity {

    private DatabaseReference pickRef;
    private DatabaseReference dropRef;
    private DatabaseReference userRef;
    private String uid;

    private TextView pickUp;
    private TextView drop;
    private TextView cartTotal;
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

        pickRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
