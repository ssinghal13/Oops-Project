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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class DeliveryManMapsActivity extends AppCompatActivity {

    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;
    private Button share_location_rider;
    private FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    private FirebaseDatabase user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_man_maps);

        share_location_rider=findViewById(R.id.shareLocation_rider);

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map_rider);

        //Initialize fused location
        client = LocationServices.getFusedLocationProviderClient(this);

        //Check permission
        if(ActivityCompat.checkSelfPermission(DeliveryManMapsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            //When permission granted
            //Call method
            getCurrentLocation();
        }else{
            //when permission denied
            //request permission
            ActivityCompat.requestPermissions(DeliveryManMapsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }
    private void getCurrentLocation() {
        //Initialize task locaion
        Task<Location> task= client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {
                //When success
                if(location!=null)
                    //Sync map
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            //Initialize lat long
                            LatLng latLng = new LatLng(location.getLatitude()
                                    ,location.getLongitude());
                            //Create marker options
                            MarkerOptions options = new MarkerOptions().position(latLng)
                                    .title("I see you");
                            //zoom map
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                            //add marker on map
                            googleMap.addMarker(options);

                            share_location_rider.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //Location added in orders
//                                    HashMap<String,Object> loc = new HashMap<>();
//                                    loc.put("Latitude",location.getLatitude());
//                                    loc.put("Longitude",location.getLongitude());
//                                    loc.put("Phone_Number", mobNumber);
                                    final Intent intent=new Intent(DeliveryManMapsActivity.this, RiderMainActivity.class);
                                    intent.putExtra("User_ID",firebaseAuth.getCurrentUser().getUid());
//                                    intent.putExtra("PhoneNumber", u.PhoneNumber);
                                    intent.putExtra("Latitude", location.getLatitude());
                                    intent.putExtra("Longitude",location.getLongitude());
//                                    packetRef.child(uid).child("Customer_Details").setValue(mobNumber);
                                    startActivity(intent);

                                }
                            });
                        }
                    });
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 44){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //when permission granted
                //call method
                getCurrentLocation();
            }
        }

    }
}
