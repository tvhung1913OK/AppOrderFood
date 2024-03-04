package com.example.foodapp.adapter_staff;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.Model.Staff;
import com.example.foodapp.databinding.ItemManagerAccountBinding;

import java.util.ArrayList;

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.ViewHolder> {

    public ArrayList<Staff> list;

    public StaffAdapter(ArrayList<Staff> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemManagerAccountBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
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
        ItemManagerAccountBinding binding;

        public ViewHolder(@NonNull ItemManagerAccountBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindData(Staff staff) {
            binding.tvEmail.setText(staff.getUser().getEmail());
            binding.tvName.setText(staff.getUser().getName());
            binding.tvPhone.setText(staff.getUser().getPhone());
        }
    }
}
