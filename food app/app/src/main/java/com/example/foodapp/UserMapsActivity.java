package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodapp.Model.InfoItem;
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
import java.util.List;

public class UserMapsActivity extends FragmentActivity implements OnMapReadyCallback {
    //Initialize variable
    GoogleMap map;
    SupportMapFragment supportMapFragment;
    //FusedLocationProviderClient client;
    SearchView searchView1;
    SearchView searchView2;
    TextView textView1;
    TextView textView2;

    private DatabaseReference cartRef;
    private DatabaseReference packetRef;
    private String mobNumber;
    private String uid;

    private Button shareLocation;
    private DatabaseReference addressRef;

    //private Button shareLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_maps);

        uid = getIntent().getStringExtra("User_ID");
        mobNumber = getIntent().getStringExtra("PhoneNumber");


//        cartRef = FirebaseDatabase.getInstance().getReference("Cart").child(uid).child("Info");
//        packetRef = FirebaseDatabase.getInstance().getReference("Orders");
        cartRef = FirebaseDatabase.getInstance().getReference().child("Cart").child(uid).child("Info");
        addressRef= FirebaseDatabase.getInstance().getReference("DeliveryAddress").child(uid);
        shareLocation = findViewById(R.id.shareLocation);

        cartRef = FirebaseDatabase.getInstance().getReference("Cart").child(uid);
        packetRef = FirebaseDatabase.getInstance().getReference("Orders");
        //shareLocation = findViewById(R.id.shareLocation);
        searchView1 = findViewById(R.id.sv_location1);
        searchView2 = findViewById(R.id.sv_location2);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);

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
//            addOrder(cartRef, packetRef);
            cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    InfoItem infoItem=dataSnapshot.getValue(InfoItem.class);
                    assert infoItem != null;
//                    loc.put("CartTotal", infoItem.getCartTotal());
                    addressRef.child("CartTotal").setValue(infoItem.getCartTotal());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            getCurrentLocation();

        searchView1.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView1.getQuery().toString();
                List<Address> addressList = null;

                if(location!=null || !location.equals(""))
                {
                    Geocoder geocoder = new Geocoder(UserMapsActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location , 1);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    map.addMarker(new MarkerOptions().position(latLng).title("Pickup").draggable(true));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng , 10));
                }



                return false;
            }

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
                                    final HashMap<String,Object> loc = new HashMap<>();
                                    loc.put("Latitude",location.getLatitude());
                                    loc.put("Longitude",location.getLongitude());
                                    loc.put("Phone_Number", mobNumber);
//                                    packetRef.child(uid).child("Customer_Details").setValue(mobNumber);
//                                    packetRef.child(uid).child("Customer_Details").setValue(loc);
                                    loc.put("User_ID", uid);


//                                    loc.put("CartTotal", cartRef.child("Info").child("CartTotal").getKey().toString());
                                    addressRef.updateChildren(loc);
                                    startActivity(new Intent(UserMapsActivity.this, OrderSuccessful.class));

                                }
                            });


                        }
                    });


                }

            public boolean onQueryTextChange(String newText) {
                return false;

            }
        });

        searchView2.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView2.getQuery().toString();
                List<Address> addressList = null;

                if(location!=null || !location.equals(""))
                {
                    Geocoder geocoder = new Geocoder(UserMapsActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location , 1);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    map.addMarker(new MarkerOptions().position(latLng).title("Delivery").draggable(true));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng , 10));
                }


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        supportMapFragment.getMapAsync(this);


//        //Initialize fused location
//        client = LocationServices.getFusedLocationProviderClient(this);
//
//        //Check permission
//        if (ActivityCompat.checkSelfPermission(UserMapsActivity.this,
//                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            //When permission granted
//            //Call method
//            addOrder(cartRef, packetRef);
//            getCurrentLocation();
//
//        } else {
//            //when permission denied
//            //request permission
//            ActivityCompat.requestPermissions(UserMapsActivity.this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
//        }

    }


//    private void addOrder(DatabaseReference cartRef, final DatabaseReference orderRef) {
//
//        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                orderRef.child(uid).setValue(dataSnapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful())
//                            Toast.makeText(UserMapsActivity.this, "Location Shared", Toast.LENGTH_SHORT).show();
//                        else
//                            Toast.makeText(UserMapsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//
//        });
//    }

//    private void getCurrentLocation() {
//        //shareLocation=findViewById(R.id.shareLocation);
//        //Initialize task location
//        Task<Location> task = client.getLastLocation();
//        packetRef = FirebaseDatabase.getInstance().getReference("Orders");
//        task.addOnSuccessListener(new OnSuccessListener<Location>() {
//            @Override
//            public void onSuccess(final Location location) {
//                //When success
//                if (location != null) {
//                    //Sync map
//                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
//                        @Override
//                        public void onMapReady(GoogleMap googleMap) {
//                            //Initialize lat long
//                            LatLng latLng = new LatLng(location.getLatitude()
//                                    , location.getLongitude());
//                            //Create marker options
//                            MarkerOptions options = new MarkerOptions().position(latLng)
//                                    .title("Delivery Point").draggable(true);
//                            //zoom map
//                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
//                            //add marker on map
//                            googleMap.addMarker(options);
//
//
////                            shareLocation.setOnClickListener(new View.OnClickListener() {
////                                @Override
////                                public void onClick(View v) {
////                                    //Location added in orders
////                                    HashMap<String,Object> loc = new HashMap<>();
////                                    loc.put("Latitude",location.getLatitude());
////                                    loc.put("Longitude",location.getLongitude());
////                                    loc.put("Phone_Number", mobNumber);
//////                                    packetRef.child(uid).child("Customer_Details").setValue(mobNumber);
////                                    packetRef.child(uid).child("Customer_Details").setValue(loc);
////                                    startActivity(new Intent(UserMapsActivity.this, OrderSuccessful.class));
////
////                                }
////                            });
//
//
//                        }
//                    });
//
//
//                }
//            }
//        });
//    }



//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode == 44) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                //when permission granted
//                //call method
//                getCurrentLocation();
//            }
//        }
//
//    }

    private void addOrder(DatabaseReference cartRef, final DatabaseReference orderRef) {

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


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

    }
}
