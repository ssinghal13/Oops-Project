package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class DeliverySignUpActivity extends AppCompatActivity {

    private EditText username;
    private EditText email_id;
    private EditText password;

    private Button btn_signIn;
    private Button btn_createAcc;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_sign_up);

        auth = FirebaseAuth.getInstance();

        username=findViewById(R.id.username);
        email_id = findViewById(R.id.email_id);
        password = findViewById(R.id.password);
        btn_createAcc = findViewById(R.id.email_createAcc_button);


        btn_createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_username=username.getText().toString().trim();
                String txt_email = email_id.getText().toString().trim();
                String txt_password = password.getText().toString();

                if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
                    Toast.makeText(DeliverySignUpActivity.this, "Empty Credentials", Toast.LENGTH_SHORT).show();
                } else if (txt_password.length() < 6) {
                    Toast.makeText(DeliverySignUpActivity.this, "Password too short", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(txt_email, txt_password);
                }
            }
        });
    }
    public void registerUser(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(DeliverySignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(DeliverySignUpActivity.this, "Register Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(DeliverySignUpActivity.this,Main2Activity.class));
                            finish();
                        } else {
                            Toast.makeText(DeliverySignUpActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
