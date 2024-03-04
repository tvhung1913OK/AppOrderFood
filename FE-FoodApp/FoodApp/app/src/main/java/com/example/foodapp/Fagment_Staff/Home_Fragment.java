package com.example.foodapp.Fagment_Staff;

import static com.example.foodapp.config.Config.IP;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.example.foodapp.R;
import com.example.foodapp.adapter_staff.HomeAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Home_Fragment extends Fragment {
    private ArrayList<Map<String, String>> orderDetails = new ArrayList<>();
    HomeAdapter adapter;
    int totalPriceOrder;
    int totalDisOrder;
    TextView Total_Revenue,Total_bill;

    int totalCount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment_staff, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Total_Revenue = view.findViewById(R.id.Total_Revenue);
        Total_bill = view.findViewById(R.id.Total_bill);
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        fetchDailyRevenueFromAPI(currentDate);
        RecyclerView recyclerView = view.findViewById(R.id.rvHomeStaff);
        adapter = new HomeAdapter(getContext(), orderDetails);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        fetchDataFromAPI();

    }
    private void fetchDailyRevenueFromAPI(String date) {
        String apiUrl = IP + "order-detail/getDailyRevenue/" + date;

        RequestQueue queue = Volley.newRequestQueue(getContext(), new HurlStack());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);


                            String dailyDate = jsonResponse.getString("date");
                            int dailyRevenue = jsonResponse.getInt("revenue");
                            DecimalFormat decimalFormat = new DecimalFormat("#,###");
                            String formattedRevenue = decimalFormat.format(dailyRevenue);

                            Total_Revenue.setText(String.valueOf(formattedRevenue)+"VND");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors here
                        Log.e("Error", "Error fetching daily revenue: " + error.toString());
                        Toast.makeText(getActivity(), "Error fetching daily revenue", Toast.LENGTH_SHORT).show();
                    }
                }
        );


        queue.add(stringRequest);
    }

    private void fetchDataFromAPI() {
        String apiUrl = IP + "order-detail";

        RequestQueue queue = Volley.newRequestQueue(getContext(), new HurlStack());


        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            Total_bill.setText(String.valueOf(jsonArray.length()));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                totalPriceOrder = 0;
                                totalCount = 0;
                                totalDisOrder = 0;
                                JSONObject orderDetailObject = jsonArray.getJSONObject(i);
                                Map<String, String> orderDetailMap = new HashMap<>();
                                orderDetailMap.put("orderItem", orderDetailObject.getString("name"));


                                JSONArray idOrderItemArray = orderDetailObject.getJSONArray("idOrderItem");

                                for (int j = 0; j < idOrderItemArray.length(); j++) {
                                    JSONObject orderItemObject = idOrderItemArray.getJSONObject(j);


                                    if (!orderItemObject.isNull("idProduct")) {
                                        JSONObject idProductObject = orderItemObject.getJSONObject("idProduct");

                                        if (idProductObject.has("price")) {
                                            int price = idProductObject.getInt("price");
                                            int quantityOrder = orderItemObject.getInt("quantity");
                                            totalPriceOrder += price * quantityOrder;
                                        }
                                    }
                                }
                                orderDetailMap.put("price", String.valueOf(totalPriceOrder));
                                for (int j = 0; j < idOrderItemArray.length(); j++) {
                                    JSONObject orderDetailObject1 = idOrderItemArray.getJSONObject(j);
                                    int count = orderDetailObject1.getInt("quantity");
                                    totalCount += count;
                                }
                                for (int j = 0; j < idOrderItemArray.length(); j++) {
                                    JSONObject orderItemObject = idOrderItemArray.getJSONObject(j);


                                    JSONObject idCustomerObject = orderItemObject.optJSONObject("idCustomer");
                                    if (idCustomerObject != null) {
                                        JSONObject idUserObject = idCustomerObject.optJSONObject("idUser");
                                        if (idUserObject != null) {
                                            String customerName = idUserObject.optString("name", "");
                                            String customerPhone = idUserObject.optString("phone", "");
                                            String customerAddress = idUserObject.optString("address", "");


                                            orderDetailMap.put("customerName", customerName);
                                            orderDetailMap.put("customerPhone", customerPhone);
                                            orderDetailMap.put("customerAddress", customerAddress);
                                        }
                                    }


                                }




                                orderDetailMap.put("count", String.valueOf(totalCount));
                                orderDetailMap.put("totalPrice", orderDetailObject.getString("totalPrice"));
                                orderDetailMap.put("createAt", orderDetailObject.getString("createdAt"));


                                orderDetails.add(orderDetailMap);
                            }


                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Lỗi", "Lỗi khi lấy dữ liệu từ API: " + error.toString());

                        if (error instanceof TimeoutError) {
                            Log.e("Lỗi", "Timeout Error");
                        } else if (error instanceof NoConnectionError) {
                            Log.e("Lỗi", "No Connection Error");
                        } else if (error instanceof NetworkError) {
                            Log.e("Lỗi", "Network Error");
                        } else if (error instanceof ServerError) {
                            Log.e("Lỗi", "Server Error");
                        } else if (error instanceof ParseError) {
                            Log.e("Lỗi", "Parse Error");
                        } else {
                            Log.e("Lỗi", "Unknown Error");
                        }

                        Toast.makeText(getActivity(), "Đã xảy ra lỗi khi lấy dữ liệu từ API", Toast.LENGTH_SHORT).show();
                    }
                }

        );
        int timeout = 10000;
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                timeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));


        queue.add(stringRequest);

    }
}
