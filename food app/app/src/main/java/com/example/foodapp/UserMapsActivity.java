package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.HashMap;

public class UserMapsActivity extends AppCompatActivity {
    //Initialize variable
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;

    private DatabaseReference cartRef;
    private DatabaseReference packetRef;
    private String mobNumber;
    private String uid;
    private Button shareLocation;
    private DatabaseReference Orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_maps);

        uid = getIntent().getStringExtra("User_ID");
        mobNumber = getIntent().getStringExtra("PhoneNumber");

        cartRef = FirebaseDatabase.getInstance().getReference("Cart").child(uid);
        packetRef = FirebaseDatabase.getInstance().getReference("Orders");
        shareLocation = findViewById(R.id.shareLocation);

        //Assign variable
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);

        //Initialize fused location
        client = LocationServices.getFusedLocationProviderClient(this);

        //Check permission
        if (ActivityCompat.checkSelfPermission(UserMapsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //When permission granted
            //Call method
            addOrder(cartRef, packetRef, mobNumber, uid);
            getCurrentLocation();

        } else {
            //when permission denied
            //request permission
            ActivityCompat.requestPermissions(UserMapsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

    }

    private void getCurrentLocation() {
        shareLocation=findViewById(R.id.shareLocation);
        //Initialize task locaion
        Task<Location> task = client.getLastLocation();
        packetRef = FirebaseDatabase.getInstance().getReference("Orders");
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {
                //When success
                if (location != null) {
                    //Sync map
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            //Initialize lat long
                            LatLng latLng = new LatLng(location.getLatitude()
                                    , location.getLongitude());
                            //Create marker options
                            MarkerOptions options = new MarkerOptions().position(latLng)
                                    .title("I see you");
                            //zoom map
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                            //add marker on map
                            googleMap.addMarker(options);


                            shareLocation.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //Location added in orders
                                    HashMap<String,Object> loc = new HashMap<>();
                                    loc.put("Latitude",location.getLatitude());
                                    loc.put("Longitude",location.getLongitude());
                                    loc.put("Phone Number", mobNumber);
//                                    packetRef.child(uid).child("Customer_Details").setValue(mobNumber);
                                    packetRef.child(uid).child("Customer_Details").setValue(loc);
                                    startActivity(new Intent(UserMapsActivity.this, OrderSuccessful.class));

                                }
                            });


                        }
                    });


                }
            }
        });
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //when permission granted
                //call method
                getCurrentLocation();
            }
        }

    }

    private void addOrder(DatabaseReference cartRef, final DatabaseReference orderRef, final String mobNumber, String user_id) {
//        orderRef.child(user_id).child("Customer_Details").setValue(mobNumber);
//        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                HashMap<String,Object> h = new HashMap<>();
//                h.put("Phone_Number",mobNumber);
//                orderRef.child(uid).child("Customer_Info").setValue(h);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderRef.child(uid).setValue(dataSnapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                            Toast.makeText(UserMapsActivity.this, "Location Shared", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(UserMapsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

}
