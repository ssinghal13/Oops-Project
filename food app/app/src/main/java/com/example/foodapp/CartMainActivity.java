package com.example.foodapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.Model.CartItem;
import com.example.foodapp.ViewHolder.CartItemsAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class CartMainActivity extends AppCompatActivity {


    CartItemsAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MaterialButton mplaceOrder;
    private TextView mPriceTotal;
    private TextView mCartTotal;
    private TextView mDiscount;

    private DatabaseReference userref;
    private DatabaseReference mCartRef;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_main);

        firebaseAuth = FirebaseAuth.getInstance();
        userref = FirebaseDatabase.getInstance().getReference("user").child(firebaseAuth.getCurrentUser().getUid().toString());
        mCartRef = FirebaseDatabase.getInstance().getReference("Cart").child(firebaseAuth.getCurrentUser().getUid().toString());

        mplaceOrder = findViewById(R.id.btn_place_order);

        setUpTotals();
        setUpRecycler();





        mplaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(CartMainActivity.this,MapsActivity.class);
                intent.putExtra("UID",firebaseAuth.getCurrentUser().getUid());

                userref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        user u = dataSnapshot.getValue(user.class);
                        intent.putExtra("PhoneNumber", u.PhoneNumber);
                        startActivity(intent);
                        startActivity(new Intent(CartMainActivity.this, Otptry.class));
                        //Toast.makeText(CartMainActivity.this,"Phone Number : "+u.PhoneNumber,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



            }
        });
    }

    public void setUpTotals() {

        mPriceTotal = findViewById(R.id.total_price_text_view);
        mCartTotal = findViewById(R.id.cart_total_textView);
        mDiscount = findViewById(R.id.DdiscountTextView);

        //int cartTotal = 0;
        //double discount = 0;


        mCartRef.child("Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int cartTotal = 0;
                if(dataSnapshot.exists()){
                    for(DataSnapshot snap: dataSnapshot.getChildren()){
                        CartItem item = snap.getValue(CartItem.class);
                        cartTotal += (Integer.parseInt(item.getPrice()) * Integer.parseInt(item.getQuantity()));

                        final int finalCartTotal = cartTotal;
                        userref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                double discount = 0;
                                user u = dataSnapshot.getValue(user.class);
                                if(u.CustomerType.equals("Regular"))
                                    discount = 20;

                                mDiscount.setText("Regular Customer Discount : "+discount+"%");

                                double TotalPrice = finalCartTotal *(100-discount)/100;

                                mPriceTotal.setText("Total Price : "+TotalPrice);

                                HashMap<String,Object> cartInfo = new HashMap<>();
                                cartInfo.put("CartItemsTotal",finalCartTotal);
                                cartInfo.put("Discount",discount);
                                cartInfo.put("CartTotal",TotalPrice);

                                mCartRef.child("Info").setValue(cartInfo);


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                }

                mCartTotal.setText("CART TOTAL : "+cartTotal);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    private void setUpRecycler() {

        recyclerView = findViewById(R.id.cart_recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);




        FirebaseRecyclerOptions<CartItem> options = new FirebaseRecyclerOptions.Builder<CartItem>()
                .setQuery(mCartRef.child("Products"),CartItem.class)
                .build();

        adapter = new CartItemsAdapter(options);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new CartItemsAdapter.OnItemClickListener() {
            @Override
            public void onItemDelete(String PID, int position) {
                mCartRef.child("Products").child(PID).removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                    Toast.makeText(CartMainActivity.this,"ITEM REMOVED", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        adapter.setOnValueChangeListener(new CartItemsAdapter.OnValueChangeListener() {
            @Override
            public void onQuantityChange(String PID, int quantity) {

                String Qty = Integer.toString(quantity);
                mCartRef.child("Products").child(PID).child("Quantity").setValue(Qty)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                    Toast.makeText(CartMainActivity.this, "Quantity Updated", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(CartMainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }
}

