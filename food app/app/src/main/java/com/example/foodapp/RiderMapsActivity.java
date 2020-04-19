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

public class RiderMapsActivity extends AppCompatActivity {

    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;
    private FirebaseAuth firebaseAuth;
    private Button shareLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_maps);

        shareLocation=findViewById(R.id.shareLocationRider);
        firebaseAuth=FirebaseAuth.getInstance();
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map_rider);

        client= LocationServices.getFusedLocationProviderClient(this);

        if(ActivityCompat.checkSelfPermission(RiderMapsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            getCurrentLocation();
        }else{
            ActivityCompat.requestPermissions(RiderMapsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION} , 44);
        }

    }

    private void getCurrentLocation() {
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {
                if(location!=null)
                {
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            LatLng latLng =new LatLng(location.getLatitude(),
                                    location.getLongitude());
                            MarkerOptions options = new MarkerOptions().position(latLng)
                                    .title("Delivery wala Bhaiya");
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                            googleMap.addMarker(options);
                            shareLocation.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final Intent intent=new Intent(RiderMapsActivity.this, RiderMainActivity.class);
                                    intent.putExtra("UID",firebaseAuth.getCurrentUser().getUid().toString());
                                    intent.putExtra("Longitude",location.getLongitude());
                                    intent.putExtra("Latitude",location.getLatitude());
                                    startActivity(intent);
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
        if(requestCode == 44)
        {
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                getCurrentLocation();
        }
    }
}
