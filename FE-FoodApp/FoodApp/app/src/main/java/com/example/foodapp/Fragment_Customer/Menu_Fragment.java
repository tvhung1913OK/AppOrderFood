package com.example.foodapp.Fragment_Customer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.example.foodapp.Adapter_Customer.ViewPagerMenuAdapter;
import com.example.foodapp.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class Menu_Fragment extends Fragment {
   private TabLayout tabLayout;
   private ViewPager2 viewPager2;
   ViewPagerMenuAdapter adapter;
   private ImageView imgCart;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu_customer, container, false);
        SearchView sv_foods = view.findViewById(R.id.sv_foods);



        viewPager2 = view.findViewById(R.id.paperMenu);
        tabLayout = view.findViewById(R.id.tabMenu);

        imgCart = view.findViewById(R.id.img_cart);

        adapter = new ViewPagerMenuAdapter(requireActivity());
        viewPager2.setAdapter(adapter);

        viewPager2.setUserInputEnabled(false);

        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            switch (position){
                case 0:
                    tab.setText("Foods");
                    break;
                case 1:
                    tab.setText("Drink");
                    break;
            }
        }).attach();

        imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();


                FragmentTransaction transaction = fragmentManager.beginTransaction();

                transaction.replace(R.id.frameLayout, new CartFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return view;
    }
}