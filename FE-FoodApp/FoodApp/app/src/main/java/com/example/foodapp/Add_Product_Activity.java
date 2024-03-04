package com.example.foodapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.foodapp.Model.Category;
import com.example.foodapp.Model.Product;
import com.example.foodapp.adapter_staff.CategoryDropDownAdapter;
import com.example.foodapp.config.Config;
import com.example.foodapp.config.VolleySingleton;
import com.example.foodapp.databinding.ActivityAddProductBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Add_Product_Activity extends AppCompatActivity {

    private static final String TAG = "Add_Product_Activity";

    public ActivityAddProductBinding binding;
    private ArrayList<Category> categoryData;
    private ArrayList<Category> newCategoryData;
    private CategoryDropDownAdapter dropDownAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();

        categoryData = new ArrayList<>();

        binding.imgBack.setOnClickListener(v -> super.finish());

        getCategory(intent.getStringExtra("category_type"));

        if (intent.getStringExtra("button_type").equals("create")) {
            binding.btnCreateProduct.setOnClickListener(v -> {
                createProduct();
            });
        } else {
            Product product = new Gson().fromJson(intent.getStringExtra("product"), Product.class);
            binding.tvTitle.setText("Update a new Product");

            binding.edtName.setText(product.getName());
            binding.categoryDropdown.setText(product.getCategory().getId());
            binding.edtQuantity.setText(String.valueOf(product.getQuantity()));
            binding.edtPrice.setText(String.valueOf(product.getPrice()));
            binding.edtImage.setText(product.getImage());
            binding.edtDescription.setText(product.getDescription());

            binding.btnCreateProduct.setOnClickListener(v -> {
                updateProduct(product.getId());
            });
        }

    }

    @Override
    protected void onDestroy() {
        binding = null;
        super.onDestroy();
    }

    public void getCategory(String type) {
        VolleySingleton.getInstance(this).addToRequestQueue(new StringRequest(Config.IP + "category", response -> {
            categoryData = new Gson().fromJson(response, new TypeToken<ArrayList<Category>>() {
            }.getType());
            newCategoryData = new ArrayList<>();
            for (Category c :
                    categoryData) {
                if (c.getType().equals(type)) {
                    newCategoryData.add(c);
                }
            }
            dropDownAdapter = new CategoryDropDownAdapter(this, newCategoryData);
            binding.categoryDropdown.setAdapter(dropDownAdapter);
            binding.categoryDropdown.setOnFocusChangeListener((v, hasFocus) -> {
                binding.categoryDropdown.showDropDown();
            });
        }, error -> {
            Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
        }) {
            @Override
            public Priority getPriority() {
                return Priority.HIGH;
            }
        });
    }

    public void createProduct() {
        VolleySingleton.getInstance(this).addToRequestQueue(new StringRequest(Request.Method.POST, Config.IP + "product/create", response -> {
            Toast.makeText(this, "Create successfully", Toast.LENGTH_SHORT).show();
            super.finish();
        }, error -> {
            Toast.makeText(this, "Create failure: " + error.networkResponse.statusCode, Toast.LENGTH_SHORT).show();
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("name", binding.edtName.getText().toString());
                params.put("image", binding.edtImage.getText().toString());
                params.put("quantity", binding.edtQuantity.getText().toString());
                params.put("price", binding.edtPrice.getText().toString());
                params.put("description", binding.edtDescription.getText().toString());
                params.put("idCategory", binding.categoryDropdown.getText().toString());
                return params;
            }
        });
    }

    public void updateProduct(String productId) {
        VolleySingleton.getInstance(this).addToRequestQueue(new StringRequest(Request.Method.PUT, Config.IP + "product/update/" + productId, response -> {
            Toast.makeText(this, "Create successfully", Toast.LENGTH_SHORT).show();
            super.finish();
        }, error -> {
            Toast.makeText(this, "Create failure: " + error.networkResponse.statusCode, Toast.LENGTH_SHORT).show();
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("name", binding.edtName.getText().toString());
                params.put("image", binding.edtImage.getText().toString());
                params.put("quantity", binding.edtQuantity.getText().toString());
                params.put("price", binding.edtPrice.getText().toString());
                params.put("description", binding.edtDescription.getText().toString());
                params.put("idCategory", binding.categoryDropdown.getText().toString());
                return params;
            }
        });
    }
}