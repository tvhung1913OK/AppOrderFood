package com.example.foodapp.Fagment_Staff;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.foodapp.Model.Category;
import com.example.foodapp.R;
import com.example.foodapp.adapter_staff.StaffCategoryAdapter;
import com.example.foodapp.config.Config;
import com.example.foodapp.config.VolleySingleton;
import com.example.foodapp.databinding.FragmentStaffCategoryBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class StaffCategoryFragment extends Fragment {
    public FragmentStaffCategoryBinding binding;
    private ArrayList<Category> categoryData;
    private StaffCategoryAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStaffCategoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        categoryData = new ArrayList<>();

        binding.fabCreate.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new CreateCategoryFragment()).commit();
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity());
        binding.rcvStaffCategory.setLayoutManager(layoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL);
        binding.rcvStaffCategory.addItemDecoration(itemDecoration);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(binding.rcvStaffCategory);

        getCategory();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void getCategory() {
        VolleySingleton.getInstance(getContext()).addToRequestQueue(new StringRequest(Config.IP + "category", response -> {
            categoryData = new Gson().fromJson(response, new TypeToken<ArrayList<Category>>() {
            }.getType());
            adapter = new StaffCategoryAdapter(categoryData);
            binding.rcvStaffCategory.setAdapter(adapter);
        }, error -> {
            Toast.makeText(getContext(), "onFailure: " + error.networkResponse.statusCode, Toast.LENGTH_SHORT).show();
        }));
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAbsoluteAdapterPosition();
            switch (direction) {
                case ItemTouchHelper.LEFT:
                    VolleySingleton.getInstance(requireActivity()).addToRequestQueue(new StringRequest(Request.Method.DELETE, Config.IP + "category/deleteCategory/" + categoryData.get(position).getId(), response -> {
                        Toast.makeText(requireActivity(), "Delete completed", Toast.LENGTH_SHORT).show();
                        categoryData.remove(position);
                        adapter.notifyItemRemoved(position);
                    }, error -> {
                        Toast.makeText(requireActivity(), "onFailure: " + error.networkResponse.statusCode, Toast.LENGTH_SHORT).show();
                    }));
                    break;
                case ItemTouchHelper.RIGHT:
                    Fragment fragment = new CreateCategoryFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("category", new Gson().toJson(categoryData.get(position)));
                    fragment.setArguments(bundle);
                    requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
                    break;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX / 5, dY, actionState, isCurrentlyActive);

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX / 5, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.red))
                    .addSwipeLeftActionIcon(R.drawable.delete_24)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.green))
                    .addSwipeRightActionIcon(R.drawable.archive_24)
                    .create()
                    .decorate();
        }
    };
}