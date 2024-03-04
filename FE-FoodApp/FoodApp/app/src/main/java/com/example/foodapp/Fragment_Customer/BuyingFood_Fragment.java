package com.example.foodapp.Fragment_Customer;

import static android.content.ContentValues.TAG;
import static com.example.foodapp.config.Config.IP;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class BuyingFood_Fragment extends Fragment {
    private TextView tvQuantity;
    Button btnIncre, btnDecre;
    boolean orderSuccessful = false;
    ImageView btnBackBuying;

    private int quantity = 1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.buying_food_fragment, container, false);


        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.INVISIBLE);
        }

        Bundle args = getArguments();
        if (args != null) {
            String productName = args.getString("productName");
            int productPrice = args.getInt("productPrice");
            String productImg = args.getString("productImg");
            String productDes = args.getString("productDes");
            int productDis = args.getInt("productDiscount");
            String productID= args.getString("productId");

            int Discount = (productPrice * productDis) / 100;
            SpannableString spannableString = new SpannableString(String.valueOf(productDis));


            spannableString.setSpan(new StrikethroughSpan(), 0, String.valueOf(productDis).length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            int total = productPrice - Discount;


            TextView tvProductName = view.findViewById(R.id.tvProductName);
            TextView tvProductPrice = view.findViewById(R.id.tvProductPrice);
            ImageView imgPro = view.findViewById(R.id.imgProduct);
            TextView tvDes = view.findViewById(R.id.DesProduct);
            TextView tvTotal = view.findViewById(R.id.totalProduct);
            Button btnOrder = view.findViewById(R.id.addOrder);
            tvQuantity = view.findViewById(R.id.tvQuantity);
            btnIncre = view.findViewById(R.id.btnIncrease);
            btnBackBuying = view.findViewById(R.id.btnBackBuying);
            btnDecre = view.findViewById(R.id.btnDecrease);

            tvProductName.setText(productName);
            tvProductPrice.setText("Price: " + productPrice);
            tvDes.setText(productDes);
            tvTotal.setText("Total: " + total+" ("+spannableString+"%)");
            Picasso.get().load(productImg).into(imgPro);
            btnBackBuying.setOnClickListener(new View.OnClickListener() {
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
            btnIncre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    increaseQuantity(view);
                }

            });
            btnDecre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    decreaseQuantity(view);
                }
            });
            btnOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String productId = args.getString("productId");
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Profile", Context.MODE_PRIVATE);
                    String customerId = sharedPreferences.getString("customerId", "");
                    int orderQuantity = quantity;

                    String url = IP + "order-item/addOrderItem";

                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            orderSuccessful = true;
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();


                            FragmentTransaction transaction = fragmentManager.beginTransaction();


                            transaction.replace(R.id.frameLayout, new CartFragment());


                            transaction.addToBackStack(null);


                            transaction.commit();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, "onErrorResponse: ", error);
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("idProduct", productId);
                            params.put("idCustomer", customerId);
                            params.put("quantity", String.valueOf(orderQuantity));
                            params.put("totalPrice",String.valueOf(total));
                            return params;
                        }
                    };

                    requestQueue.add(stringRequest);

                }
            });


        }




        return view;
    }


    public void increaseQuantity(View view) {
        quantity++;
        updateQuantity();
    }


    public void decreaseQuantity(View view) {
        if (quantity > 1) {
            quantity--;
            updateQuantity();
        }
    }


    private void updateQuantity() {
        tvQuantity.setText(String.valueOf(quantity));
    }

    @Override
    public void onDestroyView() {
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
        if (bottomNavigationView != null && !orderSuccessful) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
        super.onDestroyView();
    }


}