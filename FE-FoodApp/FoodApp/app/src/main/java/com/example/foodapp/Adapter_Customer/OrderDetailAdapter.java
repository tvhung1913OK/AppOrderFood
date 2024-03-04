package com.example.foodapp.Adapter_Customer;

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

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> {
    private ArrayList<Map<String, String>> orderDetails;
    private Context context;

    public OrderDetailAdapter(Context context, ArrayList<Map<String, String>> orderDetails) {
        this.orderDetails = orderDetails;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Map<String, String> orderDetail = orderDetails.get(position);

        float priceOri = Float.parseFloat(orderDetail.get("price"));
        float priceTotalDis = Float.parseFloat(orderDetail.get("totalPrice"));
        float discount = (1-(priceTotalDis/priceOri))*100;

        holder.orderItemTextView.setText(orderDetail.get("orderItem"));
        holder.countTextView.setText(orderDetail.get("count"));
        holder.priceTextView.setText(orderDetail.get("price"));
        holder.discountTextView.setText(String.valueOf(Math.round(discount))+" %");
        holder.historyOrderTotal.setText(orderDetail.get("totalPrice"));


        String createAtString = orderDetail.get("createAt");


        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        }
        LocalDateTime createAtDateTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            createAtDateTime = LocalDateTime.parse(createAtString, formatter);
        }


        DateTimeFormatter outputFormatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        }
        String formattedDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formattedDate = createAtDateTime.format(outputFormatter);
        }


        holder.historyOrderDate.setText(formattedDate);

    }

    @Override
    public int getItemCount() {
        return orderDetails.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView orderItemTextView;
        public TextView countTextView;
        public TextView priceTextView;
        public TextView discountTextView;

        public TextView historyOrderTotal, historyOrderDate;

        public ViewHolder(View itemView) {
            super(itemView);
            orderItemTextView = itemView.findViewById(R.id.historyOrderName);
            countTextView = itemView.findViewById(R.id.historyOrderCount);
            priceTextView = itemView.findViewById(R.id.historyOrderPrice);
            discountTextView = itemView.findViewById(R.id.historyOrderDiscount);
            historyOrderTotal = itemView.findViewById(R.id.historyOrderTotal);
            historyOrderDate = itemView.findViewById(R.id.historyOrderDate);
        }
    }
}
