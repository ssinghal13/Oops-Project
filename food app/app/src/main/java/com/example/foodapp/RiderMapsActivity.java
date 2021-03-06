package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
    public EditText setRadius;

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
                            MarkerOptions options = new MarkerOptions();
                            final MarkerOptions position = options.position(latLng).draggable(true).title("Rider's Location");
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                            googleMap.addMarker(options);
                            shareLocation.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    setRadius=findViewById(R.id.setRadius);
                                    final Intent intent=new Intent(RiderMapsActivity.this, RiderMainActivity.class);
                                    intent.putExtra("UID",firebaseAuth.getCurrentUser().getUid().toString());
                                    intent.putExtra("Longitude",location.getLongitude());
                                    intent.putExtra("Latitude",location.getLatitude());

                                    double radius = Double.parseDouble(setRadius.getText().toString());
                                    if(radius>0.0)
                                    {
                                        intent.putExtra("Rider Radius", radius);
                                        startActivity(intent);
                                    }
                                    else{
                                        Toast.makeText(RiderMapsActivity.this, "Enter Correct Radius", Toast.LENGTH_SHORT).show();
                                    }



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
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        RiderMapsActivity.super.onBackPressed();
                    }
                }).create().show();
    }
}
