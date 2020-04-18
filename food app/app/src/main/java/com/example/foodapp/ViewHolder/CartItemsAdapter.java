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

public class CartItemsAdapter extends FirebaseRecyclerAdapter<CartItem , CartItemsAdapter.CartItemsViewHolder> {

    private OnItemClickListener listener;
    private OnValueChangeListener valueListener;

    public CartItemsAdapter(@NonNull FirebaseRecyclerOptions<CartItem> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CartItemsViewHolder holder, final int position, @NonNull final CartItem model) {
        holder.mFoodName.setText(model.getName());
        holder.mFoodPrice.setText("Base Price : "+model.getPrice());
        holder.mNumberButton.setNumber(String.valueOf(model.getQuantity()));
        int q = Integer.parseInt(model.getQuantity());
        int bp = Integer.parseInt(model.getPrice());
        int tp = q*bp;
        holder.mTotalPrice.setText("Price : "+tp);

        holder.mRemove.setOnClickListener(  new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemDelete(model.getProduct_ID(),position);
            }
        });

        holder.mNumberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                valueListener.onQuantityChange(model.getProduct_ID(),newValue);
            }
        });

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

    public interface OnItemClickListener{
        void onItemDelete(String PID,int position);
    }

    public interface OnValueChangeListener{
        void onQuantityChange(String PID,int quantity);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public void setOnValueChangeListener(OnValueChangeListener listener){
        this.valueListener = listener;
    }
}