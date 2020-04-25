package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DeliverSignInActivity extends AppCompatActivity {

    private EditText email_id;
    private EditText password;

    private Button btn_signIn;
    private Button btn_createAcc;
    private LoginButton loginButton;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private AccessTokenTracker accessTokenTracker;
    private CallbackManager mCallbackManager;

    private static final String Tag="Facebook Authentication";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver_sign_in);
        //CallbackManager mCallbackManager;
        FacebookSdk.sdkInitialize(getApplicationContext());
        FacebookSdk.setApplicationId(getResources().getString(R.string.app_id));
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.foodapp",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        loginButton=findViewById(R.id.fb);
        loginButton.setReadPermissions("email","public_profile");
        mCallbackManager=CallbackManager.Factory.create();
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(Tag,"On Success "+ loginResult);
                handleFacebookToken(loginResult.getAccessToken());


            }

            @Override
            public void onCancel() {
                Log.d(Tag,"OnCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(Tag,"OnError");
            }
        });
        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();
                if(user!=null){
                    UpdateUI(user);
                }
            }
        };

        accessTokenTracker=new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if(currentAccessToken==null){
                    auth.signOut();
                }
            }
        };



        auth = FirebaseAuth.getInstance();

        email_id = findViewById(R.id.email_id);
        password = findViewById(R.id.password);
        btn_createAcc = findViewById(R.id.email_createAcc_button);
        btn_signIn = findViewById(R.id.email_sign_in_button);

        btn_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = email_id.getText().toString().trim();
                String txt_password = password.getText().toString().trim();

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
                .addOnCompleteListener(DeliverSignInActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {


                            FirebaseDatabase.getInstance().getReference("user")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String Role = dataSnapshot.child("role").getValue().toString();
                                    if (Role.equals("Customer")){
//                                progressDialog.dismiss();
                                        Toast.makeText(DeliverSignInActivity.this, "Users Account, Not Allowed", Toast.LENGTH_SHORT).show();

                                    }
                                    else if (Role.equals("Rider")) {
//                                progressDialog.dismiss();
//                                startActivity(new Intent(UserSignInActivity.this, RiderMainActivity.class));
//                                finish();
                                        Toast.makeText(DeliverSignInActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(DeliverSignInActivity.this, RiderMapsActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
//                            progressDialog.dismiss();
                                    Toast.makeText(DeliverSignInActivity.this,  "Error fetching Details",Toast.LENGTH_SHORT).show();
                                }
                            });


                        } else {

//                    progressDialog.dismiss();
                            Toast.makeText(DeliverSignInActivity.this,  "Login Failed ",Toast.LENGTH_SHORT).show();

                        }

                    }
                });
//                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//                    @Override
//                    public void onSuccess(AuthResult authResult) {
//                        Toast.makeText(DeliverSignInActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(DeliverSignInActivity.this, RiderMapsActivity.class));
//                        finish();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(DeliverSignInActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
//                    }
//                });

    }
    private void handleFacebookToken(AccessToken token){
        Log.d(Tag,"handleFacebookToken"+token);
        AuthCredential credential= FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(Tag,"Sign in with Cred successful");
                    FirebaseUser user=auth.getCurrentUser();
                    UpdateUI(user);
                }else{
                    Log.d(Tag,"Sign in with Cred Failed",task.getException());
                    Toast.makeText(DeliverSignInActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode , resultCode , data);
        super.onActivityResult(requestCode , resultCode , data);

    }
    private void UpdateUI(FirebaseUser user){
        if(user!=null){
            FirebaseDatabase.getInstance().getReference("user")
                    .child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String Role = dataSnapshot.child("role").getValue().toString();
                    if (Role.equals("Customer")){
//                                progressDialog.dismiss();
                        Toast.makeText(DeliverSignInActivity.this, "Users Account, Not Allowed", Toast.LENGTH_SHORT).show();

                    }
                    else if (Role.equals("Rider")) {
//                                progressDialog.dismiss();
//                                startActivity(new Intent(UserSignInActivity.this, RiderMainActivity.class));
//                                finish();
                        Toast.makeText(DeliverSignInActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(DeliverSignInActivity.this, RiderMapsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
//            Toast.makeText(DeliverSignInActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(DeliverSignInActivity.this, RiderMapsActivity.class));
//            finish();
        }
    }
    @Override
    protected void onStart(){
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(authStateListener!=null){
            auth.removeAuthStateListener(authStateListener);
        }
    }


}
