package com.example.foodapp.Fragment_Customer;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodapp.Customer_Activity;
import com.example.foodapp.R;
import com.example.foodapp.config.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ChangePass_Fragment extends Fragment {

    private Button btnResert, btnSave;
    private EditText passOld, passNew, retypePass;

    private ImageView img_Back;

    private String _id = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_pass_, container, false);
        passOld = view.findViewById(R.id.pass_old);
        passNew = view.findViewById(R.id.pass_new);
        retypePass = view.findViewById(R.id.retype_passnew);
        btnResert = view.findViewById(R.id.btn_resert);
        btnSave = view.findViewById(R.id.btn_save);
        img_Back = view.findViewById(R.id.img_back_changepass);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Profile", Context.MODE_PRIVATE);
        String id = sharedPreferences.getString("_id", "");
        _id = id;
        img_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.frameLayout, new More_Fragment()).commit();
            }
        });

        btnResert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passOld.setText("");
                passNew.setText("");
                retypePass.setText("");
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });

        return view;
    }

    private void changePassword() {
        RequestQueue queue = Volley.newRequestQueue(getContext());

        StringRequest request = new StringRequest(Request.Method.POST, Config.IP + "change-password/",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Thành công", "Đổi mật khẩu thành công " + response.toString());
                        Toast.makeText(getContext(), "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();

                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        More_Fragment moreFragment = new More_Fragment();
                        fragmentTransaction.replace(R.id.frameLayout, moreFragment);
                        fragmentTransaction.commit();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Lỗi", "Đổi mật khẩu thất bại" + error.getMessage());
                Toast.makeText(getContext(), "Đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("_id", _id);
                params.put("oldPassword", passOld.getText().toString());
                params.put("newPassword", passNew.getText().toString());
                params.put("reNewPassword", retypePass.getText().toString());
                return params;
            }
        };
        queue.add(request);
    }
}