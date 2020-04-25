package com.example.foodapp.ViewHolder;


import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.RelativeLayout;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.foodapp.Model.OrderInfoItem;
import com.example.foodapp.Model.RiderInfoItem;
import com.example.foodapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderItemsAdapter extends FirebaseRecyclerAdapter<OrderInfoItem, OrderItemsAdapter.OrderHolder> {

    private OnItemClickListener listener;
//    double latitude, longitude;
//    private DatabaseReference riderRef= FirebaseDatabase.getInstance().getReference();


    public OrderItemsAdapter(@NonNull FirebaseRecyclerOptions<OrderInfoItem> options) {

        super(options);
    }

    @Override

    protected void onBindViewHolder(@NonNull OrderHolder holder, int position, @NonNull final OrderInfoItem model)
    {
//        final int R = 6371; // Radius of the earth
//
//        double latDistance = Math.toRadians(model.getLatitude() - lat);
//        double lonDistance = Math.toRadians(model.getLongitude() - longi);
//        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
//                + Math.cos(Math.toRadians(lat)) * Math.cos(Math.toRadians(model.getLatitude()))
//                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
//        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
//
//        distance = R*c;
//        Log.d("Latitude: ", String.valueOf(lat));
//        Log.d("Distance: ", String.valueOf(distance));
//        Log.d("Radius: ", String.valueOf(riderRadius));
//        if(distance <= riderRadius) {
            holder.cartTotal.setText("Delivery Amount : ");
            holder.cartAmount.setText(String.format("%.2f",model.getCartTotal()));
            holder.distance.setText(String.format("Distance : %.2f Km", model.getDistance()));
            holder.viewDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(model.getLatitude(), model.getLongitude(), model.getPhone_Number(), model.getUser_ID(), model.getCartTotal(), model.getDistance());
                }
            });
//        }
//        else{
//            holder.riderLayout.setVisibility(View.GONE);
//            holder.viewDetails.setText("Not in Range");
//        }



    }

    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rider_screen,

                parent, false);
        return new OrderHolder(v);

    }

    class OrderHolder extends RecyclerView.ViewHolder {

        TextView cartAmount;
        TextView cartTotal;
//        TextView latitude;
//        TextView longitude;
        TextView distance;
        Button viewDetails;
        RelativeLayout riderLayout;

        public OrderHolder(@NonNull View itemView) {
            super(itemView);
            riderLayout=itemView.findViewById(R.id.rider_layout);
            cartAmount = itemView.findViewById(R.id.text_view_cartAmount);
            cartTotal = itemView.findViewById(R.id.deliveryAmount);
            distance= itemView.findViewById(R.id.txt_distance);
//            latitude = itemView.findViewById(R.id.text_view_Latitude);
//            longitude = itemView.findViewById(R.id.text_view_Longitude);
            viewDetails = itemView.findViewById(R.id.button_send);


        }
    }

    public interface OnItemClickListener{

        void onItemClick(double latitude, double longitude, String mobNumber, String user_ID, double cartTotal, double dist);

    }
    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.listener = listener;
    }

}

