package com.example.foodapp.Fragment_Customer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.foodapp.Fagment_Staff.MoreFragment_Staff;
import com.example.foodapp.Model.Staff;
import com.example.foodapp.R;
import com.example.foodapp.config.Config;
import com.example.foodapp.config.VolleySingleton;
import com.example.foodapp.databinding.FragmentProfileBinding;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class Profile_Fragment extends Fragment {
    public FragmentProfileBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(getLayoutInflater(), container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.imgBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new More_Fragment()).commit());

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("Profile", Context.MODE_PRIVATE);
        String id = sharedPreferences.getString("_id", "");

        getDataUserStaff(id);

        editChange(id);
    }

    private void getDataUserStaff(String id) {
        VolleySingleton.getInstance(requireActivity()).addToRequestQueue(new StringRequest(Config.IP + "customer/getCustomerById/" + id, response -> {
            Staff staff = new Gson().fromJson(response, Staff.class);
            binding.edtName.setText(staff.getUser().getName());
            binding.edtPhone.setText(staff.getUser().getPhone());
            binding.edtDate.setText(staff.getUser().getDate());
            binding.edtGender.setText(staff.getUser().getSex());
            binding.edtAddress.setText(staff.getUser().getAddress());
            binding.edtImage.setText(staff.getUser().getImage());
            Glide.with(requireActivity()).load(staff.getUser().getImage()).centerCrop().into(binding.imgAvt);
        }, error -> {
            Toast.makeText(requireActivity(), "onFailure: " + error.networkResponse.statusCode, Toast.LENGTH_SHORT).show();
        }));
    }

    public void editChange(String id) {
        binding.editSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.layoutName.setEnabled(true);
                binding.layoutPhone.setEnabled(true);
                binding.layoutDate.setEnabled(true);
                binding.layoutGender.setEnabled(true);
                binding.layoutAddress.setEnabled(true);
                binding.layoutImage.setVisibility(View.VISIBLE);
            } else {
                binding.layoutName.setEnabled(false);
                binding.layoutPhone.setEnabled(false);
                binding.layoutDate.setEnabled(false);
                binding.layoutGender.setEnabled(false);
                binding.layoutAddress.setEnabled(false);
                binding.layoutImage.setVisibility(View.GONE);
                VolleySingleton.getInstance(requireActivity()).addToRequestQueue(new StringRequest(Request.Method.PUT, Config.IP + "customer/updateCustomer/" + id, response -> {
                    Toast.makeText(requireActivity(), "onSuccess!", Toast.LENGTH_SHORT).show();
                }, error -> {
                    Toast.makeText(requireActivity(), "onFailure: " + error.networkResponse.statusCode, Toast.LENGTH_SHORT).show();
                }) {
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("name", Objects.requireNonNull(binding.edtName.getText()).toString());
                        params.put("phone", Objects.requireNonNull(binding.edtPhone.getText()).toString());
                        params.put("sex", binding.edtGender.getText().toString());
                        params.put("date", Objects.requireNonNull(binding.edtDate.getText()).toString());
                        params.put("address", Objects.requireNonNull(binding.edtAddress.getText()).toString());
                        params.put("image", Objects.requireNonNull(binding.edtImage.getText()).toString());
                        return params;
                    }
                });
            }
        });
    }
}