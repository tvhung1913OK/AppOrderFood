package com.example.foodapp.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodapp.Customer_Activity;
import com.example.foodapp.MainActivity;
import com.example.foodapp.R;
import com.example.foodapp.config.Config;
import com.example.foodapp.databinding.ActivityLoginStaffBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity_Staff extends AppCompatActivity {

    private ActivityLoginStaffBinding binding;
    String IP = Config.IP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginStaffBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginStaff();
            }
        });

        binding.btnLoginCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity_Staff.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginStaff() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        StringRequest request = new StringRequest(Request.Method.POST, Config.IP + "auth/loginStaff",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Thành công", "Đăng nhập thành công " + response.toString() );
                        Toast.makeText(getApplicationContext(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONObject userObject = jsonObject.getJSONObject("user");
                            String _id = userObject.getString("_id");
                            String name = userObject.getString("name");
                            String phone = userObject.getString("phone");
                            String password = userObject.getString("password");
                            String date = userObject.getString("date");
                            String sex = userObject.getString("sex");
                            String image = userObject.getString("image");
                            String email = userObject.getString("email");
                            String address = userObject.getString("address");

                            JSONObject staffObject = jsonObject.getJSONObject("staff");
                            String role = staffObject.getString("role");

                            // Lưu thông tin người dùng đăng nhập vào SharedPreferences
                            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Profile", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("_id", _id);
                            editor.putString("name", name);
                            editor.putString("phone", phone);
                            editor.putString("password", password);
                            editor.putString("date", date);
                            editor.putString("sex", sex);
                            editor.putString("image", image);
                            editor.putString("email", email);
                            editor.putString("address", address);
                            editor.putString("role", role);
                            editor.apply();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        Intent intent = new Intent(LoginActivity_Staff.this, MainActivity.class);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Lỗi", "Đăng nhập thất bại" + error.getMessage()  );
                Toast.makeText(getApplicationContext(), "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
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