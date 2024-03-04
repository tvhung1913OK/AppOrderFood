package com.example.foodapp.Fragment_Customer;

import static com.example.foodapp.config.Config.IP;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import com.example.foodapp.Adapter_Customer.OrderDetailAdapter;
import com.example.foodapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class HistoryFragment extends Fragment {
    private ArrayList<Map<String, String>> orderDetails = new ArrayList<>();
    OrderDetailAdapter adapter;
    int totalPriceOrder;
    int totalDisOrder;

    int totalCount;
    String customerId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_fragment, container, false);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Profile", Context.MODE_PRIVATE);
        customerId = sharedPreferences.getString("customerId", "");

        RecyclerView recyclerView = view.findViewById(R.id.lvItem);
        adapter = new OrderDetailAdapter(getContext(), orderDetails);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);


        fetchDataFromAPI(customerId);

        return view;
    }

    private void fetchDataFromAPI(String idCustomer) {
        String apiUrl = IP + "order-detail/" + idCustomer;
        Log.e("idCustomer", "fetchDataFromAPI: " + idCustomer);
        RequestQueue queue = Volley.newRequestQueue(getContext(), new HurlStack());


        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
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
