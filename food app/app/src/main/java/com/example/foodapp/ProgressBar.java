package com.example.foodapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ProgressBar extends AppCompatActivity {

    Button btStart;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_bar);

        btStart = findViewById(R.id.bt_start);
        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initialize Progress Dialog
                progressDialog = new ProgressDialog(ProgressBar.this);
                //show Dialog
                progressDialog.show();
                //Set Context View
                progressDialog.setContentView(R.layout.progress_dialog);
                //Set Transparent Background
                progressDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );
            }
        });
    }
    @Override
    public void onBackPressed() {
        //Dismiss Progress Dialog
        progressDialog.dismiss();
    }
}
