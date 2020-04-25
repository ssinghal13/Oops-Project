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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.foodapp.Model.Rating;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RatingActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private EditText txt_rating;
    private DatabaseReference ratRef;
    private Button btn_rating;
    double newValue;
    String uid;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        toolbar = findViewById(R.id.main_toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);



        txt_rating=findViewById(R.id.txt_rating);

        uid=getIntent().getStringExtra("userID");
        btn_rating=findViewById(R.id.btn_rating);


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


        ratRef= FirebaseDatabase.getInstance().getReference().child("Rating").child(uid);
        btn_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newValue=Double.parseDouble(String.valueOf(txt_rating.getText()));
                if(newValue>5 || newValue<0){
                    Toast.makeText(RatingActivity.this, "Please Enter Valid Rating", Toast.LENGTH_SHORT).show();
                }
                else {
                    ratRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Rating ratItem = dataSnapshot.getValue(Rating.class);
                            Double value = ratItem.getRating();
                            Double order = ratItem.getOrders();
                            Double finalRating = (value * order + newValue) / (order + 1);
                            HashMap<String, Object> rat = new HashMap<>();
                            rat.put("rating", finalRating);
                            rat.put("orders", order + 1);
                            ratRef.setValue(rat);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    Intent intent4 =new Intent(RatingActivity.this, RiderMainActivity.class);
                    intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent4);
                    finish();

                }
        }});

        }

        



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_home:
//                Toast.makeText(this, "Not Allowed, Please SignOut", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(RatingActivity.this, RiderMapsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;

            case R.id.profile_nav:
//                Toast.makeText(this, "Not Allowed, Please SignOut ", Toast.LENGTH_SHORT).show();
                Intent intent3 = new Intent(RatingActivity.this, ProfileRiderActivity.class);
                startActivity(intent3);
                break;
            case R.id.log_out:  
                FirebaseAuth.getInstance().signOut();
                Intent intent2=new Intent(RatingActivity.this, MainActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent2);
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
