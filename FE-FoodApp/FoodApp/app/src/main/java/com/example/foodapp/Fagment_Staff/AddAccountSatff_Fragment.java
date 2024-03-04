package com.example.foodapp.Fagment_Staff;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.foodapp.Model.Staff;
import com.example.foodapp.R;
import com.example.foodapp.config.Config;
import com.example.foodapp.config.VolleySingleton;
import com.example.foodapp.databinding.FragmentAddAccountBinding;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddAccountSatff_Fragment extends Fragment {
    public FragmentAddAccountBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddAccountBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new ManagerAccountFragment()).commit());

        binding.btnReset.setOnClickListener(v -> {
            binding.edtName.setText(null);
            binding.edtPhone.setText(null);
            binding.edtDate.setText(null);
            binding.edtPassword.setText(null);
            binding.edtEmail.setText(null);
            binding.edtAddress.setText(null);
            binding.edtImage.setText(null);
            binding.rdoNam.setChecked(true);
        });

        binding.edtDate.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            final int year = calendar.get(Calendar.YEAR);
            final int month = calendar.get(Calendar.MONTH);
            final int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(requireActivity(), (datePicker, year1, month1, dayOfMonth) -> {
                month1 = month1 + 1;
                String date = dayOfMonth + "/" + month1 + "/" + year1;
                binding.edtDate.setText(date);
            }, year, month, day);
            datePickerDialog.show();
        });

        Bundle bundle = getArguments();
        if (bundle != null) {
            Staff staff = new Gson().fromJson(bundle.getString("staff"), Staff.class);
            binding.tvTitle.setText("Update Account");
            binding.edtName.setText(staff.getUser().getName());
            binding.edtPhone.setText(staff.getUser().getPhone());
            binding.edtDate.setText(staff.getUser().getDate());
            binding.tvPassword.setVisibility(View.GONE);
            binding.edtPassword.setVisibility(View.GONE);
            binding.tvEmail.setVisibility(View.GONE);
            binding.edtEmail.setVisibility(View.GONE);
            binding.edtAddress.setText(staff.getUser().getAddress());
            binding.edtImage.setText(staff.getUser().getImage());
            if (staff.getUser().getSex().equalsIgnoreCase("Nam")) {
                binding.rdoNam.setChecked(true);
            } else {
                binding.rdoNu.setChecked(true);
            }
            binding.btnSave.setOnClickListener(v -> updateStaff(staff.getUser().getId()));
        } else {
            binding.btnSave.setOnClickListener(v -> createStaff());
        }
    }

    public void createStaff() {
        VolleySingleton.getInstance(requireActivity()).addToRequestQueue(new StringRequest(Request.Method.POST, Config.IP + "staff/addStaff", response -> {
            Toast.makeText(requireActivity(), "onSuccess!", Toast.LENGTH_SHORT).show();
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new ManagerAccountFragment()).commit();
        }, error -> {
            Toast.makeText(requireActivity(), "onFailure: " + error.networkResponse.statusCode + "\n" + error, Toast.LENGTH_SHORT).show();
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", binding.edtName.getText().toString());
                params.put("phone", binding.edtPhone.getText().toString());
                params.put("password", binding.edtPassword.getText().toString());
                params.put("date", binding.edtDate.getText().toString());
                params.put("email", binding.edtEmail.getText().toString());
                params.put("address", binding.edtAddress.getText().toString());
                params.put("image", binding.edtImage.getText().toString());
                if (binding.rdoNam.isChecked()) {
                    params.put("sex", binding.rdoNam.getText().toString());
                } else {
                    params.put("sex", binding.rdoNu.getText().toString());
                }
                return params;
            }
        });
    }

    public void updateStaff(String id) {
        VolleySingleton.getInstance(requireActivity()).addToRequestQueue(new StringRequest(Request.Method.PUT, Config.IP + "staff/updateStaff/" + id, response -> {
            Toast.makeText(requireActivity(), "onSuccess!", Toast.LENGTH_SHORT).show();
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new ManagerAccountFragment()).commit();
        }, error -> {
            Toast.makeText(requireActivity(), "onFailure: " + error.networkResponse.statusCode, Toast.LENGTH_SHORT).show();
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", binding.edtName.getText().toString());
                params.put("phone", binding.edtPhone.getText().toString());
                params.put("date", binding.edtDate.getText().toString());
                params.put("email", binding.edtEmail.getText().toString());
                params.put("address", binding.edtAddress.getText().toString());
                params.put("image", binding.edtImage.getText().toString());
                if (binding.rdoNam.isChecked()) {
                    params.put("sex", binding.rdoNam.getText().toString());
                } else {
                    params.put("sex", binding.rdoNu.getText().toString());
                }
                return params;
            }
        });
    }
}