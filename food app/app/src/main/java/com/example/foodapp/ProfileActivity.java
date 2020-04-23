package com.example.foodapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
<<<<<<< HEAD
        toolbar = findViewById(R.id.main_toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
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
        mEmail = findViewById(R.id.mEmail);
        UpdateButton = findViewById(R.id.UpdateButton);

        databaseReference = FirebaseDatabase.getInstance().getReference("user");
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase.getInstance().getReference("user")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String Username = dataSnapshot.child("username").getValue().toString();
                        String FullName = dataSnapshot.child("fullname").getValue().toString();
                        String Email = dataSnapshot.child("email").getValue().toString();
                        String PhoneNumber = dataSnapshot.child("phonenumber").getValue().toString();
                        mUsername.setText(Username);
                        mFullName.setText(FullName);
                        mMobileNumber.setText(PhoneNumber);
                        mEmail.setText(Email);
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
                                String Username = dataSnapshot.child("username").getValue().toString();
                                String FullName = dataSnapshot.child("fullname").getValue().toString();
                                String Email = dataSnapshot.child("email").getValue().toString();
                                String PhoneNumber = dataSnapshot.child("phonenumber").getValue().toString();

                                final String User_Name = mUsername.getText().toString();
                                final String Full_Name = mFullName.getText().toString();
                                final String E_mail = mEmail.getText().toString();
                                final String Phone_Number = mMobileNumber.getText().toString();

                                if (!Full_Name.equals(FullName)) {
                                    FirebaseDatabase.getInstance().getReference("user")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child("fullname").setValue(Full_Name);
                                    mFullName.setText(Full_Name);
                                }
                                if (!User_Name.equals(Username)) {
                                    FirebaseDatabase.getInstance().getReference("user")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child("username").setValue(User_Name);
                                    mUsername.setText(User_Name);
                                }
                                if (!E_mail.equals(Email)) {
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
                                }
                                if (!Phone_Number.equals(PhoneNumber)){
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
                Intent intent = new Intent(ProfileActivity.this,  MainActivity.class);
                startActivity(intent);
                break;
            case R.id.log_out:
                FirebaseAuth.getInstance().signOut();
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
=======
>>>>>>> parent of 1017781... Edit_profile
    }
}
