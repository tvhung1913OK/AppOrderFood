package com.example.foodapp.Fragment_Customer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.toolbox.StringRequest;
import com.example.foodapp.Adapter_Customer.Menu_CategoryAdapter;
import com.example.foodapp.Adapter_Customer.Menu_ProductAdapter;
import com.example.foodapp.Model.Category;
import com.example.foodapp.Model.Product;
import com.example.foodapp.R;
import com.example.foodapp.config.Config;
import com.example.foodapp.config.VolleySingleton;
import com.example.foodapp.databinding.FragmentFoodsCustomerBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class FoodsFragment extends Fragment {
    private static final String TAG = "Food_Fragment";
    private FragmentFoodsCustomerBinding binding;
    private ArrayList<Category> categoryData;
    private ArrayList<Category> listFood;
    private ArrayList<Product> productData;
    ArrayList<Product> listFoodProduct;
    private Menu_CategoryAdapter categoryAdapter;
    private Menu_ProductAdapter productAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFoodsCustomerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        categoryData = new ArrayList<>();

        getCategory();
        getProduct(null);
    }

    @Override
    public void onDestroy() {
        binding = null;
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.getRoot().requestLayout();
    }

    public void getCategory() {
        VolleySingleton.getInstance(getContext()).addToRequestQueue(new StringRequest(Config.IP + "category", response -> {
            categoryData = new Gson().fromJson(response, new TypeToken<ArrayList<Category>>() {
            }.getType());
            listFood = new ArrayList<>();
            Category category = new Category();
            category.setName("All");
            listFood.add(category);
            for (Category c :
                    categoryData) {
                if (c.getType().equals("food")) {
                    listFood.add(c);
                }
            }
            categoryAdapter = new Menu_CategoryAdapter(listFood, idCategory -> {
                getProduct(idCategory);
            });
            binding.rcvFoodCategoryMenu.setAdapter(categoryAdapter);
        }, error -> {
            Toast.makeText(getContext(), "something went wrong", Toast.LENGTH_SHORT).show();
        }));
    }

    public void getProduct(String idCategory) {
        VolleySingleton.getInstance(getContext()).addToRequestQueue(new StringRequest(Config.IP + "Product", response -> {
            productData = new Gson().fromJson(response, new TypeToken<ArrayList<Product>>() {
            }.getType());
            listFoodProduct = new ArrayList<>();
            for (Product p :
                    productData) {
                if (p.getCategory().getType().equals("food") && idCategory == null) {
                    listFoodProduct.add(p);
                } else if(p.getCategory().getType().equals("food") && p.getCategory().getId().equals(idCategory)) {
                    listFoodProduct.add(p);
                }
            }
            productAdapter = new Menu_ProductAdapter(listFoodProduct);
            binding.rcvFoodProductMenu.setAdapter(productAdapter);
        }, error -> {
        }));
    }
}