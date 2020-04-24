package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.foodapp.Model.OtpItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RiderOtpVerify extends AppCompatActivity {
    Button verify_btn;
    EditText phoneNoEnteredByTheRider;
    ProgressBar progressBar;
    private DatabaseReference otpRef;
    String uid, userPhone;
    String userNumber;
    String loc_pickUp;
    String loc_drop;
    String userName;
    Double deliveryAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_otp_verify);

        uid=getIntent().getStringExtra("UserID");
        userPhone=getIntent().getStringExtra("Phone");
        userName=getIntent().getStringExtra("Name");
        loc_pickUp=getIntent().getStringExtra("PickUp");
        loc_drop=getIntent().getStringExtra("Drop");
        deliveryAmount=getIntent().getDoubleExtra("DeliveryAmount",0.0);


        verify_btn = findViewById(R.id.verify_btn_rider);
        phoneNoEnteredByTheRider = findViewById(R.id.verification_code_entered_by_rider);
        progressBar = findViewById(R.id.progress_bar);
        otpRef= FirebaseDatabase.getInstance().getReference("OtpStatus").child(uid);

        ActivityCompat.requestPermissions(RiderOtpVerify.this,
                new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);

        verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otpRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        OtpItem otpItem=dataSnapshot.getValue(OtpItem.class);

                        String orderID=otpItem.getOrderid();
                        String riderName=otpItem.getRidername();
                        String riderNumber=otpItem.getRidernumber();

                        assert otpItem != null;
                        if(otpItem.getRiderotp().contentEquals(phoneNoEnteredByTheRider.getText())){
                            Toast.makeText(RiderOtpVerify.this, "View Order Details", Toast.LENGTH_SHORT).show();

                            String number = userPhone ;
                            String message=String.format("Your Order id is %s. Deliver Person Details-> Name: %s, Mobile Number: %s"
                        ,orderID,riderName, riderNumber);


                            SmsManager mysmsManager= SmsManager.getDefault();
                            mysmsManager.sendTextMessage(number,null,message,null,null);

                            Intent intent=new Intent(RiderOtpVerify.this, OrderDetailsActivity.class);
                            intent.putExtra("PickUp",loc_pickUp);
                            intent.putExtra("Drop",loc_drop);
                            intent.putExtra("Name", userName);
                            intent.putExtra("Phone",userNumber);
                            intent.putExtra("OrderID",orderID);
                            intent.putExtra("UserID", uid);
                            intent.putExtra("DeliveryAmount", deliveryAmount);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Toast.makeText(RiderOtpVerify.this, "Incorrect Otp", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}
