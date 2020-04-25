package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodapp.Model.CartItem;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrderDetailsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextView txt_name,txt_mob, txt_deliveryAmount, txt_small, txt_medium, txt_large, txt_pickUp,txt_drop, txt_orderID;
    private DatabaseReference cartRef;
    private DatabaseReference otpRef;
    private DatabaseReference pickRef;
    private DatabaseReference dropRef;
    String uid;
    Double deliveryAmount;
    String qty_small,qty_medium,qty_large;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        qty_small=getIntent().getStringExtra("Small");
        qty_medium=getIntent().getStringExtra("Medium");
        qty_large=getIntent().getStringExtra("Large");

        toolbar = findViewById(R.id.main_toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);



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
        txt_small.setText(qty_small);
        txt_medium.setText(qty_medium);
        txt_large.setText(qty_large);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.openNavDrawer,
                R.string.closeNavDrawer
        );

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_home);

//        otpRef=FirebaseDatabase.getInstance().getReference().child("OtpStatus").child(uid);
//        pickRef= FirebaseDatabase.getInstance().getReference().child("PickUpAddress").child(uid);
//        dropRef=FirebaseDatabase.getInstance().getReference().child("DeliveryAddress").child(uid);
//        cartRef= FirebaseDatabase.getInstance().getReference().child("Cart").child(uid);
//        pickRef.removeValue();
//        dropRef.removeValue();
//        cartRef.removeValue();
//        otpRef.removeValue();

//        cartRef= FirebaseDatabase.getInstance().getReference().child("Cart").child(uid).child("Products");
//
//        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot snap:dataSnapshot.getChildren()){
//                    CartItem cartItem=snap.getValue(CartItem.class);
//                    if(cartItem.getName().equals("Small Meal")){
//                        txt_small.setText(String.format("Small Meal : %s", cartItem.getQuantity()));
//                    }
//                    if(cartItem.getName().equals("Medium Meal")){
//                        txt_medium.setText(String.format("Medium Meal : %s", cartItem.getQuantity()));
//                    }
//                    if(cartItem.getName().equals("Large Meal")){
//                        txt_large.setText(String.format("Large Meal : %s", cartItem.getQuantity()));
//                    }
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(OrderDetailsActivity.this, "Issue in Order", Toast.LENGTH_SHORT).show();
//                Toast.makeText(OrderDetailsActivity.this, "Please Contact Customer", Toast.LENGTH_SHORT).show();
//            }
//        });






    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_home:
                Toast.makeText(this, "Not Allowed, Please SignOut", Toast.LENGTH_SHORT).show();
                break;

            case R.id.profile_nav:
                Toast.makeText(this, "Not Allowed, Please SignOut ", Toast.LENGTH_SHORT).show();
                break;
            case R.id.log_out:
                FirebaseAuth.getInstance().signOut();
//                otpRef=FirebaseDatabase.getInstance().getReference().child("OtpStatus").child(uid);
//                pickRef= FirebaseDatabase.getInstance().getReference().child("PickUpAddress").child(uid);
//                dropRef=FirebaseDatabase.getInstance().getReference().child("DeliveryAddress").child(uid);
////                cartRef= FirebaseDatabase.getInstance().getReference().child("Cart").child(uid);
////                cartRef.removeValue();
//                pickRef.removeValue();
//                dropRef.removeValue();
//                otpRef.removeValue();
//                mGoogleSignInClient.signOut();
                Intent intent=new Intent(OrderDetailsActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
}
