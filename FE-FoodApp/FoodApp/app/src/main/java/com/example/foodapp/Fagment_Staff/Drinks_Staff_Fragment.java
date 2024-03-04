package com.example.foodapp.Fagment_Staff;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
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
import com.example.foodapp.Add_Product_Activity;
import com.example.foodapp.Model.Product;
import com.example.foodapp.R;
import com.example.foodapp.adapter_staff.StaffMenuProductAdapter;
import com.example.foodapp.config.Config;
import com.example.foodapp.config.VolleySingleton;
import com.example.foodapp.databinding.FragmentDrinksStaffBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;


public class Drinks_Staff_Fragment extends Fragment {
    private static final String TAG = "Staff_Drink_Fragment";
    private ArrayList<Product> productData;
    private ArrayList<Product> listDrinkProduct;
    private StaffMenuProductAdapter adapter;
    FragmentDrinksStaffBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDrinksStaffBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.fabCreate.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), Add_Product_Activity.class);
            intent.putExtra("category_type", "drink");
            intent.putExtra("button_type", "create");
            startActivity(intent);
        });

        productData = new ArrayList<>();

        LinearLayoutManager manager = new LinearLayoutManager(requireActivity());
        binding.rcvDrinkProductMenu.setLayoutManager(manager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL);
        binding.rcvDrinkProductMenu.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(binding.rcvDrinkProductMenu);
    }

    @Override
    public void onResume() {
        super.onResume();
        GETProduct();
        binding.getRoot().requestLayout();
    }

    @Override
    public void onDestroy() {
        binding = null;
        super.onDestroy();
    }

    public void GETProduct() {
        VolleySingleton.getInstance(getContext()).addToRequestQueue(new StringRequest(Config.IP + "Product", response -> {
            productData = new Gson().fromJson(response, new TypeToken<ArrayList<Product>>() {
            }.getType());
            listDrinkProduct = new ArrayList<>();
            for (Product p :
                    productData) {
                if (p.getCategory().getType().equals("drink")) {
                    listDrinkProduct.add(p);
                }
            }
            adapter = new StaffMenuProductAdapter(listDrinkProduct);
            binding.rcvDrinkProductMenu.setAdapter(adapter);
        }, error -> {
            Toast.makeText(getContext(), "something went wrong", Toast.LENGTH_SHORT).show();
        }));
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int pos = viewHolder.getAbsoluteAdapterPosition();

            switch (direction) {
                case ItemTouchHelper.LEFT:
                    VolleySingleton.getInstance(requireActivity()).addToRequestQueue(new StringRequest(Request.Method.DELETE, Config.IP + "product/delete/" + listDrinkProduct.get(pos).getId(), response -> {
                        Toast.makeText(requireActivity(), "Delete successfully", Toast.LENGTH_SHORT).show();
                        listDrinkProduct.remove(pos);
                        adapter.notifyItemRemoved(pos);
                        }, error -> {
                        Toast.makeText(requireActivity(), "Create failure: " + error.networkResponse.statusCode, Toast.LENGTH_SHORT).show();
                    }));
                    break;
                case ItemTouchHelper.RIGHT:
                    Intent intent = new Intent(requireActivity(), Add_Product_Activity.class);
                    intent.putExtra("category_type", "drink");
                    intent.putExtra("button_type", "update");
                    intent.putExtra("product", new Gson().toJson(listDrinkProduct.get(pos)));
                    startActivity(intent);
                    break;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX / 5, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.red))
                    .addSwipeLeftActionIcon(R.drawable.delete_24)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.green))
                    .addSwipeRightActionIcon(R.drawable.archive_24)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX / 5, dY, actionState, isCurrentlyActive);
        }
    };
}