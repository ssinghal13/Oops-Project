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
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class UserSignUpActivity extends AppCompatActivity {

    private EditText fullName;
    private EditText username;
    private EditText phoneNumber;
    private EditText email_id;
    private EditText password;
    private RadioButton regularButton, irregularButton;
    private Button btn_createAcc;
    private RadioGroup CustomerTypeGroup;
    private FirebaseAuth auth;

    String finalCustomerType;
    String finalRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_up);

        auth= FirebaseAuth.getInstance();

        fullName=findViewById(R.id.fullName);
        username=findViewById(R.id.username);
        phoneNumber=findViewById(R.id.phoneNumber);
        email_id=findViewById(R.id.email_id);
        password=findViewById(R.id.password);
        btn_createAcc=findViewById(R.id.email_createAcc_button);
        regularButton = (RadioButton) findViewById(R.id.RegularButton);
        irregularButton = (RadioButton) findViewById(R.id.IrregularButton);
        CustomerTypeGroup = (RadioGroup) findViewById(R.id.CustomerTypeGroup);

//        customerButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (customerButton.isChecked()){
//                    CustomerTypeGroup.setVisibility(View.VISIBLE);
//                }
//            }
//        });
//
//        riderButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (riderButton.isChecked()){
//                    CustomerTypeGroup.setVisibility(View.INVISIBLE);
//                }
//            }
//        });


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

//                if(TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
//                    Toast.makeText(UserSignUpActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
//                }
//                else if(txt_password.length()<6){
//                    Toast.makeText(UserSignUpActivity.this, "Password too short", Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    registerUser(txt_email, txt_password);
//                }
                if (TextUtils.isEmpty(txt_fullName)) {
                    Toast.makeText(UserSignUpActivity.this,  "Please Enter Full Name",Toast.LENGTH_SHORT).show();
//                    progressDialog1.dismiss();
                    return;
                }

                if (TextUtils.isEmpty(txt_username)) {
                    Toast.makeText(UserSignUpActivity.this,  "Please Enter Username",Toast.LENGTH_SHORT).show();
//                    progressDialog1.dismiss();
                    return;
                }

                if (TextUtils.isEmpty(txt_phoneNumber)) {
                    Toast.makeText(UserSignUpActivity.this,  "Please Enter Username",Toast.LENGTH_SHORT).show();
//                    progressDialog1.dismiss();
                    return;
                }

                if (TextUtils.isEmpty(txt_email)) {
                    Toast.makeText(UserSignUpActivity.this,  "Please Enter Email",Toast.LENGTH_SHORT).show();
//                    progressDialog1.dismiss();
                    return;
                }

                if (TextUtils.isEmpty(txt_password)) {
                    Toast.makeText(UserSignUpActivity.this,  "Please Enter Password",Toast.LENGTH_SHORT).show();
//                    progressDialog1.dismiss();
                    return;
                }


//                if (customerButton.isChecked() == false && riderButton.isChecked() == false) {
//                    Toast.makeText(UserSignUpActivity.this,  "Please select either Customer or Rider",Toast.LENGTH_SHORT).show();
////                    progressDialog1.dismiss();
//                    return;
//                }
//                if (customerButton.isChecked()) {
//
//                    if (regularButton.isChecked() == false && irregularButton.isChecked() == false) {
//                        Toast.makeText(UserSignUpActivity.this, "Please select either Regular or Irregular", Toast.LENGTH_SHORT).show();
////                        progressDialog1.dismiss();
//                        return;
//                    }
//                    Role = "Customer";
//                    if (regularButton.isChecked()){
//                        CustomerType = "Regular";
//                    } else if (irregularButton.isChecked()){
//                        CustomerType = "Irregular";
//                    }
//                } else if (riderButton.isChecked()) {
//                    Role = "Rider";
//                }

                Role = "Customer";
                if (regularButton.isChecked()){
                    CustomerType = "Regular";
                } else if (irregularButton.isChecked()){
                    CustomerType = "Irregular";
                }

                final String finalRole = Role;
                final String finalCustomerType = CustomerType;
                auth.createUserWithEmailAndPassword(txt_email, txt_password)
                        .addOnCompleteListener(UserSignUpActivity.this, new OnCompleteListener<AuthResult>() {
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
                                    HashMap<String,Object> rat=new HashMap<>();
                                    rat.put("rating", 3);
                                    rat.put("orders",0);
                                    FirebaseDatabase.getInstance().getReference("Rating")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(rat);


                                    FirebaseDatabase.getInstance().getReference("user")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(details).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(UserSignUpActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                            Intent intent=new Intent(UserSignUpActivity.this, Main2Activity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
//                                            startActivity(new Intent(getApplicationContext(), Main2Activity.class));
//                                            progressDialog1.dismiss();
                                        }
                                    });


                                } else {

                                    Toast.makeText(UserSignUpActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();

                                }

                            }
                        });


            }
        });

    }

}
