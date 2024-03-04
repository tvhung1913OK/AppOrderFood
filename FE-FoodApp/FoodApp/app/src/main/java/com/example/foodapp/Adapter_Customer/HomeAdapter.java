package com.example.foodapp.Adapter_Customer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.Fragment_Customer.BuyingFood_Fragment;
import com.example.foodapp.Model.Product;
import com.example.foodapp.R;
import com.example.foodapp.databinding.ItemProductBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.Holder> {
    private final ArrayList<Product> list;

    public HomeAdapter(ArrayList<Product> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(ItemProductBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.bindData(list.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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


                FragmentTransaction transaction = ((FragmentActivity) view.getContext()).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout, buyingFoodFragment);

                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


    }

    @Override
    public int getItemCount() {
        if(list != null) {
            return list.size();
        }
        return 0;
    }

    public static class Holder extends RecyclerView.ViewHolder {

        ItemProductBinding binding;

        public Holder(@NonNull ItemProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("SetTextI18n")
        void bindData(Product product){
            Picasso.get().load(product.getImage()).resize(151,105).centerCrop().into(binding.imgDrink);
            binding.tvNameDrink.setText(product.getName());
            binding.tvPriceDrink.setText(product.getPrice().toString());
        }
    }
}
