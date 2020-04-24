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

public class OtpVerifyAcitvity extends AppCompatActivity {
    Button verify_btn;
    EditText phoneNoEnteredByTheUser;
    ProgressBar progressBar;
    private DatabaseReference otpRef;
    String uid, userPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verify_acitvity);

        uid=getIntent().getStringExtra("UserID");
        userPhone=getIntent().getStringExtra("PhoneNumber");

        verify_btn = findViewById(R.id.verify_btn);
        phoneNoEnteredByTheUser = findViewById(R.id.verification_code_entered_by_user);
        progressBar = findViewById(R.id.progress_bar);
        otpRef= FirebaseDatabase.getInstance().getReference("OtpStatus").child(uid);

        ActivityCompat.requestPermissions(OtpVerifyAcitvity.this,
                new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);

        progressBar.setVisibility(View.GONE);

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
                        if(otpItem.getOtp().contentEquals(phoneNoEnteredByTheUser.getText())){
                            Toast.makeText(OtpVerifyAcitvity.this, "Order Confirmed", Toast.LENGTH_SHORT).show();

                            String number = userPhone ;
                            String message=String.format("Your Order id is %s. Deliver Person Details-> Name: %s, Mobile Number: %s"
                        ,orderID,riderName, riderNumber);


                            SmsManager mysmsManager= SmsManager.getDefault();
                            mysmsManager.sendTextMessage(number,null,message,null,null);

                            startActivity(new Intent(OtpVerifyAcitvity.this,thankyou.class));
                            finish();
                        }
                        else{
                            Toast.makeText(OtpVerifyAcitvity.this, "Incorrect Otp", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }
}
