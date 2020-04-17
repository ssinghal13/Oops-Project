package com.example.foodapp.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.R;
import com.google.android.material.button.MaterialButton;

public class FoodItemViewHolder extends RecyclerView.ViewHolder {

    public TextView mFoodItemName;
    //public ImageView mFoodImage;
    public TextView mFoodItemPrice;
    public MaterialButton mAddToCart;


    public FoodItemViewHolder(@NonNull View itemView) {
        super(itemView);

        mFoodItemName = (TextView) itemView.findViewById(R.id.food_name);
        mFoodItemPrice = (TextView) itemView.findViewById(R.id.food_price);
        //mFoodImage = (ImageView) itemView.findViewById(R.id.food_image);
        mAddToCart = (MaterialButton) itemView.findViewById(R.id.addtocart_btn);
    }

}
