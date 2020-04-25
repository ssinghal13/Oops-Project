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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private EditText mUsername;
    private EditText mFullName;
    private EditText mMobileNumber;
    private TextView userRating;
    private TextView totalOrders;

    private EditText mEmail;
    private Button UpdateButton;
    private FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolbar = findViewById(R.id.main_toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        totalOrders=findViewById(R.id.totalOrders);
        userRating=findViewById(R.id.RatingUser);

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
        navigationView.setCheckedItem(R.id.profile_nav);

        mUsername = findViewById(R.id.mUsername);
        mFullName = findViewById(R.id.mFullName);
        mMobileNumber = findViewById(R.id.mMobileNumber);
//        mEmail = findViewById(R.id.mEmail);
        UpdateButton = findViewById(R.id.UpdateButton);

        databaseReference = FirebaseDatabase.getInstance().getReference("user");
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase.getInstance().getReference("user")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String username = dataSnapshot.child("username").getValue().toString();
                        String fullname = dataSnapshot.child("fullname").getValue().toString();
                        //String email = dataSnapshot.child("email").getValue().toString();
                        String phonenumber = dataSnapshot.child("phonenumber").getValue().toString();
                        mUsername.setText(username);
                        mFullName.setText(fullname);
                        mMobileNumber.setText(phonenumber);
                        // mEmail.setText(email);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        FirebaseDatabase.getInstance().getReference("Rating").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        totalOrders.setText(String.format("Total Orders : %.0f",Double.parseDouble(dataSnapshot.child("orders").getValue().toString())));
                        userRating.setText(String.format("User Rating : %.1f",Double.parseDouble(dataSnapshot.child("rating").getValue().toString())));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



        UpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseDatabase.getInstance().getReference("user")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String username = dataSnapshot.child("username").getValue().toString();
                                String fullname = dataSnapshot.child("fullname").getValue().toString();
                                // String email = dataSnapshot.child("email").getValue().toString();
                                String phonenumber = dataSnapshot.child("phonenumber").getValue().toString();

                                final String User_Name = mUsername.getText().toString();
                                final String Full_Name = mFullName.getText().toString();
                                // final String E_mail = mEmail.getText().toString();
                                final String Phone_Number = mMobileNumber.getText().toString();

                                if (!Full_Name.equals(fullname)) {
                                    FirebaseDatabase.getInstance().getReference("user")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child("fullname").setValue(Full_Name);
                                    mFullName.setText(Full_Name);
                                }
                                if (!User_Name.equals(username)) {
                                    FirebaseDatabase.getInstance().getReference("user")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child("username").setValue(User_Name);
                                    mUsername.setText(User_Name);
                                }
                               /* if (!E_mail.equals(email)) {
                                    FirebaseDatabase.getInstance().getReference("user")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child("email").setValue(E_mail);
                                    mEmail.setText(E_mail);
                                    // [START update_email]
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                    user.updateEmail(E_mail)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(ProfileActivity.this,  "Email Changed",Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                    // [END update_email]
                                }*/
                                if (!Phone_Number.equals(phonenumber)){
                                    FirebaseDatabase.getInstance().getReference("user")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child("phonenumber").setValue(Phone_Number);
                                    mMobileNumber.setText(Phone_Number);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                Toast.makeText(ProfileActivity.this,  "Updated",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.profile_nav:
                break;

            case R.id.nav_home:
                Intent intent = new Intent(ProfileActivity.this,  Main2Activity.class);
                startActivity(intent);
                break;
            case R.id.log_out:
                FirebaseAuth.getInstance().signOut();
                //mGoogleSignInClient.signOut();
                startActivity(new Intent(ProfileActivity.this,  MainActivity.class));
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