package com.example.foodapp.ViewHolder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.foodapp.Model.CartItem;
import com.example.foodapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.button.MaterialButton;

public class CartItemsAdapter extends FirebaseRecyclerAdapter<CartItem, CartItemsAdapter.CartItemsViewHolder> {

    public CartItemsAdapter(@NonNull FirebaseRecyclerOptions<CartItem> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CartItemsViewHolder holder, int position, @NonNull CartItem model) {
        holder.mFoodName.setText(model.getName());
        holder.mFoodPrice.setText("Base Price : "+model.getPrice());
        holder.mNumberButton.setNumber(model.getQuantity());
        int q = Integer.parseInt(model.getQuantity());
        int bp = Integer.parseInt(model.getPrice());
        int tp = q*bp;
        holder.mTotalPrice.setText("Price : "+tp);

    }

    @NonNull
    @Override
    public CartItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout,parent,false);
        CartItemsViewHolder holder = new CartItemsViewHolder(view);

        return holder;
    }

    class CartItemsViewHolder extends RecyclerView.ViewHolder{

        TextView mFoodName;
        TextView mFoodPrice;
        TextView mTotalPrice;
        MaterialButton mRemove;
        ElegantNumberButton mNumberButton;

        public CartItemsViewHolder(@NonNull View itemView) {
            super(itemView);

            mFoodName = itemView.findViewById(R.id.food_name_textView);
            mFoodPrice = itemView.findViewById(R.id.price_textView);
            mTotalPrice = itemView.findViewById(R.id.total_price_item_text_view);
            mRemove = itemView.findViewById(R.id.remove_btn);
            mNumberButton = itemView.findViewById(R.id.quantity_btns);


        }
    }
}
