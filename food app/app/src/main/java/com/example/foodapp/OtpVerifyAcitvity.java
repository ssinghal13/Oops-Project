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

import java.util.HashMap;
import java.util.Random;

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

        verify_btn = findViewById(R.id.verify_btn_rider);
        phoneNoEnteredByTheUser = findViewById(R.id.verification_code_entered_by_rider);
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
                        String riderNumber=otpItem.getRidernumber();

                        int i = new Random().nextInt(900000) + 100000;



                        assert otpItem != null;
                        if(otpItem.getOtp().contentEquals(phoneNoEnteredByTheUser.getText())){
                            Toast.makeText(OtpVerifyAcitvity.this, "Order Confirmed", Toast.LENGTH_SHORT).show();

                            String number= riderNumber;
                            String message=String.format("The user has confirmed the order. Your OTP code is %s", String.valueOf(i));

                            HashMap<String,Object> otp=new HashMap<>();
                            otp.put("riderotp", String.valueOf(i));
                            otpRef.updateChildren(otp);


                            SmsManager mysmsManager= SmsManager.getDefault();
                            mysmsManager.sendTextMessage(number,null,message,null,null);

                            Intent intent=new Intent(OtpVerifyAcitvity.this,thankyou.class);
                            intent.putExtra("UID",uid);
                            startActivity(intent);
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
    @Override
    public void onBackPressed() {
    }
}
