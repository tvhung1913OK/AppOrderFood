package com.example.foodapp.Fagment_Staff;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.foodapp.Model.Category;
import com.example.foodapp.R;
import com.example.foodapp.config.Config;
import com.example.foodapp.config.VolleySingleton;
import com.example.foodapp.databinding.FragmentCreateCategoryBinding;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CreateCategoryFragment extends Fragment {
    private static final String TAG = "CreateCategoryFragment";

    public FragmentCreateCategoryBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreateCategoryBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnBack.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new StaffCategoryFragment()).commit();
        });

        Bundle bundle = getArguments();
        if (bundle != null) {
            binding.tvTitle.setText("Update a category");
            binding.btnCreate.setText("Update category");
            Category category = new Gson().fromJson(bundle.getString("category"), Category.class);
            binding.edtName.setText(category.getName());
            binding.edtType.setText(category.getType());
            binding.btnCreate.setOnClickListener(v -> {
                if (binding.edtName.getText().toString().isEmpty()) {
                    binding.layoutEdtName.setError("*Vui lòng nhập dữ liệu");
                    binding.layoutEdtType.setError("*Vui lòng chọn dữ liệu");
                } else {
                    binding.layoutEdtName.setError(null);
                    binding.layoutEdtType.setError(null);
                    updateCategory(category.getId());
                }
            });
        } else {
            binding.btnCreate.setOnClickListener(v -> {
                if (binding.edtName.getText().toString().isEmpty()) {
                    binding.layoutEdtName.setError("*Vui lòng nhập dữ liệu");
                    binding.layoutEdtType.setError("*Vui lòng chọn dữ liệu");
                } else {
                    binding.layoutEdtName.setError(null);
                    binding.layoutEdtType.setError(null);
                    createCategory();
                }
            });
        }
    }

    public void createCategory() {
        VolleySingleton.getInstance(requireActivity()).addToRequestQueue(new StringRequest(Request.Method.POST, Config.IP + "category/addCategory", response -> {
            Toast.makeText(requireActivity(), "onSuccess!", Toast.LENGTH_SHORT).show();
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new StaffCategoryFragment()).commit();
        }, error -> {
            Toast.makeText(requireActivity(), "onFailure: " + error.networkResponse.statusCode, Toast.LENGTH_SHORT).show();
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("name", Objects.requireNonNull(binding.edtName.getText()).toString());
                params.put("type", binding.edtType.getText().toString());
                return params;
            }
        });
    }

    public void updateCategory(String id) {
        VolleySingleton.getInstance(requireActivity()).addToRequestQueue(new StringRequest(Request.Method.PUT, Config.IP + "category/updateCategory/" + id, response -> {
            Toast.makeText(requireActivity(), "onSuccess!", Toast.LENGTH_SHORT).show();
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new StaffCategoryFragment()).commit();
        }, error -> {
            Toast.makeText(requireActivity(), "onFailure: " + error.networkResponse.statusCode, Toast.LENGTH_SHORT).show();
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("name", Objects.requireNonNull(binding.edtName.getText()).toString());
                params.put("type", binding.edtType.getText().toString());
                return params;
            }
        });
    }
}