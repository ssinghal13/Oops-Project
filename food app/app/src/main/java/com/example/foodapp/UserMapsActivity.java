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
    double latitude_pickUp=0.0;
    double latitude_drop=0.0;
    double longitude_pickUp=0.0;
    double longitude_drop=0.0;

    private DatabaseReference cartRef;
    private DatabaseReference otpRef;
    private DatabaseReference deliveryRef;
    private DatabaseReference pickRef;
    private String mobNumber;
    private String uid;

    private Button shareLocationUser;
    public double distance;
    public double cartTotal;
    public String location1;
    public String location2;
    //private Button shareLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_maps);

        uid = getIntent().getStringExtra("User_ID");
        mobNumber = getIntent().getStringExtra("PhoneNumber");
        cartTotal=getIntent().getDoubleExtra("CartTotal", 0.0);

        otpRef=FirebaseDatabase.getInstance().getReference().child("OtpStatus").child(uid);
        cartRef = FirebaseDatabase.getInstance().getReference().child("Cart").child(uid).child("Info");
        deliveryRef= FirebaseDatabase.getInstance().getReference("DeliveryAddress").child(uid);
        pickRef=FirebaseDatabase.getInstance().getReference("PickUpAddress").child(uid);
        shareLocationUser = findViewById(R.id.shareLocationUser);

        cartRef = FirebaseDatabase.getInstance().getReference("Cart").child(uid);
//        packetRef = FirebaseDatabase.getInstance().getReference("Orders");
        //shareLocation = findViewById(R.id.shareLocation);
        searchView1 = findViewById(R.id.sv_location1);
        searchView2 = findViewById(R.id.sv_location2);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);

        //Assign variable
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);


        //Initialize fused location
//        client = LocationServices.getFusedLocationProviderClient(this);

        //Check permission
//        if (ActivityCompat.checkSelfPermission(UserMapsActivity.this,
//                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            //When permission granted
//            //Call method
////            addOrder(cartRef, packetRef);
//            cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    InfoItem infoItem=dataSnapshot.getValue(InfoItem.class);
//                    assert infoItem != null;
////                    loc.put("CartTotal", infoItem.getCartTotal());
//                    addressRef.child("CartTotal").setValue(infoItem.getCartTotal());
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//            getCurrentLocation();

        searchView1.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                location1 = searchView1.getQuery().toString();
                List<Address> addressList = null;

                if(location1!=null || !location1.equals(""))
                {
                    Geocoder geocoder = new Geocoder(UserMapsActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location1 , 1);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
//                    address.getLatitude();
                    map.addMarker(new MarkerOptions().position(latLng).title("Pickup").draggable(true));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng , 10));
                    latitude_pickUp=address.getLatitude();
                    longitude_pickUp=address.getLongitude();

                }



                return false;
            }

//            @Override
//
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
//                                    .title("I see you");
//                            //zoom map
//                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
//                            //add marker on map
//                            googleMap.addMarker(options);
//
//
//                            shareLocation.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    //Location added in orders
//                                    final HashMap<String,Object> loc = new HashMap<>();
//                                    loc.put("Latitude",location.getLatitude());
//                                    loc.put("Longitude",location.getLongitude());
//                                    loc.put("Phone_Number", mobNumber);
////                                    packetRef.child(uid).child("Customer_Details").setValue(mobNumber);
////                                    packetRef.child(uid).child("Customer_Details").setValue(loc);
//                                    loc.put("User_ID", uid);
//
//
////                                    loc.put("CartTotal", cartRef.child("Info").child("CartTotal").getKey().toString());
//                                    addressRef.updateChildren(loc);
//                                    startActivity(new Intent(UserMapsActivity.this, OrderSuccessful.class));
//
//                                }
//                            });
//
//
//                        }
//                    });
//
//
//                }

            public boolean onQueryTextChange(String newText) {
                return false;

            }
        });

        searchView2.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                location2 = searchView2.getQuery().toString();
                List<Address> addressList = null;

                if(location2!=null || !location2.equals(""))
                {
                    Geocoder geocoder = new Geocoder(UserMapsActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location2 , 1);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    map.addMarker(new MarkerOptions().position(latLng).title("Delivery").draggable(true));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng , 10));
                    latitude_drop=address.getLatitude();
                    longitude_drop=address.getLongitude();
                }


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        supportMapFragment.getMapAsync(this);

        shareLocationUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(latitude_pickUp ==0.0){
                    Toast.makeText(UserMapsActivity.this, "Enter Pick Up Location", Toast.LENGTH_SHORT).show();
                }
                else if(latitude_drop==0.0){
                    Toast.makeText(UserMapsActivity.this, "Enter Drop Location", Toast.LENGTH_SHORT).show();
                }
                else{
                    final HashMap<String,Object> otp = new HashMap<>();
                    otp.put("OTP","NO");
                    otpRef.setValue(otp);

                    final int R = 6371; // Radius of the earth

                    double latDistance = Math.toRadians(latitude_pickUp - latitude_drop);
                    double lonDistance = Math.toRadians(longitude_pickUp - longitude_drop);
                    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                            + Math.cos(Math.toRadians(latitude_drop)) * Math.cos(Math.toRadians(latitude_pickUp))
                            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
                    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

                    distance=R*c;

                    double deliveryCharge=distance*10;
                    double totalAmount=deliveryCharge+cartTotal;

                    final HashMap<String,Object> pickUp = new HashMap<>();
                    pickUp.put("Latitude",latitude_pickUp);
                    pickUp.put("Longitude",longitude_pickUp);
                    pickUp.put("Phone_Number", mobNumber);
                    pickUp.put("User_ID", uid);
                    pickUp.put("CartTotal", totalAmount);
                    pickUp.put("Distance", distance);
                    pickUp.put("Location", location1);
                    pickRef.updateChildren(pickUp);

                    final HashMap<String,Object> drop = new HashMap<>();
                    drop.put("Latitude",latitude_drop);
                    drop.put("Longitude",longitude_drop);
                    drop.put("Phone_Number", mobNumber);
                    drop.put("User_ID", uid);
                    drop.put("CartTotal", totalAmount);
                    drop.put("Distance", distance);
                    drop.put("Location", location2);
                    deliveryRef.updateChildren(drop);

                    Intent intent=new Intent(UserMapsActivity.this, OrderSuccessful.class);
                    intent.putExtra("UID",uid);
                    startActivity(intent);

                }
            }
        });



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


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

    }
}
