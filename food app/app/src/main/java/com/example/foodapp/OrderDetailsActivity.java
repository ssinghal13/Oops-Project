package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodapp.Model.CartItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrderDetailsActivity extends AppCompatActivity {

    private TextView txt_name,txt_mob, txt_deliveryAmount, txt_small, txt_medium, txt_large, txt_pickUp,txt_drop, txt_orderID;
    private DatabaseReference cartRef;
    String uid;
    Double deliveryAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);



        txt_name=findViewById(R.id.txt_name);
        txt_mob=findViewById(R.id.txt_mobile);
        txt_small = findViewById(R.id.qty_small);
        txt_medium=findViewById(R.id.qty_medium);
        txt_large=findViewById(R.id.qty_large);
        txt_pickUp=findViewById(R.id.txt_pickUp);
        txt_drop=findViewById(R.id.txt_drop);
        txt_orderID=findViewById(R.id.txt_orderID);
        txt_deliveryAmount=findViewById(R.id.deliveryAmount);

        txt_name.setText(String.format("Name : %s", getIntent().getStringExtra("Name")));
        txt_mob.setText(String.format("Phone Number : %s", getIntent().getStringExtra("Phone")));
        txt_pickUp.setText(String.format("Pick Up : %s", getIntent().getStringExtra("PickUp")));
        txt_drop.setText(String.format("Drop : %s", getIntent().getStringExtra("Drop")));
        txt_orderID.setText(String.format("Order ID : %s", getIntent().getStringExtra("OrderID")));
        txt_deliveryAmount.setText(String.format("DeliveryAmount : %.2f", getIntent().getDoubleExtra("DeliveryAmount",0.0)));
        uid=getIntent().getStringExtra("UserID");

        cartRef= FirebaseDatabase.getInstance().getReference().child("Cart").child(uid).child("Products");

        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snap:dataSnapshot.getChildren()){
                    CartItem cartItem=snap.getValue(CartItem.class);
                    if(cartItem.getName().equals("Small Meal")){
                        txt_small.setText(String.format("Small Meal : %s", cartItem.getQuantity()));
                    }
                    if(cartItem.getName().equals("Medium Meal")){
                        txt_medium.setText(String.format("Medium Meal : %s", cartItem.getQuantity()));
                    }
                    if(cartItem.getName().equals("Large Meal")){
                        txt_large.setText(String.format("Large Meal : %s", cartItem.getQuantity()));
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(OrderDetailsActivity.this, "Issue in Order", Toast.LENGTH_SHORT).show();
                Toast.makeText(OrderDetailsActivity.this, "Please Contact Customer", Toast.LENGTH_SHORT).show();
            }
        });






    }
}
