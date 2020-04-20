package com.example.foodapp.ViewHolder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.Model.OrderInfoItem;
import com.example.foodapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class OrderItemsAdapter extends FirebaseRecyclerAdapter<OrderInfoItem, OrderItemsAdapter.OrderHolder> {

    private OnItemClickListener listener;
    double latitude, longitude;



    public OrderItemsAdapter(@NonNull FirebaseRecyclerOptions<OrderInfoItem> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull OrderHolder holder, int position, @NonNull final OrderInfoItem model)
    {
        holder.mobile_no.setText(model.getPhone_Number());
        holder.cart_amount.setText(String.valueOf(model.getCartTotal()));
        holder.latitude.setText(String.valueOf(model.getLatitude()));
        holder.longitude.setText(String.valueOf(model.getLongitude()));
//        holder.distance.setText("Distance : "+ model.getDistance);

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(model.getLatitude(),model.getLongitude(), model.getPhone_Number(), model.getUser_ID(), model.getCartTotal());
            }
        });

    }

    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item,
                parent, false);
        return new OrderHolder(v);

    }

    class OrderHolder extends RecyclerView.ViewHolder {
        TextView mobile_no;
        TextView cart_amount;
        TextView latitude;
        TextView longitude;
        TextView distance;
        Button accept;

        public OrderHolder(@NonNull View itemView) {
            super(itemView);
            mobile_no = itemView.findViewById(R.id.text_view_mob);
            cart_amount = itemView.findViewById(R.id.text_view_cartAmount);
//            distance= itemView.findViewById(R.id.text_view_distance);
            latitude = itemView.findViewById(R.id.text_view_Latitude);
            longitude = itemView.findViewById(R.id.text_view_Longitude);
            accept = itemView.findViewById(R.id.button_send);

        }
    }

    public interface OnItemClickListener{
        void onItemClick(double latitude, double longitude, String mobNumber, String user_ID, double cartTotal);
    }
    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.listener = listener;
    }

}

