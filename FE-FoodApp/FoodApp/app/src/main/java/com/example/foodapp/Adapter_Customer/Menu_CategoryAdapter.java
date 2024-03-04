package com.example.foodapp.Adapter_Customer;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.Model.Category;
import com.example.foodapp.databinding.ItemCategoryBinding;

import java.util.ArrayList;

public class Menu_CategoryAdapter extends RecyclerView.Adapter<Menu_CategoryAdapter.Holder> {

    public ArrayList<Category> list;
    private int selectedPos = -1;

    public interface OnItemClick {
        void onClick(String idCategory);
    }

    private OnItemClick onItemClick;

    public Menu_CategoryAdapter(ArrayList<Category> list, OnItemClick onItemClick) {
        this.list = list;
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(ItemCategoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.bindData(list.get(position));
        holder.binding.tvCategoryName.setOnClickListener(v -> {
            selectedPos = holder.getAbsoluteAdapterPosition();
            holder.binding.tvCategoryName.setTypeface(null, Typeface.BOLD);
            onItemClick.onClick(list.get(position).getId());
            notifyDataSetChanged();
        });
        if (selectedPos != position) {
            holder.binding.tvCategoryName.setTypeface(Typeface.DEFAULT);
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class Holder extends RecyclerView.ViewHolder {

        ItemCategoryBinding binding;

        public Holder(@NonNull ItemCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindData(Category category) {
            binding.tvCategoryName.setText(category.getName());
        }
    }


}
