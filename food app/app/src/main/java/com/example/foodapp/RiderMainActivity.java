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
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.foodapp.Model.OrderItem;
import com.example.foodapp.ViewHolder.OrderItemsAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

    private double longitude;
    private double latitude;
    private String user_ID;

    public double distance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_main);

        toolbar = findViewById(R.id.main_toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        latitude=getIntent().getDoubleExtra("Latitude", 0.0);
        longitude=getIntent().getDoubleExtra("Longitude", 0.0);
        user_ID=getIntent().getStringExtra("UID");

        firebaseAuth = FirebaseAuth.getInstance();
        orderref = FirebaseDatabase.getInstance().getReference("Orders").child(firebaseAuth.getCurrentUser().getUid().toString());

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



        FirebaseRecyclerOptions<OrderItem> options = new FirebaseRecyclerOptions.Builder<OrderItem>()
                .setQuery(orderref,OrderItem.class)
                .build();
        adapter = new OrderItemsAdapter(options);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OrderItemsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(double latitude, double longitude)
            {
                final int R = 6371; // Radius of the earth

                double latDistance = Math.toRadians(latitude - latitude);
                double lonDistance = Math.toRadians(longitude - longitude);
                double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                        + Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(latitude))
                        * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
                double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                double dist = R * c;

                dist = Math.pow(distance, 2);
                distance = Math.sqrt(dist);

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