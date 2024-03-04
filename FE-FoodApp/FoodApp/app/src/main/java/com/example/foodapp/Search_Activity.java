package com.example.foodapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodapp.Adapter_Customer.Menu_ProductAdapter;
import com.example.foodapp.Login.LoginActivity_Staff;
import com.example.foodapp.Model.Product;
import com.example.foodapp.config.Config;
import com.example.foodapp.databinding.ActivitySearchBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Search_Activity extends AppCompatActivity {
    private ActivitySearchBinding binding;
    private ArrayList<Product> productData;

    private Menu_ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.searchFood.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Không cần thực hiện hành động gì trước khi văn bản thay đổi
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Gọi hàm search mỗi khi văn bản thay đổi
                search(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Không cần thực hiện hành động gì sau khi văn bản thay đổi
            }
        });
    }

    private void search(String stringSearch) {
        // Tạo RequestQueue cho Volley
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        queue.cancelAll("search_request");
        JsonArrayRequest request = new JsonArrayRequest(Config.IP + "product/search?name=" + stringSearch, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                productData = new Gson().fromJson(String.valueOf(response), new TypeToken<ArrayList<Product>>() {
                }.getType());

                productAdapter = new Menu_ProductAdapter(productData);
                binding.rcvSearch.setAdapter(productAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        request.setTag("search_request");
        queue.add(request);
    }
}