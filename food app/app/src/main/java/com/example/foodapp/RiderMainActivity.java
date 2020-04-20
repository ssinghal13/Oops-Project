package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.foodapp.Model.OrderInfoItem;
import com.example.foodapp.ViewHolder.OrderItemsAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RiderMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private OrderItemsAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private DatabaseReference orderref;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private double longi;
    private double lat;
    private String user_ID;

    public double distance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_main);

        toolbar = findViewById(R.id.main_toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        lat=getIntent().getDoubleExtra("Latitude", 0.0);
        longi=getIntent().getDoubleExtra("Longitude", 0.0);
        user_ID=getIntent().getStringExtra("UID");

        firebaseAuth = FirebaseAuth.getInstance();
        orderref = FirebaseDatabase.getInstance().getReference("DeliveryAddress");

        setUpRecyclerView();
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }


    private void setUpRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);



        FirebaseRecyclerOptions<OrderInfoItem> options = new FirebaseRecyclerOptions.Builder<OrderInfoItem>()
                .setQuery(orderref,OrderInfoItem.class)
                .build();
        adapter = new OrderItemsAdapter(options);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OrderItemsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(double latitude, double longitude,  String mobNumber, String user_ID, double cartTotal)
            {
                final int R = 6371; // Radius of the earth

                double latDistance = Math.toRadians(latitude - lat);
                double lonDistance = Math.toRadians(longitude - longi);
                double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                        + Math.cos(Math.toRadians(lat)) * Math.cos(Math.toRadians(latitude))
                        * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
                double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                double dist = R * c;

                dist = Math.pow(distance, 2);
                distance = Math.sqrt(dist);



//                Log.d("Coordinates = ",Double.toString(latitude)+","+Double.toString(longitude));
//                Log.d("Rider = ", Double.toString(lat)+","+Double.toString(longi));
                Log.d("Distance = ", Double.toString(distance));

                Intent intent = new Intent(RiderMainActivity.this,Main3Activity.class);

//                intent.putExtra("RiderLat",Double.toString(lati));
//                intent.putExtra("RiderLong",Double.toString(pos.longitude));
//
//                intent.putExtra("OrderLat",Double.toString(latitude));
//                intent.putExtra("OrderLong",Double.toString(longitude));
//
//                intent.putExtra("Distance",Double.toString(distance));
//
//                intent.putExtra("Phone_Number", phone);
//                intent.putExtra("Customer_UID", cust_uid);
//                intent.putExtra("Status", status);
//                intent.putExtra("Amount",amount);

                startActivity(intent);

            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home:
                break;
            case R.id.profile_nav:
                Intent intent = new Intent(RiderMainActivity.this,  ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.log_out:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(RiderMainActivity.this, MainActivity.class));
                finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onBackPressed() {

        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }
}