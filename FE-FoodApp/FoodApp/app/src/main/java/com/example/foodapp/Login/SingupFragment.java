package com.example.foodapp.Login;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodapp.Customer_Activity;
import com.example.foodapp.R;
import com.example.foodapp.config.Config;
import com.example.foodapp.databinding.FragmentLoginCustomerBinding;
import com.example.foodapp.databinding.FragmentSingupBinding;

import java.util.HashMap;
import java.util.Map;

public class SingupFragment extends Fragment {
    private FragmentSingupBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSingupBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.singupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }

    private void signUp() {
        RequestQueue queue = Volley.newRequestQueue(getContext());

        StringRequest request = new StringRequest(Request.Method.POST, Config.IP + "auth/register", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("SignUp", "Đăng ký thành công " + response.toString() );
                Toast.makeText(getContext(), "Đăng ký thành công", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("SignUp", "Đăng ký thất bại " + error.getMessage() );
                Toast.makeText(getContext(), "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params =new HashMap<>();
                params.put("name", binding.singupName.getText().toString());
                params.put("phone", binding.singupPhone.getText().toString());
                params.put("password", binding.singupPass.getText().toString());
                params.put("date", binding.singupDate.getText().toString());
                params.put("sex", binding.singupSex.getText().toString());
                params.put("image", binding.singupImage.getText().toString());
                params.put("email", binding.singupEmail.getText().toString());
                params.put("address", binding.singupAddress.getText().toString());

                return params;
            }
        };
        queue.add(request);
    }
}