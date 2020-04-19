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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class DeliverSignInActivity extends AppCompatActivity {

    private EditText email_id;
    private EditText password;

    private Button btn_signIn;
    private Button btn_createAcc;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver_sign_in);

        auth = FirebaseAuth.getInstance();

        email_id = findViewById(R.id.email_id);
        password = findViewById(R.id.password);
        btn_createAcc = findViewById(R.id.email_createAcc_button);
        btn_signIn = findViewById(R.id.email_sign_in_button);

        btn_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = email_id.getText().toString().trim();
                String txt_password = email_id.getText().toString().trim();

                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
                    Toast.makeText(DeliverSignInActivity.this, "Empty Credentials", Toast.LENGTH_SHORT).show();
                } else if (txt_password.length() < 6) {
                    Toast.makeText(DeliverSignInActivity.this, "Password too short", Toast.LENGTH_SHORT).show();
                } else {
                    loginUser(txt_email, txt_password);
                }
            }
        });
        btn_createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DeliverSignInActivity.this, DeliverySignUpActivity.class));
            }
        });
    }

    public void loginUser(String email, String password){
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(DeliverSignInActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(DeliverSignInActivity.this, DeliveryManMapsActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DeliverSignInActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
