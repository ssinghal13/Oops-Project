package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.example.foodapp.Model.Rating;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RatingActivity extends AppCompatActivity {

    private EditText txt_rating;
    private DatabaseReference ratRef;
    double newValue;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        txt_rating=findViewById(R.id.txt_rating);
        newValue=Double.parseDouble(String.valueOf(txt_rating.getText()));
        uid=getIntent().getStringExtra("userID");

        ratRef= FirebaseDatabase.getInstance().getReference().child("Rating").child(uid);
        
        ratRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Rating ratItem= dataSnapshot.getValue(Rating.class);
                Double value=ratItem.getRating();
                Double order=ratItem.getOrders();
                Double finalRating=(value*order+newValue)/(order+1);
                HashMap<String,Object> rat=new HashMap<>();
                rat.put("rating",finalRating);
                rat.put("orders", order+1);
                ratRef.setValue(rat);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        


    }
}
