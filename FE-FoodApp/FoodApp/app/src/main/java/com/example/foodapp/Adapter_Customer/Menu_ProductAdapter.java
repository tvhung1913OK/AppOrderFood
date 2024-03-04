package com.example.foodapp.Adapter_Customer;

import static android.content.ContentValues.TAG;
import static com.example.foodapp.config.Config.IP;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodapp.Fragment_Customer.BuyingFood_Fragment;
import com.example.foodapp.Fragment_Customer.CartFragment;
import com.example.foodapp.Model.Product;
import com.example.foodapp.R;
import com.example.foodapp.databinding.ItemProductMenuBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Menu_ProductAdapter extends RecyclerView.Adapter<Menu_ProductAdapter.Holder> {

    public ArrayList<Product> list;
    private int quantity = 1;
    boolean orderSuccessful = false;
    Context context ;

    public Menu_ProductAdapter(ArrayList<Product> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(ItemProductMenuBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.bindData(list.get(position));

        holder.binding.imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Product selectedProduct = list.get(holder.getAbsoluteAdapterPosition());


                BuyingFood_Fragment buyingFoodFragment = new BuyingFood_Fragment();


                Bundle args = new Bundle();
                args.putString("productName", selectedProduct.getName());
                args.putInt("productPrice", selectedProduct.getPrice());
                args.putString("productCategory", String.valueOf(selectedProduct.getCategory()));
                args.putString("productDes",selectedProduct.getDescription());
                args.putInt("productDiscount", selectedProduct.getDiscount().getDiscountPerson());
                args.putString("productImg",selectedProduct.getImage());
                args.putString("productId",selectedProduct.getId());
                buyingFoodFragment.setArguments(args);


                FragmentTransaction transaction = ((FragmentActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout, buyingFoodFragment);

                transaction.addToBackStack(null);
                transaction.commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    static final class Holder extends RecyclerView.ViewHolder {
        ItemProductMenuBinding binding;

        public Holder(@NonNull ItemProductMenuBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("SetTextI18n")
        void bindData(Product product) {
            binding.tvName.setText(product.getName());
            binding.tvPrice.setText(product.getPrice().toString());
        }
    }
}
