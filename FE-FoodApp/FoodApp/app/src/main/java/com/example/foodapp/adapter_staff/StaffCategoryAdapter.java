package com.example.foodapp.adapter_staff;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.Model.Category;
import com.example.foodapp.databinding.ItemCategoryStaffBinding;

import java.util.ArrayList;

public class StaffCategoryAdapter extends RecyclerView.Adapter<StaffCategoryAdapter.ViewHolder> {

    private ArrayList<Category> list;

    public StaffCategoryAdapter(ArrayList<Category> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemCategoryStaffBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    static final class ViewHolder extends RecyclerView.ViewHolder {

        ItemCategoryStaffBinding binding;

        public ViewHolder(@NonNull ItemCategoryStaffBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("SetTextI18n")
        void bindData(Category category) {
            binding.tvName.setText(category.getName());
            binding.tvType.setText("- " + category.getType() + " -");
        }
    }
}
