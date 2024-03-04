package com.example.foodapp.Adapter_Customer;

import static com.example.foodapp.config.Config.IP;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodapp.Interface.OnTotalPriceUpdateListener;
import com.example.foodapp.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private ArrayList<Map<String, String>> cartData;
    private Context context;
    private ArrayList<String> selectedOrderItemIds;
    private int selectedItemsTotalPrice = 0;


    private int selectedItemsTotalDiscount = 0;

    private OnTotalPriceUpdateListener listener;


    public CartAdapter(ArrayList<Map<String, String>> cartData, Context context) {
        this.cartData = cartData;
        this.context = context;
        this.selectedOrderItemIds = new ArrayList<>();
    }

    public void setOnTotalPriceUpdateListener(OnTotalPriceUpdateListener listener) {
        this.listener = listener;
    }
    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    // ...
    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Map<String, String> cartItem = cartData.get(position);

        String productName = cartItem.get("productName");
        String productPrice = cartItem.get("productPrice");
        String productQuantity = cartItem.get("productQuantity");
        String total = cartItem.get("productTotal");
        String img = cartItem.get("imageProductOrder");
        String orderId = cartItem.get("idOrderItem");
        String totalOrderValue = cartItem.get("TotalOrder");
        String totalOrderDiscount = cartItem.get("TotalDiscount");



        holder.productName.setText(productName);
        SpannableString spannableString = new SpannableString(productPrice);
        spannableString.setSpan(new StrikethroughSpan(), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.productPrice.setText(spannableString);
        holder.productQuantity.setText(productQuantity);
        holder.productTotal.setText(total);
        holder.checkBoxOrder.setOnCheckedChangeListener(null);  // Tránh gọi lặp đôi
        holder.checkBoxOrder.setChecked(Boolean.parseBoolean(cartItem.get("isChecked")));
        holder.checkBoxOrder.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                cartItem.put("isChecked", "true");
                selectedOrderItemIds.add(orderId);
                selectedItemsTotalPrice += Integer.parseInt(totalOrderValue);
                selectedItemsTotalDiscount += Integer.parseInt(totalOrderDiscount);
            } else {
                cartItem.put("isChecked", "false");
                selectedOrderItemIds.remove(orderId);
                selectedItemsTotalPrice -= Integer.parseInt(totalOrderValue);
                selectedItemsTotalDiscount -= Integer.parseInt(totalOrderDiscount);
            }
            if (listener != null) {
                listener.onTotalPriceUpdated(selectedItemsTotalPrice, selectedItemsTotalDiscount);
            }
            Log.d("CartAdapter", "Checkbox State for Item at position " + position + ": " + holder.checkBoxOrder.isChecked());
        });






        Picasso.get().load(img).into(holder.productImg);

        Log.d("CartAdapter", "orderId: " + orderId);
        Log.d("CartAdapter", "cartItem: " + cartItem.toString());
        holder.imvDeleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();


                if (position != RecyclerView.NO_POSITION) {

                    Map<String, String> deletedItem = cartData.get(position);
                    String orderId = deletedItem.get("idOrderItem");


                    deleteOrderItem(orderId,position);
                }
            }
        });

    }
    private void deleteOrderItem(String orderId,int position) {
        String url = IP+"order-item/deleteOrderItem/" + orderId;

        StringRequest request = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        cartData.remove(position);
                        notifyDataSetChanged();


                        updateTotalPrice();



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(context, "Lỗi khi xóa sản phẩm khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
                    }
                });


        Volley.newRequestQueue(context).add(request);
    }

    public void updateTotalPrice() {
        int totalPrice = 0;
        int totalDiscount = 0;

        for (Map<String, String> orderItem : cartData) {
            if (Boolean.parseBoolean(orderItem.get("isChecked"))) {
                totalPrice += Integer.parseInt(orderItem.get("TotalOrder"));
                totalDiscount += Integer.parseInt(orderItem.get("TotalDiscount"));
            }
        }

        selectedItemsTotalPrice = totalPrice;
        selectedItemsTotalDiscount = totalDiscount;

        if (listener != null) {
            listener.onTotalPriceUpdated(totalPrice, totalDiscount);
        }
    }


    public void clearSelectedOrderItemIds() {
        if (cartData != null) {
            for (Map<String, String> cartItem : cartData) {
                if (cartItem.containsKey("isChecked")) {
                    cartItem.put("isChecked", "false");
                }
            }

            selectedOrderItemIds.clear();

            updateTotalPrice();
            notifyDataSetChanged();
        }
    }



    public ArrayList<Map<String, String>> getSelectedOrderItems() {
        ArrayList<Map<String, String>> selectedItems = new ArrayList<>();
        for (Map<String, String> cartItem : cartData) {
            if (cartItem.containsKey("isChecked") && Boolean.parseBoolean(cartItem.get("isChecked"))) {
                selectedItems.add(cartItem);
            }
        }
        Log.d("CartAdapter", "Selected Order Items: " + selectedItems);
        return selectedItems;
    }


    @Override
    public int getItemCount() {
        if (cartData != null) {

            return cartData.size();
        } else {

            return 0;
        }

    }



    public class CartViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productQuantity, productTotal;
        ShapeableImageView productImg;
        CheckBox checkBoxOrder;
        ImageView imvDeleteProduct;


        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.tvProduct);
            productPrice = itemView.findViewById(R.id.priceProduct);
            productQuantity = itemView.findViewById(R.id.quantityProduct);
            productTotal = itemView.findViewById(R.id.totalPriceProduct);
            productImg = itemView.findViewById(R.id.imageProductOrder);
            checkBoxOrder = itemView.findViewById(R.id.checkboxOrder);
            imvDeleteProduct = itemView.findViewById(R.id.imvDeleteProduct);


        }
    }
}