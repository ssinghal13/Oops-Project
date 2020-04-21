package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class DeliverySignUpActivity extends AppCompatActivity {

    private EditText fullName;
    private EditText username;
    private EditText phoneNumber;
    private EditText email_id;
    private EditText password;
    private RadioButton regularButton, irregularButton;

    private Button btn_createAcc;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_sign_up);

        auth = FirebaseAuth.getInstance();

        fullName=findViewById(R.id.fullName);
        username=findViewById(R.id.username);
        phoneNumber=findViewById(R.id.phoneNumber);
        email_id = findViewById(R.id.email_id);
        password = findViewById(R.id.password);
        btn_createAcc = findViewById(R.id.email_createAcc_button);


        btn_createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String txt_username=username.getText().toString().trim();
                final String txt_email=email_id.getText().toString().trim();
                String txt_password=password.getText().toString().trim();
                final String txt_fullName = fullName.getText().toString();
                final String txt_phoneNumber = phoneNumber.getText().toString();
                String Role = "";
                String CustomerType = "";

//                if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
//                    Toast.makeText(DeliverySignUpActivity.this, "Empty Credentials", Toast.LENGTH_SHORT).show();
//                } else if (txt_password.length() < 6) {
//                    Toast.makeText(DeliverySignUpActivity.this, "Password too short", Toast.LENGTH_SHORT).show();
//                } else {
//                    registerUser(txt_email, txt_password);
//                }

                if (TextUtils.isEmpty(txt_fullName)) {
                    Toast.makeText(DeliverySignUpActivity.this,  "Please Enter Full Name",Toast.LENGTH_SHORT).show();
//                    progressDialog1.dismiss();
                    return;
                }

                if (TextUtils.isEmpty(txt_username)) {
                    Toast.makeText(DeliverySignUpActivity.this,  "Please Enter Username",Toast.LENGTH_SHORT).show();
//                    progressDialog1.dismiss();
                    return;
                }

                if (TextUtils.isEmpty(txt_phoneNumber)) {
                    Toast.makeText(DeliverySignUpActivity.this,  "Please Enter Username",Toast.LENGTH_SHORT).show();
//                    progressDialog1.dismiss();
                    return;
                }

                if (TextUtils.isEmpty(txt_email)) {
                    Toast.makeText(DeliverySignUpActivity.this,  "Please Enter Email",Toast.LENGTH_SHORT).show();
//                    progressDialog1.dismiss();
                    return;
                }

                if (TextUtils.isEmpty(txt_password)) {
                    Toast.makeText(DeliverySignUpActivity.this,  "Please Enter Password",Toast.LENGTH_SHORT).show();
//                    progressDialog1.dismiss();
                    return;
                }


                final String finalRole = "Rider";
                final String finalCustomerType = "None";
                auth.createUserWithEmailAndPassword(txt_email, txt_password)
                        .addOnCompleteListener(DeliverySignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    user details = new user(
                                            txt_fullName,
                                            txt_username,
                                            txt_phoneNumber,
                                            txt_email,
                                            finalRole,
                                            finalCustomerType

                                    );

                                    FirebaseDatabase.getInstance().getReference("user")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(details).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(DeliverySignUpActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(), RiderMapsActivity.class));
//                                            progressDialog1.dismiss();
                                        }
                                    });


                                } else {

                                    Toast.makeText(DeliverySignUpActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();

                                }

                            }
                        });


            }
        });

    }
//    public void registerUser(String email, String password) {
//        auth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(DeliverySignUpActivity.this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(DeliverySignUpActivity.this, "Register Successful", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(DeliverySignUpActivity.this,Main2Activity.class));
//                            finish();
//                        } else {
//                            Toast.makeText(DeliverySignUpActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }
}
