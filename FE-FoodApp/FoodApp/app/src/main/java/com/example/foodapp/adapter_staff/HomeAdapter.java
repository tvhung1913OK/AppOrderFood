package com.example.foodapp.adapter_staff;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.R;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {
    private ArrayList<Map<String, String>> orderDetails;
    private Context context;

    public HomeAdapter(Context context, ArrayList<Map<String, String>> orderDetails) {
        this.orderDetails = orderDetails;
        this.context = context;
    }

    @NonNull
    @Override
    public HomeAdapter.HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_receipt, parent, false);

        HomeViewHolder viewHolder = new HomeViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAdapter.HomeViewHolder holder, int position) {
        Map<String, String> orderDetail = orderDetails.get(position);

        float priceOri = Float.parseFloat(orderDetail.get("price"));
        float priceTotalDis = Float.parseFloat(orderDetail.get("totalPrice"));
        float discount = (1-(priceTotalDis/priceOri))*100;

        holder.tvNameOrder.setText(orderDetail.get("orderItem"));
        holder.nb_quantity.setText(orderDetail.get("count"));
        holder.nb_price.setText(orderDetail.get("price"));
        holder.nb_discout.setText(String.valueOf(Math.round(discount))+" %");
        holder.tv_total_price_Staff.setText(orderDetail.get("totalPrice"));
        holder.tvNameCustomer.setText(orderDetail.get("customerName"));
        holder.tvAddressCustomer.setText(orderDetail.get("customerAddress"));
        holder.tvPhoneCustomer.setText(orderDetail.get("customerPhone"));



    }

    @Override
    public int getItemCount() {
        return orderDetails.size();
    }


    public class HomeViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNameOrder;
        public TextView nb_quantity;
        public TextView nb_price,tvNameCustomer;
        public TextView nb_discout;

        public TextView tv_total_price_Staff, tvAddressCustomer,tvPhoneCustomer;
        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameOrder = itemView.findViewById(R.id.tvNameOrder);
            nb_quantity = itemView.findViewById(R.id.nb_quantity);
            nb_price = itemView.findViewById(R.id.nb_price);
            nb_discout = itemView.findViewById(R.id.nb_discout);
            tv_total_price_Staff = itemView.findViewById(R.id.tv_total_price_Staff);
            tvAddressCustomer =itemView.findViewById(R.id.tvAddressCustomer);
            tvNameCustomer = itemView.findViewById(R.id.tvNameCustomer);
            tvPhoneCustomer = itemView.findViewById(R.id.tvPhoneCustomer);
        }
    }
}
