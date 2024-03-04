package com.example.foodapp.adapter_staff;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.foodapp.Model.Product;
import com.example.foodapp.databinding.ItemFoodMenuStaffBinding;

import java.util.ArrayList;

public class StaffMenuProductAdapter extends RecyclerView.Adapter<StaffMenuProductAdapter.Holder> {

    public ArrayList<Product> list;

    public StaffMenuProductAdapter(ArrayList<Product> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StaffMenuProductAdapter.Holder(ItemFoodMenuStaffBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.bindData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    static final class Holder extends RecyclerView.ViewHolder {
        ItemFoodMenuStaffBinding binding;
        public Holder(@NonNull ItemFoodMenuStaffBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("SetTextI18n")
        void bindData(Product product) {
            binding.tvFoodStaff.setText(product.getName());
            binding.tvPriceFoodStaff.setText(product.getPrice().toString());
        }
    }
}
