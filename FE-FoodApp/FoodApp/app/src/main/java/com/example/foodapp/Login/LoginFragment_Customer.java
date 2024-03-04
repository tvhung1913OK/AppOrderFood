package com.example.foodapp.Login;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodapp.Customer_Activity;
import com.example.foodapp.config.Config;
import com.example.foodapp.databinding.FragmentLoginCustomerBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginFragment_Customer extends Fragment {

    private FragmentLoginCustomerBinding binding;
    String IP = Config.IP;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginCustomerBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.txtForgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginCustomer();
            }
        });

        binding.btnLoginstaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginActivity_Staff.class);
                startActivity(intent);
            }
        });
    }

    private void loginCustomer() {
        RequestQueue queue = Volley.newRequestQueue(getContext());

        StringRequest request = new StringRequest(Request.Method.POST, Config.IP + "auth/loginCustomer",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Thành công", "Đăng nhập thành công " + response.toString() );
                        Toast.makeText(getContext(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String _id = jsonObject.getJSONObject("user").getString("_id");
                            String name = jsonObject.getJSONObject("user").getString("name");
                            String phone = jsonObject.getJSONObject("user").getString("phone");
                            String password = jsonObject.getJSONObject("user").getString("password");
                            String date = jsonObject.getJSONObject("user").getString("date");
                            String sex = jsonObject.getJSONObject("user").getString("sex");
                            String image = jsonObject.getJSONObject("user").getString("image");
                            String email = jsonObject.getJSONObject("user").getString("email");
                            String address = jsonObject.getJSONObject("user").getString("address");
                            String customerId = jsonObject.getJSONObject("customer").getString("_id");
                            // Lưu thông tin người dùng đăng nhập vào SharedPreferences
                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Profile", MODE_PRIVATE);
                            Editor editor = sharedPreferences.edit();
                            editor.putString("_id", _id);
                            editor.putString("name", name);
                            editor.putString("phone", phone);
                            editor.putString("password", password);
                            editor.putString("date", date);
                            editor.putString("sex", sex);
                            editor.putString("image", image);
                            editor.putString("email", email);
                            editor.putString("address", address);
                            editor.putString("customerId", customerId);
                            editor.apply();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        Intent intent = new Intent(getActivity(), Customer_Activity.class);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Lỗi", "Đăng nhập thất bại" + error.getMessage()  );
                Toast.makeText(getContext(), "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params =new HashMap<>();

                params.put("email", binding.loginEmail.getText().toString());
                params.put("password", binding.loginPass.getText().toString());

                return params;
            }
        };
        queue.add(request);
    }
}