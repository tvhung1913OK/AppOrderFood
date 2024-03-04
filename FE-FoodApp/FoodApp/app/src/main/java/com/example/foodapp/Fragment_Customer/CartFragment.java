package com.example.foodapp.Fragment_Customer;

import static com.example.foodapp.config.Config.IP;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodapp.Adapter_Customer.CartAdapter;
import com.example.foodapp.Interface.OnTotalPriceUpdateListener;
import com.example.foodapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CartFragment extends Fragment implements OnTotalPriceUpdateListener{
    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private ArrayList<Map<String, String>> cartData = new ArrayList<>();
    Button btnAddDetail;
    ImageView btnBack;
    String customerId;
    TextView tvDiscountDetail, tvTotalDetail;
    CheckBox checkboxAllItem;
    boolean isSelectAllChecked = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.INVISIBLE);
        }

        BuyingFood_Fragment buyingFoodFragment = (BuyingFood_Fragment) getActivity().getSupportFragmentManager().findFragmentByTag("BuyingFoodFragmentTag");
        if (buyingFoodFragment != null) {
            buyingFoodFragment.orderSuccessful = false;
        }
        checkboxAllItem = view.findViewById(R.id.checkboxAllItem);

        recyclerView = view.findViewById(R.id.recyclerViewCart);
        btnAddDetail = view.findViewById(R.id.addOrderDetail);
        btnBack = view.findViewById(R.id.btnBackCart);
        tvDiscountDetail=view.findViewById(R.id.DiscountAllOrder);
        tvTotalDetail = view.findViewById(R.id.TotalAllOrder);

        checkboxAllItem.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isSelectAllChecked = isChecked;
            updateAllCheckBoxes(isChecked);
            cartAdapter.updateTotalPrice();
        });
        cartAdapter = new CartAdapter(cartData, getContext());

        cartAdapter.setOnTotalPriceUpdateListener((OnTotalPriceUpdateListener) this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        recyclerView.setAdapter(cartAdapter);



        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Profile", Context.MODE_PRIVATE);
         customerId = sharedPreferences.getString("customerId", "");
        getOrderItemsByIdCustomer(customerId);

        btnAddDetail.setOnClickListener(v -> {
            ArrayList<Map<String, String>> selectedOrderItems = cartAdapter.getSelectedOrderItems();
            Log.d("CartAdapter", "Selected Order Items: " + selectedOrderItems);
            if (selectedOrderItems.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng chọn ít nhất một sản phẩm để thêm OrderDetail.", Toast.LENGTH_SHORT).show();
            } else {
                addOrderDetail(selectedOrderItems);
                cartAdapter.clearSelectedOrderItemIds();

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();


                fragmentManager.popBackStack();


                BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
                if (bottomNavigationView != null) {
                    bottomNavigationView.setVisibility(View.VISIBLE);
                }
            }
        });


        cartAdapter.notifyDataSetChanged();

        return view;
    }
    private void updateAllCheckBoxes(boolean isChecked) {
        for (Map<String, String> orderItem : cartData) {
            orderItem.put("isChecked", String.valueOf(isChecked));
        }
        cartAdapter.notifyDataSetChanged();
    }

    private void updateRecyclerView(String idCustomer) {
        getOrderItemsByIdCustomer(idCustomer);
        cartAdapter.notifyDataSetChanged();
    }

    private void addOrderDetail(ArrayList<Map<String, String>> selectedOrderItems) {

        Iterator<Map<String, String>> iterator = selectedOrderItems.iterator();
        while (iterator.hasNext()) {
            Map<String, String> orderItem = iterator.next();
            if (!Boolean.parseBoolean(orderItem.get("isChecked"))) {
                iterator.remove();
            }
        }


        if (selectedOrderItems.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng chọn ít nhất một sản phẩm để thêm OrderDetail.", Toast.LENGTH_SHORT).show();
            return;
        }


        String url = IP + "order-detail/create";
        RequestQueue queue = Volley.newRequestQueue(getContext());


        JSONObject orderDetailObject = new JSONObject();
        try {
            orderDetailObject.put("name", "order detail 1");
            orderDetailObject.put("totalPrice", calculateTotalPrice(selectedOrderItems));

            JSONArray idOrderItemsArray = new JSONArray();
            for (Map<String, String> orderItem : selectedOrderItems) {
                idOrderItemsArray.put(orderItem.get("idOrderItem"));
            }
            orderDetailObject.put("idOrderItem", idOrderItemsArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, orderDetailObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        getOrderItemsByIdCustomer(customerId);
                        cartAdapter.updateTotalPrice();
                        cartAdapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "Thêm OrderDetail thành công.", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(), "Lỗi khi thêm OrderDetail: " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });


        int timeout = 10000;
        request.setRetryPolicy(new DefaultRetryPolicy(
                timeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        queue.add(request);
    }



    private int calculateTotalPrice(ArrayList<Map<String, String>> selectedOrderItems) {
        int total = 0;
        for (Map<String, String> orderItem : selectedOrderItems) {
            total += Integer.parseInt(orderItem.get("TotalOrder"));
        }
        return total;
    }



    @Override
    public void onTotalPriceUpdated(int totalPrice,int totalDiscount) {
        tvTotalDetail.setText(totalPrice+" VND");
        tvDiscountDetail.setText(totalDiscount+ "VND");
        checkboxAllItem.setChecked(isSelectAllChecked);
    }

    public void updateCartData(ArrayList<Map<String, String>> newCartData) {
        if (cartData != null) {
            cartData.clear();
            cartData.addAll(newCartData);
            updateAllCheckBoxes(isSelectAllChecked);
            cartAdapter.notifyDataSetChanged();
        } else {
            cartData = new ArrayList<>(newCartData);
            cartAdapter.notifyDataSetChanged();
        }
    }


    private void getOrderItemsByIdCustomer(String idCustomer) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = IP + "order-item/getByIdCustomer/" + idCustomer;

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            ArrayList<Map<String, String>> newCartData = new ArrayList<>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);


                                String status = jsonObject.getString("status");
                                if (!TextUtils.isEmpty(status) && status.equals("unconfirm")) {
                                    String productName = jsonObject.getJSONObject("idProduct").getString("name");
                                    String productDiscount = jsonObject.getJSONObject("idProduct").getJSONObject("idDiscount").getString("discountPerson");
                                    String productPrice = jsonObject.getJSONObject("idProduct").getString("price");
                                    String quantity = jsonObject.getString("quantity");
                                    String ImageProduct = jsonObject.getJSONObject("idProduct").getString("image");
                                    int Discount = (Integer.valueOf(productPrice) * Integer.valueOf(productDiscount)) / 100;
                                    int Total = (Integer.valueOf(productPrice) - Discount);
                                    int TotalOrder= Total * Integer.valueOf(quantity);
                                    int TotalDiscount = Discount*Integer.valueOf(quantity);

                                    Map<String, String> orderItem = new HashMap<>();
                                    orderItem.put("productName", productName);
                                    orderItem.put("productPrice", productPrice);
                                    orderItem.put("productQuantity", quantity);
                                    orderItem.put("imageProductOrder", ImageProduct);
                                    orderItem.put("productTotal", String.valueOf(Total));
                                    orderItem.put("idOrderItem", jsonObject.getString("_id"));
                                    orderItem.put("TotalOrder", String.valueOf(TotalOrder));
                                    orderItem.put("TotalDiscount", String.valueOf(TotalDiscount));

                                    newCartData.add(orderItem);
                                }
                            }


                            updateCartData(newCartData);
                            cartAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
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

                Toast.makeText(getContext(), "Đã xảy ra lỗi khi lấy dữ liệu từ API", Toast.LENGTH_SHORT).show();
            }
        });
        int timeout = 10000;
        request.setRetryPolicy(new DefaultRetryPolicy(
                timeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        queue.add(request);
    }



    @Override
    public void onDestroyView() {

//        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
//        if (bottomNavigationView != null) {
//            bottomNavigationView.setVisibility(View.VISIBLE);
//        }
        BuyingFood_Fragment buyingFoodFragment = (BuyingFood_Fragment) getActivity().getSupportFragmentManager().findFragmentByTag("BuyingFoodFragmentTag");
        if (buyingFoodFragment != null) {
            buyingFoodFragment.orderSuccessful = false;
        }
        cartAdapter.setOnTotalPriceUpdateListener(null);
        super.onDestroyView();
    }
}