package com.example.foodapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;

public class smsActivity extends AppCompatActivity {
    private EditText editTextNumber;
    private EditText editTextMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        ActivityCompat.requestPermissions(smsActivity.this,
                new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);

        editTextMessage = findViewById(R.id.editText);
        editTextNumber = findViewById(R.id.editTextnumber);
    }

    public void sendSMS(View view){

        String message= editTextMessage.getText().toString();
        String number = editTextNumber.getText().toString();

        SmsManager mysmsManager= SmsManager.getDefault();
        mysmsManager.sendTextMessage(number,null,message,null,null);
    }
}
