package com.example.foodapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;

public class PlacePicker extends AppCompatActivity {
    private TextView get_place;
    int PLACE_PICKER_REQUEST=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_picker);

        get_place = (TextView)findViewById(R.id.editText3);
        get_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                PlacePicker.IntentBuilder builder= new PlacePicker.IntentBuilder();
//                Intent intent;
//                intent = builder.build(getApplicationContext());
//                startActivityForResult(intent , PLACE_PICKER_REQUEST );
                com.google.android.gms.location.places.ui.PlacePicker.IntentBuilder builder = new com.google.android.gms.location.places.ui.PlacePicker.IntentBuilder();
                Intent intent;
                try {
                    intent = builder.build((Activity) getApplicationContext());
                    startActivityForResult(intent , PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = com.google.android.gms.location.places.ui.PlacePicker.getPlace(data,this);
                String address = String.format("Place: %s", place.getAddress());
                get_place.setText(address);
            }
        }
    }
}
