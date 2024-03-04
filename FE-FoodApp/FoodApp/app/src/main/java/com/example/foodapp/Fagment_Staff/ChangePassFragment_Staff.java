package com.example.foodapp.Fagment_Staff;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodapp.R;
import com.example.foodapp.config.Config;

import java.util.HashMap;
import java.util.Map;

public class ChangePassFragment_Staff extends Fragment {
    private Button btnResert, btnSave;
    private EditText passOld, passNew, retypePass;
    private ImageView back;
    private String _id = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        passOld = view.findViewById(R.id.pass_old);
        passNew = view.findViewById(R.id.pass_new);
        retypePass = view.findViewById(R.id.retype_passnew);
        btnResert = view.findViewById(R.id.btn_resert);
        btnSave = view.findViewById(R.id.btn_save);
        back = view.findViewById(R.id.back_w);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, new MoreFragment_Staff())
                        .commit();
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

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Profile", Context.MODE_PRIVATE);
        String id = sharedPreferences.getString("_id", "");
        _id = id;

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassStaff();
            }
        });

        return view;
    }

    private void changePassStaff(){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.IP + "change-password/",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Thành công", "Đổi mật khẩu thành công" + response.toString());
                        Toast.makeText(getContext(), "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();

                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        MoreFragment_Staff moreFragmentStaff = new MoreFragment_Staff();
                        transaction.replace(R.id.frameLayout, moreFragmentStaff);
                        transaction.commit();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Lối", "Đổi mật khẩu không thành công" + error.getMessage());
                Toast.makeText(getContext(), "Đổi mật khẩu không thành công", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("_id", _id);
                params.put("oldPassword", passOld.getText().toString());
                params.put("newPassword", passNew.getText().toString());
                params.put("reNewPassword", retypePass.getText().toString());

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

}
